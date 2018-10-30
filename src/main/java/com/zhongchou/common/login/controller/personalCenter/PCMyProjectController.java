package com.zhongchou.common.login.controller.personalCenter;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IPCMyProjectService;
import com.zhongchou.common.service.PCProductListService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class PCMyProjectController extends BaseController {
	Logger logger=Logger.getLogger(PCMyProjectController.class);
	String ZZbankCd = Config.getString("ZZbankCd");//中证金通支持的银行

	@Autowired
	private IPCMyProjectService myProjectService;
	@Autowired
	private PCProductListService productService;


	/**
	 * 我的项目信息加载
	 * @param curPage 页数
	 * @param projectState 项目状态
	 * @return
	 */
	@RequestMapping("/SCLogin/PCmyProject.do")
	public ModelAndView projectList(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.PCmyProject  start");
		Map out = new HashMap();
		UserDto userDto = getUser();
		 String oidUserId = userDto.getOidUserId();
		 String projectState = request.getParameter("projectState");//0:待编辑 1：待审核  2：驳回  3 已发布
		String page = request.getParameter("curPage");
		int curPage = 0;
		if(StringUtils.isEmpty(page)){
			curPage = 1;
		}else{
			try {
				curPage = Integer.valueOf(page);
			} catch (Exception e) {
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "系统异常");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
		 int myProjectCnt = myProjectService.getMyProjectCnt(oidUserId, projectState);
		 List projectList = myProjectService.getMyProjectList(projectState,oidUserId,10, curPage);
		 
		 //给用户的提示信息
		 Set stSet = new HashSet();
		 for (int i = 0; i < projectList.size(); i++) {
			 Map m = (Map)projectList.get(i);
			 if("2".equals(m.get("projectReviewedSt"))){
				 stSet.add("您的项目"+m.get("projectId")+"审核被管理员驳回，请及时编辑后重新提交");
			 }else if("3".equals(m.get("projectReviewedSt"))){
				 stSet.add("您的项目"+m.get("projectId")+"审核被管理员通过");
			 }if("1".equals(m.get("projectOnlineFlag"))){
				 stSet.add("您的项目"+m.get("projectId")+"审核被管理员下线，请联系您的项目专员进行沟通。");
			 }
		}
		 out.put("point", stSet);
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
	@RequestMapping("/SCLogin/PCprojectDetail.do")
	public ModelAndView projectDetail(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.projectDetail  start");
		UserDto userDto = getUser();
		 String oidUserId = userDto.getOidUserId();
		String projectCode = request.getParameter("projectCode");

		Map out = new HashMap();
		Map reqMap = new HashMap();  
		reqMap.put("oidUserId", oidUserId);
		reqMap.put("projectId", projectCode);
		//查询项目信息
		Map project = myProjectService.selProductDetail(reqMap);
		List verifyList= myProjectService.getVerifyList(projectCode,oidUserId);
		//行业
		Set setList = productService.seldustryType();
		out.put("industryList", setList); 
		//驳回原因
		 out.put("verifyList", verifyList);
		 out.put("project", project);
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
	@RequestMapping("/SCLogin/PCprojectDetailModular.do")
	public ModelAndView Modular(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.projectDetail  start");
		UserDto userDto = getUser();
		 String oidUserId = userDto.getOidUserId();
		String projectCode = request.getParameter("projectCode");

		Map out = new HashMap();
		Map reqMap = new HashMap();  
		reqMap.put("oidUserId", oidUserId);
		reqMap.put("projectId", projectCode);
		//查询想模块信息
		List ProductModularDetailList = myProjectService.selProductModularDetailList(reqMap);
		//驳回原因
		 out.put("ProductModularDetailList", ProductModularDetailList);
		 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		 JsonUtil.writeJson(response,out);
         return null;
	}
	

	/**
	 * 我的项目提交项目介绍信息des(保存草稿)
	 * @param projectCode 我的项目编号
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/SCLogin/PCsubmitMyProjectDes.do")
	public ModelAndView submitMyProjectDes(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException{
		logger.info("MyProjectController.submitMyProjectDes  start");
		Map out = new HashMap();
		Map reqMap = new HashMap();
		String content = request.getParameter("content");
		JSONObject obj = JSONObject.fromObject(content);
        //System.out.println("这个是用JSON类,指定解析类型，来解析JSON字符串!!!");  
        for (Object map : obj.keySet()){  
            //System.out.println("key为："+map+"值为："+obj.getString(ConvUtils.convToString(map)));  
            reqMap.put(map, obj.getString(ConvUtils.convToString(map)));
            if("minTargetAmt".equals(map)){
            	 reqMap.put("minTargetAmt",  StringUtils.isEmpty(obj.getString(ConvUtils.convToString(map)))?
         				"0":obj.getDouble(ConvUtils.convToString(map))*1000000
         				);//最低目标额度（万元）
            }
            
            if("maxTargetAmt".equals(map)){
           	 reqMap.put("maxTargetAmt",  StringUtils.isEmpty(obj.getString(ConvUtils.convToString(map)))?
        				"0":obj.getDouble(ConvUtils.convToString(map))*1000000
        				);//最高目标额度（万元）
           }
            
            if("minSubsAmt".equals(map)){
           	 reqMap.put("minSubsAmt",  StringUtils.isEmpty(obj.getString(ConvUtils.convToString(map)))?
        				"0":obj.getDouble(ConvUtils.convToString(map))*100
        				);//最低认购金额（元）
           }
            
            if("subsAddAmt".equals(map)){
           	 reqMap.put("subsAddAmt",  StringUtils.isEmpty(obj.getString(ConvUtils.convToString(map)))?
        				"0":obj.getDouble(ConvUtils.convToString(map))*100
        				);//最小追加金额（元）
           }
            
            if("maxSubsSmt".equals(map)){
              	 reqMap.put("maxSubsSmt",  StringUtils.isEmpty(obj.getString(ConvUtils.convToString(map)))?
           				"0":obj.getDouble(ConvUtils.convToString(map))*100
           				);//最高认购金额（元）
              }
        }  
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		reqMap.put("draftFlag", "1");//草稿
		reqMap.put("status", "0");
		reqMap.put("oidUserId", oidUserId);
		//查询项目信息
		if(myProjectService.saveMyProjectInfo(reqMap)==1){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "保存失败");
		}
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
	@RequestMapping("/SCLogin/PCinsertProjectInformation.do")
	public ModelAndView insertProjectInformation(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.insertProjectInformation  start");
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String projectTitle	 = request.getParameter("projectTitle");//模块title
		String projectCode = request.getParameter("projectCode");//产品编号
		String projectContent = request.getParameter("projectContent");//模块内容
		String partyNum = request.getParameter("partyNum");//模块编号
		String projectId = request.getParameter("projectId");//模块id(有修改  没有新增)
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
		
		Map proMap = new HashMap();
		proMap.put("projectCode", projectCode);
		proMap.put("partyNum", partyNum);
		proMap.put("projectTitle", projectTitle);
		proMap.put("projectContent", projectContent);
		proMap.put("oidUserId", oidUserId);
		proMap.put("projectId", projectId);
		proMap.put("type", "1");//1项目模块  2项目进展
		proMap.put("imgPath", "");
		Map retMap =myProjectService.insertProjectInformation(proMap);
		if("0".equals(retMap.get("proCode"))){
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
	 * 我的项目查询项目进展
	 * @param 
	 * @return
	 */
	@RequestMapping("/SCLogin/PCselMyProjectProgress.do")
	public ModelAndView selMyProjectProgress(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.selMyProjectProgress  start");
		String projectId = request.getParameter("projectId");//进展id
		Map out = new HashMap();
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map reqMap = new HashMap();
		reqMap.put("projectId", projectId);
		reqMap.put("oidUserId", oidUserId);
		Map ProjectProgressMap =  myProjectService.selMyProjectProgress(reqMap);
		out.put("ProjectProgressMap",ProjectProgressMap);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
	
	/**
	 * 我的项目进展保存草稿
	 * @param 
	 * @return
	 */
	@RequestMapping("/SCLogin/PCsaveMyProjectProgress.do")
	public ModelAndView saveMyProjectProgress(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.saveMyProjectProgress  start");
		String projectCode = request.getParameter("projectCode");//项目cd
		String projectContent = request.getParameter("projectContent");//进展内容
		String imgPath = request.getParameter("imgPath");//图片
		String progressDate = request.getParameter("progressDate");//进展时间
		String progressId = request.getParameter("progressId");//进展id(有修改  没有新增)
		Map out = new HashMap();
		if(StringUtils.isEmpty(projectCode)
				||StringUtils.isEmpty(projectContent)
				||StringUtils.isEmpty(progressDate)
				){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map reqMap = new HashMap();
		Map proMap = new HashMap();
		proMap.put("progressDate", progressDate);
		proMap.put("projectCode", projectCode);
		proMap.put("projectContent", projectContent);
		proMap.put("imgPath", imgPath);
		if(!StringUtils.isEmpty(imgPath))
			proMap.put("imgPath", imgPath.substring(0, imgPath.length()-1));
		proMap.put("oidUserId", oidUserId);
		proMap.put("type", "2");//1项目模块  2项目进展
		proMap.put("projectId", progressId);
		proMap.put("partyNum", "");
		Map retMap =myProjectService.insertProjectInformation(proMap);
		if("0".equals(retMap.get("proCode"))){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "添加失败");
		}
		JsonUtil.writeJson(response,out);
		return null;
	}
	/**
	 * 我的项目所有草稿状态提交审核
	 * @param 
	 * @return
	 */
	@RequestMapping("/SCLogin/submitMyProject.do")
	public ModelAndView submitMyProject(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyProjectController.submitMyProject  start");
		Map out = new HashMap();
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map reqMap = new HashMap();
		reqMap.put("oidUserId", oidUserId);
		if(myProjectService.submitMyProject(reqMap)){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "提交失败");
		}
		JsonUtil.writeJson(response,out);
		return null;
	}
	
	/**
	 * 删除模块和进展
	 * @param projectCode 项目code
	 * @param partyNum 排序序号
	 * @return
	 */
	@RequestMapping("/SCLogin/PCdeleteProjectInformation.do")
	public ModelAndView deleteProjectInformation(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String projectCode = request.getParameter("projectCode");//产品code
		String partyNum = request.getParameter("partyNum");//进展编号
		String projectId = request.getParameter("projectId");//模块或进展id
		String projectType = request.getParameter("projectType");//1：模块  2进展
		
		if(StringUtils.isEmpty(projectCode)
				||StringUtils.isEmpty(projectId)
				||!("1".equals(projectType)||"2".equals(projectType))
				){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
	         return null;
		}
		if("1".equals(projectType)
				&&StringUtils.isEmpty(partyNum)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
	         return null;
		}
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map reqMap = new HashMap();
		reqMap.put("projectCode", projectCode);
		reqMap.put("partyNum", partyNum);
		reqMap.put("oidUserId", oidUserId);
		reqMap.put("projectId", projectId);
		reqMap.put("projectType", projectType);
		if(myProjectService.deleteProjectInformation(reqMap)){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			logger.info("用户："+oidUserId+"删除项目："+projectCode+"id："+projectId+"失败");
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "删除失败");
		}
		 out.put("projectCode",projectCode);
		 JsonUtil.writeJson(response,out);
         return null;
	}
	/**
	 * 获取用户是否有我的项目菜单的权限
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/PCqueryMyProject.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView queryMyProject(HttpServletRequest request,HttpServletResponse response,Model model) {
		logger.info("PersonalInfo.queryMyProject  start");
		Map<String,Object> out = new HashMap<String,Object>();
		UserDto sessionUserDto = getUser();
		int count = myProjectService.getMyProject(sessionUserDto.getOidUserId())>0?1:0;
		out.put("projectFlag", count);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
	
	/**
	 * 我的项目模块排序
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/MyProjectModularSort.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView MyProjectModularSort(HttpServletRequest request,HttpServletResponse response,Model model) {
		logger.info("PersonalInfo.queryMyProject  start");
		Map<String,Object> out = new HashMap<String,Object>();
		String projectCode = request.getParameter("projectCode");//产品code
		String fistModId = request.getParameter("fistModId");//第一个模块的id
		String fistSort = request.getParameter("fistSort");//第一个模块的序号
		String secondModId = request.getParameter("secondModId");//第二个模块的id
		String secondSort = request.getParameter("secondSort");//第二个模块的序号
		UserDto sessionUserDto = getUser();
		if(!StringUtil.isAllNumber(fistModId)
				||!StringUtil.isAllNumber(secondModId)
				||!StringUtil.isAllNumber(fistSort)
				||!StringUtil.isAllNumber(secondSort)
				){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
		}
		Map reqMap = new HashMap();
		reqMap.put("projectCode", projectCode);
		reqMap.put("fistModId", fistModId);
		reqMap.put("fistSort", fistSort);
		reqMap.put("secondModId", secondModId);
		reqMap.put("secondSort", secondSort);
		reqMap.put("oidUserId", sessionUserDto.getOidUserId());
		if(!myProjectService.setMyProjectModularSort(reqMap)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "排序失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
}
