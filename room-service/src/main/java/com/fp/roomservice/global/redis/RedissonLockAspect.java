package com.fp.roomservice.global.redis;

import com.fp.roomservice.global.exception.GlobalException;
import com.fp.roomservice.global.exception.type.RedisErrorType;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RedissonLockAspect {

    @Autowired
    RedissonClient redissonClient;

    @Around("@annotation(redissonLock)")
    public Object redissonLock(ProceedingJoinPoint joinPoint, RedissonLock redissonLock)
        throws Throwable {
        String lockKey = generateLockKey(joinPoint, redissonLock.key());
        long waitTime = redissonLock.waitTime();
        long leaseTime = redissonLock.leaseTime();
        TimeUnit timeUnit = redissonLock.timeUnit();

        boolean isLocked = false;
        RLock lock = null;
        try {
            lock = redissonClient.getLock(lockKey);
            isLocked = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (isLocked) {
                log.info("Successfully lock : {}", lockKey);
                return joinPoint.proceed();
            } else {
                log.warn("Failed lock: {}", lockKey);
                throw new GlobalException(RedisErrorType.KEY_NOT_GAIN);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GlobalException(RedisErrorType.KEY_INTERRUPTED);
        } catch (RedisException e) {
            throw new GlobalException(RedisErrorType.REDIS_ERROR);
        } finally {
            if (isLocked) {
                try {
                    lock.unlock();
                    log.info("Successfully unlock: {}", lockKey);
                } catch (RedisException e) {
                    log.error("Failed unlock: {}", lockKey, e);
                    throw new GlobalException(RedisErrorType.REDIS_ERROR);
                }
            }
        }
    }

    public String generateLockKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        MethodSignature nextSignature = (MethodSignature) joinPoint.getSignature();
        nextSignature.getMethod();
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        String[] parameterNames = nextSignature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(keyExpression).getValue(context, String.class);

    }
}
