package com.zhongchou.common.weixin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.util.RedisUtil;
import com.zhongchou.common.util.SerializeUtil;
import com.zhongchou.common.zhongzheng.util.HttpUtil;
 
/**
 * ClassName: OauthCode_GetUseInfo
 * @Description: 用户oauth2.0授权登录 通过code获取用户真实信息
 */
public class WeixinUtil {
	static Logger logger=Logger.getLogger(WeixinUtil.class);
	
	//获取openid
	public static Map getOpenId(Map reqMap) throws Exception{
		logger.info("获取openId   开始-------------------");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String openId = ConvUtils.convToString(reqMap.get("openId"));
		Map retMap = new HashMap();
		String resMsg = HttpUtil.sendByPost(url, reqMap);
		JSONObject obj = JSONObject.fromObject(resMsg);
		retMap.put("openid", obj.get("openid"));
		logger.info("获取openId   结束-------------------");
		return retMap;
	}
	//获取access_token
	public static Map getAccessToken(Map reqMap) throws Exception{
		logger.info("获取accessToken   开始-------------------");
		String url = "https://api.weixin.qq.com/cgi-bin/token";
		//String openId = ConvUtils.convToString(reqMap.get("openId"));
		Map retMap = (Map) SerializeUtil.unserializeString(RedisUtil.get("WXaccessToken"));
		//logger.info("openId:"+openId);
		logger.info("retMap:"+retMap);
		if(/*StringUtils.isEmpty(openId)||*/retMap==null){
			logger.info("第一次进入公众号获取accessToken");
			retMap = new HashMap();
			String resMsg = HttpUtil.sendByPost(url, reqMap);
			JSONObject obj = JSONObject.fromObject(resMsg);
			retMap.put("accessToken", obj.get("access_token"));
			retMap.put("expiresIn", obj.get("expires_in"));
			retMap.put("accessTokenDate", new Date().getTime());
			RedisUtil.set("WXaccessToken", SerializeUtil.serializeString(retMap),60*60*24);
		}else if(StringUtils.isEmpty(ConvUtils.convToString(retMap.get("accessToken")))){
			logger.info("缓存accessToken 已失效，重新获取");
			RedisUtil.del("WXaccessToken");
			retMap = getAccessToken(retMap);
			RedisUtil.set("WXaccessToken", SerializeUtil.serializeString(retMap),60*60*24);

		}else{
			long accessTokenDate = ConvUtils.convToLong(retMap.get("accessTokenDate"));//产生accessToken的时间
			if((new Date().getTime()-accessTokenDate)>7100000){//accessToken过期
				logger.info("accessToken 已过期，重新获取");
				RedisUtil.del("WXaccessToken");
				retMap = getAccessToken(retMap);
				RedisUtil.set("WXaccessToken", SerializeUtil.serializeString(retMap),60*60*24);
			}
		}
		logger.info("获取accessToken  结束-------------------");
		logger.info("accessToken："+retMap.get("accessToken"));
		return retMap;
	}
	//刷新openId
/*	public static Map refreshAccessToken(Map reqMap) throws Exception{
		logger.info("刷新accessToken   开始-------------------");
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
		Map retMap = new HashMap();
		String resMsg = HttpUtil.sendByPost(url, reqMap);
		JSONObject obj = JSONObject.fromObject(resMsg);
		retMap.put("openid", obj.get("openid"));
		retMap.put("expiresIn", obj.get("expires_in"));
		retMap.put("refreshToken", obj.get("refresh_token"));
		retMap.put("scope", obj.get("scope"));
		retMap.put("accessToken", obj.get("access_token"));
		retMap.put("accessTokenDate", new Date().getTime());
		RedisUtil.set(obj.get("openid")+"accessToken", SerializeUtil.serializeString(retMap),60*60*24);
		logger.info("刷新accessToken   结束-------------------");
		return retMap;
	}*/
	//获取jsapi_ticket
	public static Map getjsapiTicket(Map reqMap) throws Exception{
		logger.info("获取jsapi_ticket   开始-------------------");
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
		Map retMap = (Map) SerializeUtil.unserializeString(RedisUtil.get("WXjsapiTicket"));
		logger.info("当前的retMap:"+retMap);
		if(retMap==null){
			logger.info("第一次获取jsapi_ticket");
			retMap = new HashMap();
			String resMsg = HttpUtil.sendByPost(url, reqMap);
			JSONObject obj = JSONObject.fromObject(resMsg);
			logger.info("ticket obj:"+obj);
			retMap.put("ticket", obj.get("ticket"));
			retMap.put("jsapiTicketDate", new Date().getTime());
			RedisUtil.set("WXjsapiTicket", SerializeUtil.serializeString(retMap),60*60*24);
		}else{
			long accessTokenDate = ConvUtils.convToLong(retMap.get("jsapiTicketDate"));//产生jsapiTicket的时间
			if((new Date().getTime()-accessTokenDate)>7100000){//jsapiTicket过期
				logger.info("jsapiTicket 已过期，重新获取");
				RedisUtil.del("WXjsapiTicket");
				retMap = getjsapiTicket(reqMap);
				RedisUtil.set("WXjsapiTicket", SerializeUtil.serializeString(retMap),60*60*24);
			}
		}
		logger.info("获取jsapi_ticket   结束-------------------");
		return retMap;
	}
	/*//获取微信零时素材
	public static Map getWXMedia(Map reqMap) throws Exception{
		logger.info("获取video_url   开始-------------------");
		String url = "https://api.weixin.qq.com/cgi-bin/media/get";
		url+="?access_token="+reqMap.get("access_token")+"&media_id="+reqMap.get("media_id");
		Map retMap = new HashMap();
		//String resMsg = HttpUtil.sendByPost(url, reqMap);
		//JSONObject obj = JSONObject.fromObject(resMsg);
		//logger.info("ticket obj:"+obj);
		//retMap.put("videoUrl", obj.get("video_url"));
		logger.info("获取video_url   结束-------------------");
		return retMap;
	}*/
}
