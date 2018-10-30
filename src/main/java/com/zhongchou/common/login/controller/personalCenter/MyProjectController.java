package com.zhongchou.common.login.controller.personalCenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.MyAppProjectlDto;
import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.ILoginService;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class MyProjectController extends BaseController {
	Logger logger=Logger.getLogger(MyProjectController.class);
	String ZZbankCd = Config.getString("ZZbankCd");//中证金通支持的银行

	@Autowired
	private IMyProjectService myProjectService;

	@Autowired
	private ILoginService loginService;

	@Autowired
	private IUserTenderService userTenderService;

	@Autowired
	private IBankService bankService;


	/**
	 * 我的项目信息加载
	 * @param curPage 页数
	 * @param projectState 项目状态
	 * @return
	 */
	@RequestMapping("/SCLogin/myProject.do")
	public ModelAndView projectList(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.myProject  start");
		UserDto userDto = getUser();
		 String oidUserId = userDto.getOidUserId();
		 int curPage = request.getParameter("curPage") == null ? 1:Integer.valueOf(request.getParameter("curPage"));
		 String projectState = request.getParameter("projectState");//1：待审核  2：审核中  3 已发布
		 int myProjectCnt = myProjectService.getMyProjectCnt(oidUserId, projectState);
		 List<MyProjectlDto> projectList = myProjectService.getMyProjectList(projectState,oidUserId,10, curPage);
		 Map out = new HashMap();
		 int pageNum = (myProjectCnt-1)/10 + 1;
		 out.put("pageNum", pageNum);
		 out.put("projectList", projectList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}
	/**
	 * 我的app项目列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/SCLogin/myAppProject.do")
	public ModelAndView projectAppList(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.myAppProject  start");
		UserDto userDto = getUser();
		 String oidUserId = userDto.getOidUserId();
		 int curPage = request.getParameter("curPage") == null ? 1:Integer.valueOf(request.getParameter("curPage"));
		 String projectState = request.getParameter("projectState");//1：待审核  2：审核中  3 已发布
		 int myProjectCnt = myProjectService.getMyProjectCnt(oidUserId, projectState);
		 List<MyAppProjectlDto> projectList = myProjectService.getMyAppProjectList(oidUserId,10, curPage);
		 Map out = new HashMap();
		 int pageNum = (myProjectCnt-1)/10 + 1;
		 out.put("pageNum", pageNum);
		 out.put("projectList", projectList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	/**
	 * 我的项目详情
	 * @param projectCode 我的项目编号
	 * @return
	 */
	@RequestMapping("/SCLogin/projectDetail.do")
	public ModelAndView projectDetail(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.projectDetail  start");
		UserDto userDto = getUser();
		 String oidUserId = userDto.getOidUserId();
		String projectCode = request.getParameter("projectCode");
		String type = request.getParameter("type");//类型1 项目信息  2项目进展

		Map out = new HashMap();
		//查询项目信息
		Map project = myProjectService.selProjectAscription(projectCode,oidUserId);
		if(project.isEmpty()){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "只能查看自己发布的项目");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//当前产品详细信息
	    Map projectDetail = userTenderService.selProductDetailList(projectCode,type);
		if(projectDetail==null){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "产品id有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		List<String> industry = myProjectService.getIndustry();
		List verifyList= myProjectService.getVerifyList(projectCode,oidUserId);
		 //ProjectInfoDto projectDetail = myProjectService.getprojectDeatail(projectCode);
		 //List<ProgressDto> progressList= myProjectService.getProgressList(projectCode);

		//详细信息
		 out.put("projectDetail", projectDetail);
		//行业信息
		 out.put("industry", industry);
		 //驳回原因
		 out.put("verifyList", verifyList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}




	/**
	 * 项目信息添加 保存草稿
	 * @param introduction 项目简介
	 * @param videoUrl 视频路径
	 * @param imgUrl 图片路径
	 * @param industry 行业
	 * @param projectTitle 项目标题
	 * @param projectCode 项目code
	 * @param projectContent 项目内容
	 * @param partyNum 模块编号
	 * @param ivsNumber 企业组织机构代码
	 * @return
	 */
	@RequestMapping("/SCLogin/insertProjectInformation.do")
	public ModelAndView insertProjectInformation(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.insertProjectInformation  start");
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String projectTitle	 = request.getParameter("projectTitle");//模块title
		String projectCode = request.getParameter("projectCode");//产品编号
		String projectContent = request.getParameter("projectContent");//模块内容
		String partyNum = request.getParameter("partyNum");//模块编号
		String id = request.getParameter("id");//模块id
		Map out = new HashMap();
		if(StringUtils.isEmpty(projectCode)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "请输入产品编号");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(projectTitle)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "请输入标题");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(projectContent)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请输入正文");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(partyNum)){
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "请输入描述模块序号");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//查询项目信息
		Map project = myProjectService.selProjectAscription(projectCode,oidUserId);
		if(project.isEmpty()){
			logger.info("用户："+oidUserId+"保存不是自己的项目:"+projectCode+"的草稿");
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "只能修改自己发布的项目");
			JsonUtil.writeJson(response,out);
			return null;
		}
		Map proMap = new HashMap();
		proMap.put("projectCode", projectCode);
		proMap.put("partyNum", partyNum);
		proMap.put("projectTitle", projectTitle);
		proMap.put("projectContent", projectContent);
		proMap.put("oidUserId", oidUserId);
		proMap.put("id", id);
		String insertInformation = null;
		insertInformation =myProjectService.insertProjectInformation(proMap);
		if("000".equals(insertInformation)){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "添加失败");
		}
		 out.put("projectCode",projectCode);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	/**
	 * 项目进展插入
	 * @param progressDate 进展时间
	 * @param progressContent 进展内容
	 * @param projectCode 项目code
	 * @return/SCLogin
	 */
	@RequestMapping("/SCLogin/insertProjectProgress.do")
	public ModelAndView insertProjectProgress(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.insertProjectProgress  start");

		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String progressContent = request.getParameter("progressContent");//进展内容
		String projectCode = request.getParameter("projectCode");//项目编号
		String progressDate = request.getParameter("progressDate");//进展时间
		String progressImg = request.getParameter("progressImg");//进展图片 多张图片（,）分割
		String id = request.getParameter("id");//进展id
		Map out = new HashMap();
		if(StringUtils.isEmpty(progressDate)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "请选择项目进展时间");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(progressContent)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请输入项目进展");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//查询项目是否是本人发布的项目 true: 不是自己的项目  false：是
		if(myProjectService.selProjectAscription(projectCode,oidUserId).isEmpty()){
			logger.info("用户："+oidUserId+"保存不是自己的项目:"+projectCode+"项目进展");
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "只能修改自己发布的项目");
			JsonUtil.writeJson(response,out);
			return null;
		}


		List<Object> params = new ArrayList<>();
		int insertProjectProgress = 0;
		if(StringUtils.isEmpty(id)){
			params.add(projectCode);
			params.add(progressContent);
			params.add(progressDate);
			params.add(oidUserId);
			params.add(progressImg);
			insertProjectProgress =myProjectService.insertProjectProgress(params);
		}else{
			params.add(progressContent);
			params.add(progressDate);
			params.add(progressImg);
			params.add(id);
			insertProjectProgress =myProjectService.updProjectProgress(params);
		}

		if(insertProjectProgress != 0){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "插入失败");
		}
		 out.put("projectCode",projectCode);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	/**
	 * 删除模块
	 * @param projectCode 项目code
	 * @param partyNum 排序序号
	 * @return
	 */
	@RequestMapping("/SCLogin/deleteProjectInformation.do")
	public ModelAndView deleteProjectInformation(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String projectCode = request.getParameter("projectCode");
		String partyNum = request.getParameter("partyNum");
		String id = request.getParameter("id");
		if(StringUtils.isEmpty(projectCode)
				||StringUtils.isEmpty(partyNum)
				||StringUtils.isEmpty(id)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
	         return null;
		}
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();

		int deleteProjectInformation =myProjectService.deleteProjectInformation(projectCode,partyNum,oidUserId,id);

		if(deleteProjectInformation != 0){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			logger.info("用户："+oidUserId+"删除项目："+projectCode+"的模块号"+partyNum+"失败");
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "删除失败");
		}
		 out.put("projectCode",projectCode);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	/**
	 * 删除进展
	 * @param projectCode 项目code
	 * @param partyNum 排序序号
	 * @return
	 */
	@RequestMapping("/SCLogin/deleteProjectProgress.do")
	public ModelAndView deleteProjectProgress(HttpServletRequest request,HttpServletResponse response,Model model){

		String projectCode = request.getParameter("projectCode");
		String id = request.getParameter("id");//进展id
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map out = new HashMap();
		int deleteProjectInformation =myProjectService.deleteProjectProgress(projectCode,id,oidUserId);

		if(deleteProjectInformation != 0){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			logger.info("用户："+oidUserId+"删除项目："+projectCode+"的进展id:"+id+"失败");
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "删除失败");
		}
		 out.put("projectCode",projectCode);
		 JsonUtil.writeJson(response,out);
         return null;
	}


	/**
	 * 提交审核
	 * @param projectCode 项目code
	 */
	@RequestMapping("/SCLogin/submitProject.do")
	public ModelAndView submitProject(HttpServletRequest request,HttpServletResponse response,Model model){
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String projectCode = request.getParameter("projectCode");
		Map out = new HashMap();
		int submitProject =myProjectService.submitProject(projectCode,oidUserId);

		if(submitProject != 0){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "提交失败");
		}
		 out.put("projectCode",projectCode);
		 JsonUtil.writeJson(response,out);
         return null;
	}

	/**
	 * 添加项目介绍
	 * @param introduction 项目简介
	 * @param videoUrl 视频路径
	 * @param imgUrl 图片路径
	 * @param industry 行业
	 * @param projectTitle 项目标题
	 * @param projectCode 项目code
	 * @param projectContent 项目内容
	 * @param partyNum 模块编号
	 * @param ivsNumber 企业组织机构代码
	 * @return
	 */
	@RequestMapping("/SCLogin/insertProjectIntroduction.do")
	public ModelAndView insertProjectIntroduction(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.insertProjectInformation  start");
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map out = new HashMap();
		String introduction = request.getParameter("introduction");//产品简介
		String videoUrl = request.getParameter("videoUrl");//视屏url
		String imgUrl = request.getParameter("imgUrl");//产品信息大图
		//String appImgUrl = request.getParameter("appImgUrl");//app产品信息大图
		String industry = request.getParameter("industry");//行业
		String projectCode = request.getParameter("projectCode");//项目id
		//String mainTitle = request.getParameter("mainTitle");//项目主标题
		String status = request.getParameter("type");//1:保存草稿  2提交审核
		if(StringUtils.isEmpty(introduction)
				||StringUtils.isEmpty(videoUrl)
				||StringUtils.isEmpty(imgUrl)
				//||StringUtils.isEmpty(appImgUrl)
				||StringUtils.isEmpty(industry)
				||StringUtils.isEmpty(projectCode)
				//||StringUtils.isEmpty(mainTitle)
				){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
		}
		if(Arrays.binarySearch(new String[]{"1","2"},status)<0){
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "类型有误");
		}

		//查询项目信息
		Map project = myProjectService.selProjectAscription(projectCode,oidUserId);
		if(project.isEmpty()){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "只能修改自己发布的项目");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//查询是否是初审状态
		String checkStatus = ConvUtils.convToString(project.get("project"));
		if("1".equals(checkStatus)){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "该项目状态不能修改此信息");
			JsonUtil.writeJson(response,out);
			return null;
		}
		Map introductionMap = new HashMap();
		introductionMap.put("industry", industry);
		introductionMap.put("imgUrl", imgUrl);
		//introductionMap.put("appImgUrl", appImgUrl);
		introductionMap.put("videoUrl", videoUrl);
		introductionMap.put("introduction", introduction);
		introductionMap.put("projectCode", projectCode);
		introductionMap.put("oidUserId", oidUserId);
		introductionMap.put("status", status);
		//introductionMap.put("mainTitle", mainTitle);
		if(myProjectService.insertProjectIntroduction(introductionMap)){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "提交失败");
		}
		JsonUtil.writeJson(response,out);
		return null;
	}

	/**
	 * 查询公共模块
	 * @param
	 */
	@RequestMapping("/SCLogin/selIntroductionTemplate.do")
	public ModelAndView selIntroductionTemplate(HttpServletRequest request,HttpServletResponse response,Model model){
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map out = new HashMap();
		List Template =myProjectService.selIntroductionTemplate();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		out.put("Template",Template);
		JsonUtil.writeJson(response,out);
        return null;
	}

	/**
	 * 查询单个公共模块
	 * @param
	 */
	@RequestMapping("/SCLogin/selSingleIntroductionTemplate.do")
	public ModelAndView selSingleIntroductionTemplate(HttpServletRequest request,HttpServletResponse response,Model model){
		String id = request.getParameter("id");//模块编号
		Map out = new HashMap();
		if(StringUtils.isEmpty(id)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "模块id有误");
			JsonUtil.writeJson(response,out);
	        return null;
		}
		out =myProjectService.selSingleIntroductionTemplate(id);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}

	/**
	 * 更改项目信息模块编号
	 * @param
	 *//*
	@RequestMapping("/SCLogin/updateModuleNumber.do")
	public ModelAndView updateModuleNumber(HttpServletRequest request,HttpServletResponse response,Model model){
		UserDto userDto = (UserDto)request.getSession().getAttribute(Constants.SESSION_USER_INFO);
		String oidUserId = userDto.getOidUserId();
		Map out = new HashMap();
		List Template =myProjectService.selIntroductionTemplate();
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		out.put("Template",Template);
		JsonUtil.writeJson(response,out);
        return null;
	}*/

	/**
	 * 查询卡号中证是否支持
	 * @param
	 */
	@RequestMapping("/SCLogin/selSupportBank.do")
	public ModelAndView selSupportBank(HttpServletRequest request,HttpServletResponse response,Model model){
		String id = request.getParameter("bankId");//银行卡号
		Map out = new HashMap();
		String flag = "0";
		Map bankFlag =bankService.selSupportBank(id);
		if(bankFlag.isEmpty()){//空 不支持
			flag="0";
		}else{
			String[] bankCdList = ZZbankCd.split(",");
			for (int i = 0; i < bankCdList.length; i++) {
		        if(bankCdList[i].equals(bankFlag.get("bankCd"))){
		        	flag="1";
		        	break;
		        }
		    }
		}
		out.put("bankName",bankFlag.get("bankName"));
		out.put("bankCd",bankFlag.get("bankCd"));
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		out.put("bankFlag",flag);
		JsonUtil.writeJson(response,out);
        return null;
	}
}
