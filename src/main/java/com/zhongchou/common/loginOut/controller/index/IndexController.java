package com.zhongchou.common.loginOut.controller.index;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.base.SubmitToken;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dao.IUserAccountBindBankDao;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.CompanionDto;
import com.zhongchou.common.dto.CooperateDto;
import com.zhongchou.common.dto.ProjectDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IIndexService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class IndexController extends BaseController {

	@Autowired
	private IIndexService indexService;

	@Autowired
	private ProductService productService;
	@Autowired
	private IUserAccountBindBankDao userAccountBindBankDao;


	@RequestMapping("/bannerList.do")
	public ModelAndView bannerList(HttpServletRequest request,HttpServletResponse response,Model model){
		 Map out = new HashMap();
		 List<BannerDto> bannerList = indexService.getBannerList("1");//首页
		 out.put("bannerList", bannerList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	@RequestMapping("/projectList.do")
	public ModelAndView projectList(HttpServletRequest request,HttpServletResponse response,Model model){

		 Map out = new HashMap();
		 List<ProjectDto> projectList = productService.getProjectList();
		 out.put("projectList", projectList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	@RequestMapping("/companionList.do")
	public ModelAndView companionList(HttpServletRequest request,HttpServletResponse response,Model model){

		 Map out = new HashMap();
		 List<CompanionDto> companionList = indexService.getCompanionList();
		 out.put("companionList", companionList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	@RequestMapping("/cooperateList.do")
	public ModelAndView cooperateList(HttpServletRequest request,HttpServletResponse response,Model model){

		 Map out = new HashMap();
		 List<CooperateDto> cooperateList = indexService.getCooperateList();
		 out.put("cooperateList", cooperateList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}
	@RequestMapping("/SCLogin/initIndex.do")
	public ModelAndView initIndex(HttpServletRequest request,HttpServletResponse response,Model model){
		 Map out = new HashMap();
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}
	//退出
	@RequestMapping("/SCLogin/signOut.do")
	public ModelAndView signOut(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		clearUserInfo();
		Map out = new HashMap();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);

        return null;
	}
	//退出
	@RequestMapping("/signOut.do")
	public ModelAndView loginOutsignOut(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		clearUserInfo();
		Map out = new HashMap();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);

        return null;
	}
	@RequestMapping("/SCLogin/isAuthentica.do")
	public ModelAndView isAuthentica(HttpServletRequest request,HttpServletResponse response,Model model){
		 Map out = new HashMap();
		 UserDto user = getUser();
         if (null == user) {
         	out.put(Constants.RET_CODE, "999");
 			out.put(Constants.RET_MSG, "未登录");
 			JsonUtil.writeJson(response,out);
 			 return null;
         }else{
        	List<Row> bankRow  = userAccountBindBankDao.queryBankCard(user.getOidUserId());
        	String bankCd = "";
        	if(bankRow.size()>0){
        		Map bankMap = bankRow.get(0);
        		bankCd = ConvUtils.convToString(bankMap.get("BANK_CD"));
        	}
        	out.put("bankCd", bankCd);
        	out.put("tipCard", "1");
          	out.put("riskAssessment", "1");
          	out.put("bankContract", "1");
          	out.put("isVerify", "1");
          	out.put("userPayPwd", "1");
      		String tipCard = user.getTcFlag();
      		String riskAssessment = user.getRaFlag();
      		String verifyFlag = user.getVerifyFlag();
      		int signFlag = Integer.parseInt(user.getSignFlag());
      		if(StringUtils.isEmpty(user.getIdCard())
      				||StringUtils.isEmpty(verifyFlag
      						)){//未实名
      			out.put("isVerify", "0");
      		}if(!"1".equals(riskAssessment)){//未测评
      			out.put("riskAssessment", "0");
      		}if(!"1".equals(tipCard)){//未绑卡
      			out.put("tipCard", "0");
      		}if(signFlag !=1){//未进行网银签约
         		out.put("bankContract", "0");
         	}if(StringUtils.isEmpty(user.getUserPayPwd())){
         		out.put("userPayPwd", "0");
         	}
         }
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	//获取submitToken
	@RequestMapping("/getSubmitToken.do")
	@SubmitToken(save=true)
	public ModelAndView getSubmitToken(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		out.put("SUBMITTOKEN", getSessionObj("SubmitToken"));
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);

        return null;
	}

	//验证submitToken
	@RequestMapping("/setSubmitToken.do")
	@SubmitToken(remove=true)
	public ModelAndView setSubmitToken(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);

        return null;
	}

	//添加反馈
	@RequestMapping("/SCLogin/addFeedback.do")
	public ModelAndView addFeedback(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Map out = new HashMap();
		Map insMap = new HashMap();
		String content = request.getParameter("content");
		String contactInfo = request.getParameter("contactInfo");
		if(StringUtils.isEmpty(content)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "输入反馈内容");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserDto user = getUser();
		insMap.put("content", content);
		insMap.put("contact", contactInfo);
		insMap.put("userId", user.getOidUserId());
		if(indexService.addFeedback(insMap)==0){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}

	//登录页面图
	@RequestMapping("/loginOut/initLogin.do")
	public ModelAndView initLogin(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		 Map out = new HashMap();
		 List<BannerDto> bannerList = indexService.getBannerList("3");//登录页
		 out.put("bannerList", bannerList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}
}
