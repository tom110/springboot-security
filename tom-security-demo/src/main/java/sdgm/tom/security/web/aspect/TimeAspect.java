package sdgm.tom.security.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

//切片可以拦截到处理方法的参数，但是拿不到request和response
@Aspect
@Component
public class TimeAspect {
    @Around("execution (* sdgm.tom.security.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("time aspect start");
        Object[] args=pjp.getArgs();
        for(Object arg : args){
            System.out.println("arg is "+arg);
        }
        Long start =new Date().getTime();
        Object object=pjp.proceed();
        System.out.println("time aspect 耗时："+(new Date().getTime()-start));
        System.out.println("time aspect finish");
        return object;
    }
}
