package com.zhongchou.common.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;

import com.zhongchou.common.constant.Constants;

public class JsonUtil {
	static Logger logger=Logger.getLogger(JsonUtil.class);
	public static void writeJson(HttpServletResponse response,Map model) {
		PrintWriter out = null;
		System.out.println("code:"+model.get(Constants.RET_CODE));
		System.out.println("msg:"+model.get(Constants.RET_MSG));

		logger.info("code:"+model.get(Constants.RET_CODE));
		logger.info("msg:"+model.get(Constants.RET_MSG));
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		setEncoding(response);
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setContentType("text/html; charset=utf-8");
			/* response.setHeader("Access-Control-Allow-Method", "POST");*/
			out = response.getWriter();
            JSONObject responseJSONObject = JSONObject.fromObject(model,jsonConfig); //将实体对象转换为JSON Object转换
            out.print(responseJSONObject.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (Exception e2) {

			}
		}
	}
	public static void setEncoding(HttpServletResponse response) {
		 response.setCharacterEncoding("utf-8");
		 response.setContentType("text/html;charset=utf-8");
	}

}
