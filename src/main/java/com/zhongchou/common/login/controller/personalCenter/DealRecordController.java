package com.zhongchou.common.login.controller.personalCenter;

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
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IDealRecordService;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class DealRecordController extends BaseController {

	Logger logger=Logger.getLogger(DealRecordController.class);

	@Autowired
	private IDealRecordService dealRecordService;

	/**
	 * 交易记录信息获取
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param dateType 时间类型
	 * @param projectType 交易信息状态
	 * @param curPage
	 * @return
	 */
	@RequestMapping("/SCLogin/dealRecord.do")
	public ModelAndView dealRecord(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("DealRecordController.dealRecord  start");
		Map out = new HashMap();
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String dateType = request.getParameter("dateType");
		String projectType = request.getParameter("projectType");
		String projectName = request.getParameter("projectName");
		//String StrsumNum = request.getParameter("sumNum");//手机端下滑加载更多次数
	    int curPage = StringUtils.isEmpty(request.getParameter("curPage")) ? 1:Integer.valueOf(request.getParameter("curPage"));
	    int pageSize =10;//显示条数
	    int PageCount =(curPage-1)*pageSize;//起始位置
/*	    if(isApp()){
	    	int clickNum =0;//点击次数
	    	PageCount = 0;
	    	if(!StringUtils.isEmpty(StrsumNum)){
				try {
					clickNum = Integer.parseInt(StrsumNum);
				} catch (Exception e) {
					out.put(Constants.RET_CODE, "002");
					out.put(Constants.RET_MSG, "系统异常");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			int addNum =10;//每次增加数量
			pageSize+=clickNum*addNum;
	    }*/
	    int dealRecordtCnt = dealRecordService.getDealRecordCnt(oidUserId, beginDate, endDate,dateType, projectType,projectName);
	    List<DealRecordDto> dealRecordList = dealRecordService.getDealRecordList(oidUserId, beginDate, endDate,dateType, projectType,projectName,pageSize,PageCount);

		out.put("beginDate", beginDate);
		out.put("endDate", endDate);
		out.put("dateType", dateType);
		out.put("projectType", projectType);
		out.put("curPage", curPage);
		if(dealRecordtCnt == 0){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "暂无记录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		int dealRecordtPage = (dealRecordtCnt-1)/pageSize +1;
		out.put("dealRecordtPage", Integer.toString(dealRecordtPage));
		out.put("dealRecordtCnt", dealRecordtCnt);
		out.put("dealRecordList", dealRecordList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}
	@RequestMapping("/SCLogin/dealAppRecord.do")
	public ModelAndView dealAppRecord(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("DealRecordController.dealRecord  start");
		Map out = new HashMap();
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String dateType = request.getParameter("dateType");
		String projectType = request.getParameter("projectType");//交易状态信息
		String projectName = request.getParameter("projectName");
		//String StrsumNum = request.getParameter("sumNum");//手机端下滑加载更多次数
	    int curPage = StringUtils.isEmpty(request.getParameter("curPage")) ? 1:Integer.valueOf(request.getParameter("curPage"));
	    int pageSize =10;//显示条数
	    int PageCount =(curPage-1)*pageSize;//起始位置
/*	    if(isApp()){
	    	int clickNum =0;//点击次数
	    	PageCount = 0;
	    	if(!StringUtils.isEmpty(StrsumNum)){
				try {
					clickNum = Integer.parseInt(StrsumNum);
				} catch (Exception e) {
					out.put(Constants.RET_CODE, "002");
					out.put(Constants.RET_MSG, "系统异常");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			int addNum =10;//每次增加数量
			pageSize+=clickNum*addNum;
	    }*/
	    int dealRecordtCnt = dealRecordService.getDealRecordCnt(oidUserId, beginDate, endDate,dateType, projectType,projectName);
	    List<DealRecordDto> dealRecordList = dealRecordService.getDealRecordList(oidUserId, beginDate, endDate,dateType, projectType,projectName,pageSize,PageCount);

		out.put("beginDate", beginDate);
		out.put("endDate", endDate);
		out.put("dateType", dateType);
		out.put("projectType", projectType);
		out.put("curPage", curPage);
		if(dealRecordtCnt == 0){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "暂无记录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		int dealRecordtPage = (dealRecordtCnt-1)/pageSize +1;
		out.put("dealRecordtPage", Integer.toString(dealRecordtPage));
		out.put("dealRecordtCnt", dealRecordtCnt);
		out.put("dealRecordList", dealRecordList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}
}
