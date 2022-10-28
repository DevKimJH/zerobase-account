package com.example.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisTestService {
    private final RedissonClient redissonClient;

    public String getLock(){
        
        // sampleLock이라는 이름의 lock을 획득
        RLock lock = redissonClient.getLock("sampleLock");

        try{
            // 1초동안 Lock 시도, leaseTime 뒤에 Lock 해제
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS);

            // Lock 획득에 실패한 경우
            if(!isLock){
                log.error("====== Lock acquisition failed ========");
                return "Lock failed";
            }

        }
        catch(Exception e){
            log.error("Redis lock failed");
        }

        return "Lock success";
    }
}
