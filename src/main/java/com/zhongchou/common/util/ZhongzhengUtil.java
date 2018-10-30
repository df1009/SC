package com.zhongchou.common.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.yanshang.config.Config;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.zhongzheng.cipher.Des3;
import com.zhongchou.common.zhongzheng.cipher.MD5;
import com.zhongchou.common.zhongzheng.cipher.Rsa;
import com.zhongchou.common.zhongzheng.util.HttpUtil;

public class ZhongzhengUtil {
	static String ZZChannelNo = Config.getString("ZZChannelNo");
	static String zzurl = Config.getString("zzurl");
	static String zzpublicKey = Config.getString("ZZpublicKey");
	static String ZZprivateKey = Config.getString("ZZprivateKey");


	static Logger logger=Logger.getLogger(ZhongzhengUtil.class);
	//渠道号
	static String channelNo = ZZChannelNo;
	//加密的3deskey
	static String key = null;
	static SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
	//中证提供的rsa加密公钥
	static String publicKey = zzpublicKey;
	//static String publicKey = Config.getString("RSAPrivate");
	//中证http请求url
	static String url = zzurl;
	//rsa私钥 解密由中证的发送的数据
	static String privateKey = ZZprivateKey;

	//时间戳
	public static String geTimeStamp(){
		Random rand = new Random();
		String strRand="";
		for (int i = 0; i < 4; i++) {
			strRand+=rand.nextInt(10);
		}
		String date = df.format(new Date());
		return date+String.valueOf(new Date().getTime())+strRand;
    }

	//动态获取3des秘钥-初始化
	public static String get3desKey(){
		key = ThreeDesUtil.getKey("yilicai");
		return key;
    }

	//rsa加密后的3des秘钥
	public static String encrypt3desKey() throws UnsupportedEncodingException{
		if(!StringUtils.isEmpty(key)){
			return RSAUtil.encryptionByPublic(publicKey,key);
		}else{
			throw new UnsupportedEncodingException("先初始化3des公钥");
		}
    }

	//rsa公钥加密
	public static String publicEncrypByRSA(String content) throws UnsupportedEncodingException {
		return RSAUtil.encryptionByPublic(publicKey, content);
    }

	//http请求
	public static String sendPost(Map sendMap) throws Exception {
		//content=channelNo+geTimeStamp()+key;
		return HttpUtil.sendByPost(url, sendMap);
    }

	//初始化接收到的json流 解密出encrypt_data
	public static String receivEncryptData(String content) throws Exception {
		logger.info("ZhongzhengUtil initReceivEncryptData HTTPResponse :\r\n"+Log4jUtil.output(content));
		JSONObject obj = JSONObject.parseObject(content);
		String encryptKey = obj.get("encrypt_key").toString();//加密密钥    随机生成的24个字节(字母或数字组合)的3des密钥；通过渠道上传的rsa公钥加密
		String encryptata = obj.get("encrypt_data").toString();//加密数据  随机生成的3des密钥对业务功能参数加密后的密文；

		//rsa解密出3des秘钥
		String desKey = Rsa.decrypt(privateKey,encryptKey);
		//3des解密
		String encryptData3des = Des3.decrypt(Des3.getSecretKey(desKey), encryptata);
		return encryptData3des;
	}

	public static String encryption3Des(Map scMap) throws Exception {
		String sendData = JSONObject.toJSONString(scMap);
		//生成一个3des秘钥
		String desKey = get3desKey();
		//获取时间戳
		String timestamp = geTimeStamp();
		String encrypt_key = Rsa.encrypt(publicKey,desKey);
		//3des加密
		String encodeSendData = Des3.encrypt(Des3.getSecretKey(desKey), sendData);
		Map sendMap = new HashMap();
		sendMap.put("channel_no", channelNo);//渠道编号
		sendMap.put("timestamp", timestamp);//时间戳
		sendMap.put("encrypt_key", encrypt_key);//加密密钥  随机生成的24个字节(字母或数字组合)的3des密钥；通过中证云分配的rsa公钥加密
		sendMap.put("encrypt_data", encodeSendData);//加密业务参数数据  随机生成的3des密钥对业务功能参数加密后的密文
		sendMap.put("mac", Rsa.sign(privateKey,MD5.encode( channelNo + timestamp
				+ encrypt_key + encodeSendData)));//签名  自有rsa私钥加密由参数组合(channel_no+timestamp+encrypt_key+ encrypt_data)的字符串密文
		String sendDataPost = JSONObject.toJSONString(sendMap);
		Map<String, String> paramsmap = new HashMap<>();
		paramsmap.put("json", sendDataPost);
		logger.info("ZhongzhengUtil encryption3Des HTTPRequest :\r\n"+Log4jUtil.output(sendDataPost.toString()));
		//return "sendPost(sendDataPost.toString())";
		return sendPost(paramsmap);
	}

}
