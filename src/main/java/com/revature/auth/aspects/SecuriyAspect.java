package com.revature.auth.aspects;

import com.revature.auth.exceptions.UnauthorizedException;
import com.revature.auth.services.UserService;
import com.revature.auth.utils.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Aspect
public class SecuriyAspect {

    @Autowired
    UserService us;

    @Around("authorizeAdmin()")
    public Object authenticate(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String auth = request.getHeader("Authorization");
        System.out.println(auth);
        if (auth == null){
            throw new UnauthorizedException("Please check if logged in.");
        }

        String auth_admin = JwtUtil.isValidJWT(auth).getClaim("role").toString();
        auth_admin = auth_admin.replace("\"", "");

        System.out.println(auth_admin);

        if (auth_admin == "admin"){
            Object obj = pjp.proceed();
            return obj;
        }

        throw new UnauthorizedException("Please check if logged in.");

    }

    @Pointcut("@annotation(com.revature.auth.aspects.Authorized)")
    private void authorizeAdmin(){ }

}
