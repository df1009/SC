package com.zhongchou.common.login.controller.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserForum;
import com.zhongchou.common.service.UserForumService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class UserForumController extends BaseController {
	Logger logger=Logger.getLogger(UserForumController.class);
	@Autowired
	private UserForumService userForumService;

	@RequestMapping(value = "SCLogin/addUserForum.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView addUserForum(HttpServletRequest request,HttpServletResponse response,Model model,String username,String password){
		Map out = new HashMap();
		logger.info("UserForumController.addUserForum  start");
		String content = request.getParameter("content");//评论内容
		String projectId = request.getParameter("projectId");//项目id
		String answerNickName = request.getParameter("answerNickName");//回复用户昵称
		String answerId = request.getParameter("answerId");//回复用户id
		String showFlag = request.getParameter("showFlag");//显示标识0：显示；1:隐藏
		String fatherId = request.getParameter("fatherId");//评论父节点id（没有上级楼层为空）
		UserDto user = (UserDto)request.getSession().getAttribute(Constants.SESSION_USER_INFO);
		if(fatherId==null){
			fatherId="0";
		}

		//查询当前登录用户是否是该项目发起人-待补充
		boolean prjFlag = true;
		if(StringUtils.isEmpty(content)
				||StringUtils.isEmpty(projectId)
				||StringUtils.isEmpty(showFlag)
				||content.length()>140){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if("true".equals(showFlag)){
			showFlag = "1";
		}else if("false".equals(showFlag)){
			showFlag = "0";
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!prjFlag&&"1".equals(showFlag)){
			logger.debug("userId:"+user.getOidUserId()+"  projectId:"+projectId+"  showFlag:"+showFlag);
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//id解密-待补充
		if(user==null){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "未登录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserForum userForum = new UserForum();
		userForum.setOidUserId(user.getOidUserId());//用户id
		userForum.setNickName(user.getNickname());//用户昵称
/*		userForum.setOidUserId("20625");//用户id
		userForum.setNickName("吴润阁");//用户昵称*/
		userForum.setOidFinanceProjectsId(projectId);//产品id
		userForum.setForumNews(content);//评论内容
		userForum.setAnswerNickname(answerNickName);//回复用户昵称
		userForum.setAnswerOidUserId(answerId);//回复用户id
		userForum.setShowFlag(showFlag);//显示标识
		userForum.setUpLevel(fatherId);//上级评论楼层或id
		userForum.setInsIp(getRemoteAddr());//IP地址
		userForumService.insertForum(userForum);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;

	}

	@RequestMapping(value = "SCLogin/delUserForum.do", method ={ RequestMethod.POST})
	public ModelAndView delUserForum(HttpServletRequest request,HttpServletResponse response,Model model,String username,String password){
		logger.info("UserForumController.delUserForum  start");
		Map out = new HashMap();
		String id = request.getParameter("id");//当前评论的id
		UserDto user = (UserDto)request.getSession().getAttribute(Constants.SESSION_USER_INFO);
		if(!StringUtil.isAllNumber(id)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(user==null){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "未登录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(userForumService.delForum(id,user.getOidUserId())==1){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "只能删除自己的评论");
			JsonUtil.writeJson(response,out);
			return null;
		}

	}


	@RequestMapping(value = "loginOut/selUserForum.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selUserForum(HttpServletRequest request,HttpServletResponse response,Model model,String username,String password){
		logger.info("UserForumController.selUserForum  start");
		Map out = new HashMap();
		String page = request.getParameter("page");//查询页数
		//每页起始评论
		int initPage = 0;
		//查询的页数
		int intPage = 0;
		int PageCount = 10;
		UserDto user = (UserDto)request.getSession().getAttribute(Constants.SESSION_USER_INFO);
		if(StringUtils.isEmpty(page)){
			page="1";
		}
		if(!StringUtil.isAllNumber(page)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		intPage = Integer.parseInt(page);
		initPage = (intPage-1)*PageCount;
		//总评论数
		int totalCount = userForumService.countForum();
		//总页数
		int totalNumPages = (totalCount-1)/PageCount+1;
		if(totalCount==0){
			totalNumPages=0;
		}
		if(intPage<1||intPage>totalNumPages){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserForum userForum = new UserForum();
		// 每页起始位置
		userForum.setPageIndex(initPage);
		// 每页最大件数
		userForum.setPageSize(PageCount);


		List<UserForum> allList = userForumService.selMainForum(userForum,user);
		out.put("allComment", allList);
		out.put("totalCount", totalCount);
		out.put("totalNumPages", totalNumPages);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;

	}




}
