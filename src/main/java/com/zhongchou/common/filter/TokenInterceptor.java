package com.zhongchou.common.filter;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhongchou.common.base.SubmitToken;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RedisUtil;
import com.zhongchou.common.util.SerializeUtil;
import com.zhongchou.common.util.UserTokenUtil;

public class TokenInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(SubmitToken.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            SubmitToken annotation = method.getAnnotation(SubmitToken.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                	if(UserTokenUtil.isApp(request)){
                		UserTokenUtil.setSessionObj("SubmitToken", UUID.randomUUID().toString(),request);
                	}else{
                		request.getSession(true).setAttribute("SubmitToken", UUID.randomUUID().toString());
                	}
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        LOG.warn("please don't repeat submit,url:"+ request.getServletPath());
                        Map out = new HashMap();
                        out.put(Constants.RET_CODE, "996");
            			out.put(Constants.RET_MSG, "重复提交");
            			JsonUtil.writeJson(response,out);
                        return false;
                    }
                    if(UserTokenUtil.isApp(request)){
                    	UserTokenUtil.clearSessionObj("SubmitToken",request);
                	}else{
                		request.getSession(true).removeAttribute("SubmitToken");
                	}
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
    	String serverToken =  null;
    	if(UserTokenUtil.isApp(request)){
    		serverToken = (String)UserTokenUtil.getSessionObj("SubmitToken",request);
    	}else{
    		serverToken = (String) request.getSession(true).getAttribute("SubmitToken");
    	}
        if (serverToken == null) {
            return true;
        }
        String clinetToken = request.getParameter("SUBMITTOKEN");
        if (clinetToken == null) {
            return true;
        }
        if (!serverToken.equals(clinetToken)) {
            return true;
        }
        return false;
    }
}