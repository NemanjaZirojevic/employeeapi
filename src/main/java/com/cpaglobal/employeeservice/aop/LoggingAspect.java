package com.cpaglobal.employeeservice.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author nemanja.zirojevic
 * This class is used as centralized logging configuration
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);


    @Pointcut(value = "execution(* com.cpaglobal.employeeservice.web.controllers.*.*(..))")
    public void getPointCut(){

    }


    @Before("getPointCut() && args(..,request)")
    public void logBefore(JoinPoint joinPoint, HttpServletRequest request) {

        log.info("Entering in Method :  " + joinPoint.getSignature().getName());
        log.info("Class Name :  " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Arguments :  " + Arrays.toString(joinPoint.getArgs()));
        log.info("Target class : " + joinPoint.getTarget().getClass().getName());

    }


    @AfterThrowing(pointcut = "getPointCut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.info("An exception has been thrown in " + joinPoint.getSignature().getName() + " ()");
        log.info("Cause : " + exception.getCause());
    }

    @Around("getPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            Object result = joinPoint.proceed();
            ObjectMapper objectMapper = new ObjectMapper();
            log.info(className + " : " + methodName + "()" + " executed with response : " + objectMapper.writeValueAsString(result));
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in "
                    + joinPoint.getSignature().getName() + "()");
            throw e;
        }
    }




}
