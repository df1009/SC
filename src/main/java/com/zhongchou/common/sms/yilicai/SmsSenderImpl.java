package com.zhongchou.common.sms.yilicai;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.sms.AbstractSmsSender;
import com.zhongchou.common.sms.SmsSender;
@Service
public class SmsSenderImpl extends AbstractSmsSender implements SmsSender {

	public static final String URL = "http://cf.51welink.com/submitdata/service.asmx/g_Submit";
	private static final org.apache.log4j.Logger LOGGER= org.apache.log4j.Logger.getLogger(SmsSenderImpl.class);

	/**
	 * 发送手机短信。
	 *
	 * @param SmsSendBean
	 *            发送短信信息对象
	 * @return 是否成功发送
	 */
	@Override
	public boolean send(SendSmsDto sendSms) {
		try {
			return super.send(sendSms);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	/**
	 * 发送手机语音。
	 *
	 * @param phoneNumber
	 *            电话号码
	 * @param content
	 *            短信内容
	 * @return 是否成功发送
	 */
	@Override
	protected boolean sendVoiceSms(String phone, String content) throws Exception {
		return true;
	}

	/**
	 * 发送手机短信。
	 *
	 * @param phoneNumber
	 *            电话号码
	 * @param content
	 *            短信内容
	 * @return 是否成功发送
	 */
	@Override
	protected boolean sendTextSms(String phone, String content) throws Exception {
		readContentFromPost(phone,content);
		return true;

		/*
		CloseableHttpClient client = null;
		HttpPost post = null;
		CloseableHttpResponse response = null;
		try {
			client = HttpClients.createDefault();
			post = new HttpPost(URL);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("username", "yanshang"));
	    	params.add(new BasicNameValuePair("password", "eXMxMjM0NTY="));
	    	params.add(new BasicNameValuePair("method", "sendSMS"));
	    	params.add(new BasicNameValuePair("smstype", "0"));
	    	params.add(new BasicNameValuePair("mobile", phone));
	    	params.add(new BasicNameValuePair("content", content));
	    	params.add(new BasicNameValuePair("extno", "506949469"));
	    	post.setEntity(new UrlEncodedFormEntity(params, "GBK"));

			response = client.execute(post);
			return EntityUtils.toString(response.getEntity()).startsWith("success");
		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			if (response != null) {
				response.close();
			}
			if (client != null) {
				client.close();
			}
		}
	*/}


	public static void readContentFromPost(String phone, String content) throws IOException {
		URL url = new URL(URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		String postData = "sname=dlyiyan0&spwd=YSyysc2017&scorpid=&sprdid=1012888&sdst="+phone+"&smsg="+URLEncoder.encode(content, "UTF-8");
		conn.setRequestProperty("Content-Length", "" + postData.length());
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
		out.write(postData);
		out.flush();
		out.close();

		//获取响应状态
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			System.out.println("connect failed!");
			LOGGER.info("connect failed!");
		}
		//获取响应内容体
		String line, result = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		while ((line = in.readLine()) != null) {
			result += line + "\n";
		}
		LOGGER.info(result);
		in.close();
	}
}
