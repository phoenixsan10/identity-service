package com.phoenixsan.identity.identity_service.redis.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveToken(Long userId, String token) {

        // key = userId
        // value = jwt token
        redisTemplate.opsForValue()
                .set(
                        String.valueOf(userId),
                        token,
                        1,
                        TimeUnit.DAYS);
    }

    public String getToken(Long userId) {

        return redisTemplate.opsForValue()
                .get(String.valueOf(userId));
    }

    public void deleteToken(Long userId) {

        redisTemplate.delete(String.valueOf(userId));
    }
}
