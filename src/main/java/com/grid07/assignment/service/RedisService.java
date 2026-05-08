package com.grid07.assignment.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    
    private final StringRedisTemplate stringRedisTemplate;


    public RedisService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //virality score
    public void increaseViralityScore(Long postId,int points){
       
       //key:post:1:virality_score+=20
        String key = "post:" + postId +":virality_score";
        stringRedisTemplate.opsForValue().increment(key,points);
    }

    //Bot count 
    public Long increaseBotCount(Long postId){
        //key:post:1:bot_count+=1 Redis does this atomically.
        String key = "post:" + postId +":bot_count";
        return stringRedisTemplate.opsForValue().increment(key);
    }


    //get Bot count
    public Long getBotCount(Long postId){
        String key = "post:" + postId +":bot_count";
        String botCountStr = stringRedisTemplate.opsForValue().get(key);
        return botCountStr != null ? Long.parseLong(botCountStr) : 0L;
    }

    
    public boolean hasCooldown(Long botId,Long humanId){
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        Boolean exists = stringRedisTemplate.hasKey(key);
        return exists != null && exists;
    }


    public void createCooldown(Long botId,Long humanId){
        String key = "cooldown:bot_" + botId + ":human_" + humanId;
        stringRedisTemplate.opsForValue()
        .set(key,"active",Duration.ofMinutes(10));
    }
}
