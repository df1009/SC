package com.zhongchou.common.login.controller.personalCenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.SpringLayout.Constraints;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.file.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class UploadMediaController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadMediaController.class);
	private static final String MEDIA_TYPE = ".mp4";
	private static final int MEDIASIZE = 52428800;
	private static final String PASH = "D:\\var";
	/**
	 * 上传视频
	 * @param mediaFile
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/SCLogin/uploadMedia.do",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView uploadMedia(@RequestParam("mediaFile") MultipartFile mediaFile,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> out = new HashMap<String,Object>();
		UserDto user = getUser();
		String loginId = user.getOidUserId();
		Boolean isMedia = false;//判断后缀格式是否为图片格式
		if(StringUtils.endsWithIgnoreCase(mediaFile.getOriginalFilename(), MEDIA_TYPE)){
			isMedia = true;
		}
		if(!isMedia){
			LOGGER.info("---------上传的文件类型不是MP4格式------------");
			return outParam(response, "","001","上传的文件类型只能是MP4格式");
		}
		if(mediaFile.getSize()>MEDIASIZE){
			LOGGER.info("---------上传的视频不能大于50M------------");
			return outParam(response, "","002","上传的视频不能大于50M");
		}
		String fileName = mediaFile.getOriginalFilename();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String newPath = PASH+File.separator+sdf.format(new Date()).toString()+fileName;
		File newFile = new File(newPath);
		try {
			mediaFile.transferTo(newFile);
			LOGGER.info("---------上传的视频成功------------");
			return outParam(response,newPath, Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);

		} catch (Exception e) {
			LOGGER.error("---------上传视频异常------------");
			return outParam(response, "","003","上传视频异常");
		}

	}
	private ModelAndView outParam(HttpServletResponse response,
			String imagePath, String code,String msg) {
		Map<String,Object> out = new HashMap<String,Object>();
		out.put("imagePath", imagePath);
		out.put(Constants.RET_CODE, code);
		out.put(Constants.RET_MSG, msg);
		JsonUtil.writeJson(response, out);
		return null;
	}
}
