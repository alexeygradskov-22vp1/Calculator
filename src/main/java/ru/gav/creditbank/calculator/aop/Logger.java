package ru.gav.creditbank.calculator.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class Logger {


    @Pointcut("execution(public  * ru.gav.creditbank.calculator.service.CalculatorService.*(..))")
    private void callAtCalculateService(){}

    @Before("callAtCalculateService()")
    private void beforeCallMethods(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Arrays.stream(joinPoint.getArgs()).forEach(x->log.info("Входные данные в метод {}:{}",methodName,x.toString()));
    }

    @AfterReturning(value = "callAtCalculateService()", returning = "returned")
    private void afterCallMethods(JoinPoint joinPoint, Object returned){
        String methodName = joinPoint.getSignature().getName();
       log.info("Выходные данные из метода {}:{}",methodName,returned);
    }

}
