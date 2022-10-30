package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    public void lock(String accountNumber){
        
        // sampleLock이라는 이름의 lock을 획득
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try{
            // 1초동안 Lock 시도, leaseTime 뒤에 Lock 해제
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);

            // Lock 획득에 실패한 경우
            if(!isLock){
                log.error("====== Lock acquisition failed ========");
                throw new AccountException(ErrorCode.ACCOUNT_TRANSACTION_LOCK);
            }

        }
        catch(AccountException e){
            throw e;
        }
        catch(Exception e){
            log.error("Redis lock failed", e);
        }
    }

    public void unlock(String accountNumber){
        log.debug("Unlock for accountNumber : {}", accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }

    private String getLockKey(String accountNumber) {
        return "ACLK:" + accountNumber;
    }
}

