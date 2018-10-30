package com.zhongchou.common.zhongzheng.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpUtil {
	private static final int TIMEOUT=60000;
	private static final String CHARSET="UTF-8";
	
	/**
	 * get请求
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String sendByGet(String url) throws Exception{
		String result="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT)
                    .setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT)
                    .build();
            httpget.setConfig(requestConfig);
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            result = httpclient.execute(httpget, responseHandler);
        } catch(Exception e){
        	if(e.getMessage().indexOf("connect timed out")!=-1
        			||e.getMessage().indexOf("Connection refused")!=-1
        			||e.getMessage().indexOf("Read timed out")!=-1){
        		throw new TimeoutException();
        	}
        } finally {
            httpclient.close();
        }
        return result;
	}
	/**
	 * post请求
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String sendByPost(String url,Map<String,String> paramsmap) throws Exception{
		String result="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(TIMEOUT)
                    .setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT)
                    .build();
            httppost.setConfig(requestConfig);
            if(paramsmap!=null&&paramsmap.size()>0){
            	List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            	Iterator<String> it=paramsmap.keySet().iterator();
            	while(it.hasNext()){
            		String key=it.next();
            		 nvps.add(new BasicNameValuePair(key, paramsmap.get(key)));
            	}
              httppost.setEntity(new UrlEncodedFormEntity(nvps));
            }
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity,CHARSET) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            result = httpclient.execute(httppost, responseHandler);
        } catch(Exception e){
        	e.printStackTrace();
        	if(e.getMessage().indexOf("connect timed out")!=-1
        			||e.getMessage().indexOf("Connection refused")!=-1
        			||e.getMessage().indexOf("Read timed out")!=-1){
        		throw new TimeoutException();
        	}else{
        		throw e;
        	}
        } finally {
            httpclient.close();
        }
        return result;
	}
}
