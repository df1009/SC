package com.zhongchou.common.log;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.LogDto;
import com.zhongchou.common.dto.UserDto;

@Aspect
public class LogAop {
    //注入service,用来将日志信息保存在数据库
   /* @Resource(name="logService")
    private LogServiceImpl logservice;*/

     //配置接入点,如果不知道怎么配置,可以百度一下规则
     @Pointcut("execution(* com.licaishi.commom.service..*.*(..))")
     private void controllerAspect(){}//定义一个切入点

     @Around("controllerAspect()")
     public Object around(ProceedingJoinPoint pjp) throws Throwable {
    	 System.out.println("日志切面");
         //常见日志实体对象
    	 LogDto log = new LogDto();
         //获取登录用户账户
         HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
         UserDto user = (UserDto) request.getSession().getAttribute(Constants.SESSION_USER_INFO);
         if(user == null){
        	 log.setOidUserId("00000");
         }else{
        	 log.setOidUserId(user.getOidUserId());
         }
         //获取系统时间
         String time = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date());
         log.setStartData(time);

         //获取系统ip
         String ip = getRemoteAddr(request);
         log.setIpAddress(ip);

        //方法通知前获取时间,为什么要记录这个时间呢？当然是用来计算模块执行时间的
         long start = System.currentTimeMillis();
        // 拦截的实体类，就是当前正在执行的controller
        Object target = pjp.getTarget();
        // 拦截的方法名称。当前正在执行的方法
        String methodName = pjp.getSignature().getName();
        // 拦截的方法参数
        Object[] args = pjp.getArgs();
        // 拦截的放参数类型
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Class[] parameterTypes = msig.getMethod().getParameterTypes();

        Object object = null;
        // 获得被拦截的方法
        Method method = null;
        try {
            method = target.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (null != method) {
            // 判断是否包含自定义的注解，说明一下这里的SystemLog就是我自己自定义的注解
            if (method.isAnnotationPresent(SystemLog.class)) {
                SystemLog systemlog = method.getAnnotation(SystemLog.class);
                log.setModule(systemlog.module());
                log.setMethod(systemlog.methods());
                try {
                    object = pjp.proceed();
                    long end = System.currentTimeMillis();
                    log.setEnData(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
                    //将计算好的时间保存在实体中
                    log.setReponesData(""+(end-start));
                    log.setCommite("执行成功！");
                    //保存进数据库
                    //logservice.saveLog(log);
                    System.out.println("日志切面："+log.getMethod()+log.getReponesData());
                } catch (Throwable e) {
                    // TODO Auto-generated catch block
                    long end = System.currentTimeMillis();
                    log.setReponesData(""+(end-start));
                    log.setCommite("执行失败");
                    System.out.println("日志切面："+log.getMethod()+log.getReponesData());
                    //logservice.saveLog(log);
                }
            } else {//没有包含注解
                object = pjp.proceed();
            }
        } else { //不需要拦截直接执行
            object = pjp.proceed();
        }
        return object;
     }

     protected String getRemoteAddr(HttpServletRequest request) {
 		String ip = request.getHeader("x-forwarded-for");
 		if(ip != null && ip.length() != 0 && !"unKnown".equalsIgnoreCase(ip)){
 			//多次反向代理后会有多个ip值，第一个ip才是真实ip
 			return ip.split(",")[0];
 		}
 		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
 			ip = request.getHeader("Proxy-Client-IP");
 		}
 		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
 			ip = request.getHeader("WL-Proxy-Client-IP");
 		}
 		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
 			ip = request.getRemoteAddr();
 		}
 		if (ip != null && ip.indexOf(":") >= 0)
 		{ // 判断是否为IPV6地址
 			ip = "127.0.0.1";
 		}
 		return ip;
 	}
}

