package com.zhongchou.common.filter;


import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import com.yanshang.config.Config;
import com.yanshang.util.DateUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.RedisUtil;
import com.zhongchou.common.util.SerializeUtil;
import com.zhongchou.common.util.UserTokenUtil;

/**
 * 登录过滤
 *
 * @date 2017-3-20
 */
public class SessionFilter extends OncePerRequestFilter {
	Logger logger=Logger.getLogger(SessionFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	Map out = new HashMap();
    	//System.out.println(DateUtils.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss:SSS"));
    	logger.info(DateUtils.convertDate2String(new Date(),"yyyy-MM-dd HH:mm:ss:SSS"));
    	//System.out.println("sessionId:"+request.getSession().getId());
    	logger.info("sessionId:"+request.getSession().getId());
    	//System.out.println("ip:"+request.getRemoteAddr()+"   请求链接："+request.getRequestURI());
    	logger.info("ip:"+request.getRemoteAddr()+"   请求链接："+request.getRequestURI());
    	String ip = request.getHeader("x-forwarded-for");
		//System.out.println(ip);
		logger.info(ip);
		Enumeration enu=request.getParameterNames();
		while(enu.hasMoreElements()){
			String paraName=(String)enu.nextElement();
			if(!"file".equals(paraName)){
				System.out.println(paraName+": "+request.getParameter(paraName));
				logger.info(paraName+": "+request.getParameter(paraName));
			}
		}
    	String s1 = request.getHeader("user-agent");
    	//System.out.println("user:"+s1);
    	//System.out.println("请求方式:"+request.getMethod());

    	logger.info("user:"+s1);
    	logger.info("请求方式:"+request.getMethod());
    	// 不过滤的uri
        //String[] notFilter = new String[] { "SCLogin/initIndex.do"};
    	String[] notFilter = new String[] { };
        // 请求的uri
        String uri = request.getRequestURI();
        boolean flag = true;
     	String privateKey = Config.getString("RSAPrivate");
    	String tokenId = request.getParameter("TOKENID");
		String IMEI = request.getParameter("IMEIID");
		try {
			if(!StringUtils.isEmpty(tokenId)){
				tokenId = RSAUtil.decryptByPrivate(privateKey,tokenId);
			}
			if(!StringUtils.isEmpty(IMEI)){
				IMEI = RSAUtil.decryptByPrivate(privateKey,IMEI);
			}
		} catch (Exception e) {
			flag = false;
			out.put(Constants.RET_CODE, "991");
			out.put(Constants.RET_MSG, "token or imei is error");
			JsonUtil.writeJson(response,out);
		}
		if(flag){//TOKENID和IMEIID合法
			boolean mobileFlag = UserTokenUtil.isApp(request);
	        // uri中包含SCLogin时才进行过滤
	        if (uri.indexOf("/SCLogin/") != -1) {
	            // 是否过滤
	            boolean doFilter = true;
	            for (String s : notFilter) {
	                if (uri.indexOf(s) != -1) {
	                    // 如果uri中包含不过滤的uri，则不进行过滤
	                    doFilter = false;
	                    break;
	                }
	            }
	            if (doFilter) {
	            	UserDto user =null;
	                // 执行过滤
	        		if(mobileFlag){//手机端
	        			if(StringUtils.isEmpty(tokenId)
	        					||StringUtils.isEmpty(IMEI)){//参数异常
	        				out.put(Constants.RET_CODE, "990");
	            			out.put(Constants.RET_MSG, "token or imei is error");
	            			JsonUtil.writeJson(response,out);
	        			}else{
	        				String userId= null;
        	        		if(IMEI.equals(tokenId.split("\\+")[1])){
        	        			userId = tokenId.split("\\+")[0];
        	        		}
	        				Object obj = SerializeUtil.unserializeString(RedisUtil.get(userId+Constants.SESSION_USER_INFO));
	        				if(obj==null){
	        					out.put(Constants.RET_CODE, "999");
		            			out.put(Constants.RET_MSG, "未登录");
		            			JsonUtil.writeJson(response,out);
	        				}else{
	        					filterChain.doFilter(request, response);
	        				}
	        			}
	        		}else{//登录内PC端
	        			user = (UserDto)request.getSession().getAttribute(Constants.SESSION_USER_INFO);
	        			if (null == user) {
	                    	out.put(Constants.RET_CODE, "999");
	            			out.put(Constants.RET_MSG, "未登录");
	            			JsonUtil.writeJson(response,out);
	                    }else{
	    	        		// 如果session中存在登录者实体，则继续
	    	        		filterChain.doFilter(request, response);
	                    }
	        		}
	            }
	        } else {
	        	if(mobileFlag){//手机端
	    			if(StringUtils.isEmpty(IMEI)){
	    				out.put(Constants.RET_CODE, "990");
	        			out.put(Constants.RET_MSG, "imei is error");
	        			JsonUtil.writeJson(response,out);
	    			}else{
	    				filterChain.doFilter(request, response);
	    			}
	    		}else{
	    			// 如果uri中不包含SCLogin，则继续
	                filterChain.doFilter(request, response);
	    		}
	        }
		}
    }
}