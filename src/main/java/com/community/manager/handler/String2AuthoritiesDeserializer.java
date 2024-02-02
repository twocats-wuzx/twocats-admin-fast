package com.community.manager.handler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * String2AuthoritiesDeserializer
 * 权限字符串转换为权限集合
 */
public class String2AuthoritiesDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {
    @Override
    public Collection<? extends GrantedAuthority> deserialize(JsonParser jsonParser,
                                                              DeserializationContext deserializationContext)
            throws IOException, JacksonException {

        ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        Iterator<JsonNode> elements = jsonNode.elements();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        }
        return grantedAuthorities;
    }
}
