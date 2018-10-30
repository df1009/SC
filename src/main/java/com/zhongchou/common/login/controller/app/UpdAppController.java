package com.zhongchou.common.login.controller.app;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.service.IHelpCenterService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class UpdAppController extends BaseController {
	@Autowired
	private IHelpCenterService helpCenterService;

	String filePath = Config.getString("appFileUrl");
	String registUrl = Config.getString("registUrl");

	/**
	 *查询app版本号
	 */
	@RequestMapping(value="/loginOut/UpdApp.do",method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView UpdApp(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> out = new HashMap<>();
		out = helpCenterService.getDownloadApp();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response, out);
		return null;
	}

	 private String getFile(String path){
	        // get file list where the path has
		 try {
				File file = new File(path);
		        // get the folder list
		        File[] array = file.listFiles();
		        String[] fileName = new String[array.length];
		        int count = 0;
		        String name = null;
		        for(int i=0;i<array.length;i++){
		            if(array[i].isFile()){
		            	fileName[count++] = array[i].getName();
		            }/*else if(array[i].isDirectory()){
		                getFile(array[i].getPath());
		            }*/
		        }
		       for (int i = 0; i < count-1; i++) {
		    	   if(StringUtil.compareVersion(fileName[i].split("-")[1].split("\\.apk")[0], fileName[i+1].split("-")[1].split("\\.apk")[0])>0){
		    		   String now = fileName[i];
		    		   fileName[i] = fileName[i+1];
		    		   fileName[i+1] = now;
		    	   }
				}
		       return fileName[count-1];
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	    }
}
