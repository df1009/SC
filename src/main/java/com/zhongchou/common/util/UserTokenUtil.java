package com.zhongchou.common.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yanshang.config.Config;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;

public class UserTokenUtil {
	static Logger logger=Logger.getLogger(UserTokenUtil.class);
	static String privateKey = Config.getString("RSAPrivate");
	public static boolean isApp(HttpServletRequest request){
		String s1 = request.getHeader("user-agent");
		if(s1.contains("Android")) {
			//System.out.println("Android移动客户端");
			return true;
		} else if(s1.contains("iPhone")) {
			//System.out.println("iPhone移动客户端");
			return true;
		} else if(s1.contains("iPad")) {
			//System.out.println("iPad客户端");
			return true;
		}  else {
			if(s1.contains("UNAVAILABLE")){//测试
				return true;
			}
			//System.out.println("其他客户端");
			return false;
		}
	}
	public static String getUserId(HttpServletRequest request) {
		//getRequest().getCookies().g
		String tokenId = request.getParameter("TOKENID");
		String IMEI = request.getParameter("IMEIID");
		try {
			if(!StringUtils.isEmpty(IMEI)){
				IMEI = RSAUtil.decryptByPrivate(privateKey,IMEI);
			}
			if(!StringUtils.isEmpty(tokenId)){
				tokenId = RSAUtil.decryptByPrivate(privateKey,tokenId);
			}
		} catch (Exception e) {
			logger.error("TOKENID  or IMEIID解密有误");
			return null;
		}
		String userId = null;
		String uri = request.getRequestURI();
        // uri中包含SCLogin时才进行过滤
        if (uri.indexOf("/SCLogin/") != -1) {//登录内取userId
        	if(!StringUtils.isEmpty(tokenId)
        			&&!StringUtils.isEmpty(IMEI)){
        		if(IMEI.equals(tokenId.split("\\+")[1])){
        			userId = tokenId.split("\\+")[0];
        		}
        	}
        }else{//登录外取IMEI号
        	if(!StringUtils.isEmpty(IMEI)){
        		userId = IMEI;
        	}
        }
		return userId;
	}

	public static void setUser(UserDto user,HttpServletRequest request) {
		if(isApp(request)){
			RedisUtil.set(user.getOidUserId()+Constants.SESSION_USER_INFO, SerializeUtil.serializeString(user),60*60*24);
		}else{
			request.getSession().setAttribute(Constants.SESSION_USER_INFO, user);
		}
	}

	public static UserDto getUser(HttpServletRequest request) {
		UserDto userDto = null;
		if(isApp(request)){
			userDto = (UserDto)SerializeUtil.unserializeString(RedisUtil.get(getUserId(request)+Constants.SESSION_USER_INFO));
		}else{
			userDto = (UserDto) request.getSession().getAttribute(Constants.SESSION_USER_INFO);
		}
		 return userDto;
	}

	public static void clearUserInfo(HttpServletRequest request) {
		if(isApp(request)){
			RedisUtil.del(getUserId(request)+Constants.SESSION_USER_INFO);
		}else{
			request.getSession().removeAttribute(Constants.SESSION_USER_INFO);
		}
	}

	public static void setSessionObj(String key,Object obj,HttpServletRequest request) {
		if(isApp(request)){
			RedisUtil.set(getUserId(request)+key, SerializeUtil.serializeString(obj),60*60*24);
		}else{
			request.getSession().setAttribute(key, obj);
		}
	}

	public static Object getSessionObj(String key,HttpServletRequest request) {
		if(isApp(request)){
			return SerializeUtil.unserializeString(RedisUtil.get(getUserId(request)+key));
		}else{
			return request.getSession().getAttribute(key);
		}
	}

	public static void clearSessionObj(String key,HttpServletRequest request) {
		if(isApp(request)){
			RedisUtil.del(getUserId(request)+key);
		}else{
			request.getSession().removeAttribute(key);
		}
	}

}
