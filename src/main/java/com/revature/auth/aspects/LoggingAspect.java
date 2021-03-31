package com.revature.auth.aspects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

    private static Logger logger = Logger.getLogger(LoggingAspect.class);

    @Before("logPointCut()")
    public void logRequest(JoinPoint joinPoint){
        logger.info("Attempting incoming request: "+joinPoint.toString());
    }

    @After("logPointCut()")
    public void logAfterRequest(JoinPoint joinPoint){
        logger.info("Completed request: "+joinPoint.toString());
    }

    @AfterThrowing("logPointCut()")
    public void logAfterExceptionRequest(JoinPoint joinPoint){
        logger.error("Exception thrown in method: "+joinPoint.toString());
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    private void logPointCut(){}

}
