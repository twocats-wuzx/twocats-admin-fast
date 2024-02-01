package com.community.manager.init;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.File;

public class AppInitializer {

    public static final String CACHE_DIRECTORY = "db";
    public static final String DB_NAME = "order.db";
    public static final String SOURCE_DB_PATH = "db/order.db";
    public static final Logger log = LoggerFactory.getLogger(AppInitializer.class);

    public static void init() {
        initDatabase();
    }

    /**
     * 初始化数据库
     */
    private static void initDatabase() {
        String basePath = System.getProperty("user.dir");
        if (StringUtils.hasLength(basePath)){
            File cacheFile = new File(basePath + File.separator + CACHE_DIRECTORY);

            if (!cacheFile.exists() || !cacheFile.isDirectory()){
                if (!cacheFile.mkdir()){
                    log.error("=> [创建缓存文件夹错误]");
                    throw new RuntimeException("缓存文件夹创建错误");
                }
            }
            File file = new File(basePath + File.separator + CACHE_DIRECTORY + File.separator + DB_NAME);
            if (!file.exists() || file.isDirectory()){
                copyDatabaseFile(SOURCE_DB_PATH, file.getPath());
            }
        }

    }

    /**
     * 初始化数据库地址文件
     *
     * @param sourcePath 源地址(资源文件夹内地址)
     * @param targetPath 目标地址
     */
    public static void copyDatabaseFile(String sourcePath, String targetPath) {
        try  {
            log.info("开始初始化数据库");
            FileUtils.copyToFile(new ClassPathResource(sourcePath).getInputStream(), new File(targetPath));
        } catch (Exception e) {
            log.error("=> [拷贝数据库文件失败], errorMessage: ", e);
            throw new RuntimeException(e);
        }
    }

}
