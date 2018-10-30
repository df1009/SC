package com.zhongchou.common.login.controller.personalCenter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.UploadImageUtil;

@Controller
public class UploadImageController extends BaseController{
	@Autowired
	private IMyProjectService myProjectService;
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadImageController.class);
	/**
	 * 上传头像图片
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/SCLogin/uploadHeadImage.do",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView uploadImage(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> out = new HashMap<String,Object>();

			UserDto user = getUser();
			String loginId = user.getOidUserId();
			String imagePath = request.getParameter("file");

			if(StringUtils.isEmpty(imagePath)){
				out.put("imagePath", "");
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "图片路径为空");
				JsonUtil.writeJson(response, out);
				LOGGER.info("-------------图片路径为空------------");
				return null;
			}
			try {
				myProjectService.updateHeadImage(loginId, imagePath);
				out.put("imagePath", imagePath);
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response, out);
				LOGGER.info("-------------保存图片路径成功------------");
				return null;

			} catch (Exception e) {
				LOGGER.error("-------------保存图片路径出错------------");
				out.put("imagePath", "");
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "保存图片路径出错");
				JsonUtil.writeJson(response, out);
				return null;
			}
	}
}
