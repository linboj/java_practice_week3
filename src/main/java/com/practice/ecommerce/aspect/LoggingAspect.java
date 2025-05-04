package com.practice.ecommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.practice.ecommerce.service..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entering method: {} with arguments: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {} with result: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("Exception in method: {} with message: {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage(), ex);
    }

    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(); // continue method execution
        long duration = System.currentTimeMillis() - start;
        logger.info("Method {} executed in {} ms", joinPoint.getSignature().toShortString(), duration);
        return proceed;
    }
}
