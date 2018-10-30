package com.zhongchou.common.weixin.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.VerificationUtil;
import com.zhongchou.common.weixin.util.WeixinUtil;

@Controller
public class WeixinController extends BaseController {
	@Autowired
	private IUserService userService;
	Logger logger=Logger.getLogger(WeixinController.class);
	String appid = Config.getString("APPID");//公众号的唯一标识
	String secret = Config.getString("SECRET");//公众号的appsecret
	String weixinurl = Config.getString("WEIXINURL");//微信域名
	String privateKey = Config.getString("RSAPrivate");
	static String headpicUrl = Config.getString("HeadpicUrl");//头像保存路径
	String weixinToken = "yanshangshuangchuangweixintest";
	//微信接口配置
	@RequestMapping(value = "weixin/weixinCheck.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView weixinCheck(HttpServletRequest request,HttpServletResponse response,Model model){
		//Map out = new HashMap();
		logger.info("WeixinController.weixinCheck  start");
		String echostr = request.getParameter("echostr");
		PrintWriter out = null;
		try {
			if(!com.yanshang.util.StringUtils.isEmpty(echostr)){
				out = response.getWriter();
				if(CheckSignature(request)){
					logger.info("out   echostr:"+echostr);
					out.write(echostr);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				out.close();
			}
		}
		return null;
	}
	//获取js接口信息
	@RequestMapping(value = "weixin/weixinJSCheck.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView weixinJSCheck(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		Map reqMap = new HashMap();
		String ticket = "";
		reqMap.put("secret",secret);
		reqMap.put("grant_type","client_credential");
		reqMap.put("appid",appid);
		Map accessTokenMap;
		//获取当前的accessToken
		accessTokenMap = WeixinUtil.getAccessToken(reqMap);
		System.out.println("accessTokenMap:"+accessTokenMap);
		reqMap.put("access_token", accessTokenMap.get("accessToken"));
		reqMap.put("type", "jsapi");
		logger.info("access_token:"+reqMap.get("access_token"));
		//根据accessTokenMap获取jsapiTicket
		Map jsapiTicketMap = WeixinUtil.getjsapiTicket(reqMap);
		System.out.println("jsapiTicketMap:"+jsapiTicketMap);
		ticket = ConvUtils.convToString(jsapiTicketMap.get("ticket"));
		String appId = appid;
		String url = "&url="+request.getParameter("url").split("#")[0];//当前网页的URL
		String timestamp = String.valueOf(new Date().getTime());
		String timestampWX = "&timestamp="+timestamp;
		String nonceStr = UUID.randomUUID().toString();
		String nonceStrWX = "&noncestr="+nonceStr;
		String jsapi_ticket = "&jsapi_ticket="+ticket;
		String[] ArrTmp = { url,jsapi_ticket, timestampWX, nonceStrWX };
		Arrays.sort(ArrTmp);
		String signature = encode(StringUtils.join(ArrTmp).substring(1));
		System.out.println("ArrTmp:"+StringUtils.join(ArrTmp).substring(1));
		System.out.println("signature:"+signature);
		out.put("appId", appId);
		out.put("timestamp", timestamp);
		out.put("nonceStr", nonceStr);
		out.put("signature", signature);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}
	//获取openid
	@RequestMapping(value = "weixin/weixinIndex.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView weixinIndex(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
		Map reqMap = new HashMap();
		String openid = null;
		String code = request.getParameter("code");
		reqMap.put("appid", appid);
		reqMap.put("secret", secret);
		reqMap.put("code", code);
		reqMap.put("grant_type", "authorization_code");
		try {
			Map accessTokenMap = WeixinUtil.getOpenId(reqMap);
			openid = (String)accessTokenMap.get("openid");
			System.out.println("openid:"+openid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
		response.sendRedirect(weixinurl+"/dist/index.html#/index?openid="+openid);
		return null;
	}
	//保存头像
	@RequestMapping(value = "SCLogin/weixin/WXSaveHeadpic.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getWXMedia(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		Map reqMap = new HashMap();
		reqMap.put("secret",secret);
		reqMap.put("grant_type","client_credential");
		reqMap.put("appid",appid);
		UserDto user = getUser();
		String Randoms = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String picName = DateUtils.convertDate2String(new Date(), "yyyyMMdd")+VerificationUtil.generatingRandomNum(Randoms,10)+"-";
		String imgFilePath = headpicUrl+picName+".jpg";//新生成的图片
		String mediaId = request.getParameter("mediaId");
		//获取当前accessToken 
		Map accessTokenMap = WeixinUtil.getAccessToken(reqMap);
		String url = "https://api.weixin.qq.com/cgi-bin/media/get";
		url+="?access_token="+accessTokenMap.get("accessToken")+"&media_id="+mediaId;
		try {
			saveImageToDisk(url,imgFilePath);
		} catch (Exception e) {
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "下载失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(userService.updUserIconFileId(user.getOidUserId(),imgFilePath)){
        	user.setUserIconFileId(imgFilePath);
        	setUser(user);
        	out.put("path", imgFilePath);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "保存失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
	}
	private boolean CheckSignature(HttpServletRequest request)
    {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String[] ArrTmp = { weixinToken, timestamp, nonce };
		Arrays.sort(ArrTmp);
		String tmpStr = encode(StringUtils.join(ArrTmp));
        if (tmpStr.equals(signature))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
	/*public static String getSha1(String str){
        if(str==null||str.length()==0){
            return null;
        }
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9',
                'a','b','c','d','e','f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j*2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];      
            }
            return new String(buf);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
*/
	
	private String getFormattedText(byte[] bytes) {   
		final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };    
        int len = bytes.length;    
        StringBuilder buf = new StringBuilder(len * 2);    
        // 把密文转换成十六进制的字符串形式    
        for (int j = 0; j < len; j++) {    
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);    
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);    
        }    
        return buf.toString();    
    }    
    
    public String encode(String str) {    
        if (str == null) {    
            return null;    
        }    
        try {    
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");    
            messageDigest.update(str.getBytes());    
            return getFormattedText(messageDigest.digest());    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    } 
    
    /**
	    * 根据文件id下载文件
	    * 
	    * @param mediaId
	    *            媒体id
	    * @throws Exception
	    */
	   public  InputStream getInputStream(String url) {
	       InputStream is = null;
	       try {
	           URL urlGet = new URL(url);
	           HttpURLConnection http = (HttpURLConnection) urlGet
	                   .openConnection();
	           http.setRequestMethod("GET"); // 必须是get方式请求
	           http.setRequestProperty("Content-Type",
	                   "application/x-www-form-urlencoded");
	           http.setDoOutput(true);
	           http.setDoInput(true);
	           System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
	           System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
	           http.connect();
	           // 获取文件转化为byte流
	           is = http.getInputStream();
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	       return is;
	   }
	   /**
	    * 获取下载图片信息（jpg）
	    * 
	    * @param mediaId
	    *            文件的id
	    * @throws Exception
	    */
	   public void saveImageToDisk(String url,String imgFilePath) throws Exception {
		   logger.info("url:"+url);
		  
	       InputStream inputStream = getInputStream(url);
	       byte[] data = new byte[1024];
	       int len = 0;
	       FileOutputStream fileOutputStream = null;
	       try {
	           fileOutputStream = new FileOutputStream(imgFilePath);
	           while ((len = inputStream.read(data)) != -1) {
	               fileOutputStream.write(data, 0, len);
	           }
	       } catch (IOException e) {
	           e.printStackTrace();
	       } finally {
	           if (inputStream != null) {
	               try {
	                   inputStream.close();
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
	           }
	           if (fileOutputStream != null) {
	               try {
	                   fileOutputStream.close();
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
	           }
	       }
	   }
}
