package com.community.manager.auth;

import cn.hutool.core.lang.UUID;
import com.community.manager.common.AppConstant;
import com.community.manager.module.system.domain.dto.UserDetailDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的Spring Security上下文存储
 */
@Component(value = "redisSecurityContextRepository")
public class RedisSecurityContextRepository implements SecurityContextRepository {

    public static final Integer EXPIRE_MINUTE = 60;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    public static final String AUTH_KET = "community:manager:auth";
    private final SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();
    private static Logger log = LoggerFactory.getLogger(RedisSecurityContextRepository.class);


    public RedisSecurityContextRepository(ObjectMapper objectMapper,
                                          RedisTemplate<String, String> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加载延迟上下文
     * @param request 请求
     * @return 延迟上下文
     */
    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        return new SupplierDeferredSecurityContext(
                () -> readSecurityContextFormRedis(request),
                securityContextHolderStrategy);
    }

    /**
     * 从Redis中加载上下文 该方法已经过时
     * @return 上下文
     * @throws UnsupportedOperationException Method Deprecated
     */
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder)
            throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Method Deprecated");
    }

    /**
     * 保存用户信息 {@link UserDetailDTO}，并将Token放入响应头
     *
     * @param context 上下文
     * @param request 请求
     * @param response 响应
     */
    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String token = UUID.fastUUID().toString(true);
        String key = AUTH_KET + AppConstant.COLON + token;
        Authentication authentication = context.getAuthentication();
        if (authentication == null){
            return;
        }
        UserDetailDTO userDetail = null;
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailDTO)){
            return;
        }
        userDetail = (UserDetailDTO) principal;
        userDetail.setPassword(null);
        try {
            redisTemplate.opsForValue().set(key,
                    objectMapper.writeValueAsString(userDetail),
                    EXPIRE_MINUTE,
                    TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
           throw new RuntimeException("save user detail to redis failed");
        }
        securityContextHolderStrategy.setContext(context);
        response.setHeader(AppConstant.REQUEST_AUTH_KEY, token);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }

    /**
     * 从Redis中读取用户信息 {@link UserDetailDTO}, 需要的Key需要从请求头中获取
     * <p>
     *     如果没有过期则重新设置过期时间
     * <p/>
     *
     * @param request 请求
     * @return 上下文
     */
    public SecurityContext readSecurityContextFormRedis(HttpServletRequest request)  {
        if (request == null){
            return null;
        }
        String authKey = request.getHeader(AppConstant.REQUEST_AUTH_KEY);
        if (!StringUtils.hasLength(authKey)){
            return null;
        }
        String key = AUTH_KET + AppConstant.COLON + authKey;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))){
            redisTemplate.expire(key, EXPIRE_MINUTE, TimeUnit.MINUTES);
            String userDetailStr = redisTemplate.opsForValue().get(key);

            UserDetailDTO userDetailDTO;
            try {
                userDetailDTO = objectMapper.readValue(userDetailStr, UserDetailDTO.class);
            }catch (JsonProcessingException e) {
                log.info("==> read user detail from redis failed, message: {}", e.getMessage());
                return null;
            }

            if (userDetailDTO == null){
                return null;
            }
            LoginAuthenticationToken authenticationToken = new LoginAuthenticationToken(userDetailDTO);
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authenticationToken);
            return context;
        }
        return null;
    }
}
