package com.kirito.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class ProxyController {

    @Value("${proxy.origin}")
    private String origin;

    private final Cache<String, ResponseEntity<String>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES) // 缓存有效期
            .build();

    private RestTemplate restTemplate;

    @PostConstruct
    public void init(){
        restTemplate = new RestTemplate();
    }

    @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> test(HttpServletRequest request) throws Exception{
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String fullUrl = origin + path + (query != null ? "?" + query : "");

        // 检查缓存
        ResponseEntity<String> cachedResponse = cache.getIfPresent(fullUrl);
        if (cachedResponse != null){
            return ResponseEntity.status(cachedResponse.getStatusCode())
                    .headers(cachedResponse.getHeaders())
                    .header("X-Cache", "HIT")
                    .body(cachedResponse.getBody());
        }

        // 如果未命中缓存, 转发请求
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        // 缓存响应
        cache.put(fullUrl, response);

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .header("X-Cache", "MISS")
                .body(response.getBody());
    }

    @PostMapping("/clear-cache")
    public String clearCache(){
        cache.invalidateAll();
        return "Cache cleared.";
    }
}
