package com.zhongchou.common.loginOut.controller.notice;

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

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.service.INoticeService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class NoticeController extends BaseController {
	Logger logger=Logger.getLogger(NoticeController.class);
	@Autowired
	private INoticeService noticeService;

	/**
	 * 获取首页滚动公告
	 *
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/getHomePageNotice.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getHomePageNotice(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		List homeNoticeList = noticeService.getHomePageNotice();
		out.put("homeNoticeList", homeNoticeList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}


	/**
	 * 获取公告列表
	 *
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/getNoticeList.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getNoticeList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String page = request.getParameter("sumNum");//页数
		String type = request.getParameter("type");//新闻类型 1：平台公告;2:最新动态;3：媒体报道
		/*if(isApp()){
			type = "1";
		}*/
		if(StringUtils.isEmpty(type)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "缺少参数");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(page)){
			page= "1";
		}
		int PageCount =10;//显示条数
		if("1".equals(type)){
			PageCount =5;
		}
		int initPage = 0;
		try {
			initPage = Integer.parseInt(page);
			initPage = initPage<1?1:initPage;
		} catch (Exception e) {
			logger.info("page error :"+page);
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		initPage = (initPage-1)*PageCount;
		int noticeCount = noticeService.getNoticeCount(type);
		List noticeList = noticeService.getNoticeList(type,initPage, PageCount);
		int totalNumPages = (noticeCount-1)/PageCount+1;
		out.put("noticeList", noticeList);
		out.put("totalNumPages", totalNumPages);
		out.put("noticeCount", noticeCount);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	/**
	 * 获取详情
	 *
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/getNoticeDetail.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getNoticeDetail(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String page = request.getParameter("sumNum");//当前第几页
		String type = request.getParameter("type");//新闻类型 0:最新活动；1：平台公告;2:公司动态;3：媒体报道
		String noticeId = request.getParameter("noticeId");
		if(StringUtils.isEmpty(noticeId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "缺少参数");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(type)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "缺少参数");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(StringUtils.isEmpty(page)){
			page= "1";
		}
		int PageCount =10;//显示条数
		if("1".equals(type)){
			PageCount =10;
		}
		int initPage  = Integer.parseInt(page);
		 initPage = (initPage-1)*PageCount;
		out = noticeService.getNoticeDetail(noticeId,type);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
	/**
	 * 获取App公告详情
	 *
	 * @return 无
	 */
	@RequestMapping(value = "loginOut/getAppNoticeDetail.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAppNoticeDetail(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String noticeId = request.getParameter("noticeId");
		if(StringUtils.isEmpty(noticeId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "缺少参数");
			JsonUtil.writeJson(response,out);
			return null;
		}
		out = noticeService.getAppNoticeDetail(noticeId);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
}
