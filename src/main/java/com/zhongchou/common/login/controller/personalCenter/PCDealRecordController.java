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

import com.yanshang.util.DateUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IPCDealRecordService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class PCDealRecordController extends BaseController {

	Logger logger=Logger.getLogger(PCDealRecordController.class);

	@Autowired
	private IPCDealRecordService dealRecordService;

	/**
	 * 交易记录信息获取
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param dateType 时间类型
	 * @param projectType 交易信息状态
	 * @param curPage
	 * @return
	 */
	@RequestMapping("/SCLogin/PCdealRecord.do")
	public ModelAndView dealRecord(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("DealRecordController.dealRecord  start");
		Map out = new HashMap();
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String projectType = request.getParameter("projectType");//1:购买  2:退款   空:所有
		String dateType = request.getParameter("dateType");
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
		if(!StringUtils.isEmpty(beginDate)
				&&!StringUtils.isEmpty(endDate)
				&&!(DateUtils.isValidDate(beginDate, "yyyy-MM-dd")
				&&DateUtils.isValidDate(endDate, "yyyy-MM-dd"))){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
	    int pageSize =10;//显示条数
	    int PageCount =(curPage-1)*pageSize;//起始位置
	    Map reqMap = new HashMap();
	    reqMap.put("oidUserId",oidUserId);
	    reqMap.put("beginDate",beginDate);
	    reqMap.put("endDate",endDate);
	    reqMap.put("projectType",projectType);
	    reqMap.put("dateType",dateType);
	    reqMap.put("curPage",PageCount);
	    reqMap.put("pageSize",pageSize);
	    int dealRecordtCnt = dealRecordService.getDealRecordCnt(reqMap);
	    List<DealRecordDto> dealRecordList = dealRecordService.getDealRecordList(reqMap);
		out.put("beginDate", beginDate);
		out.put("endDate", endDate);
		out.put("projectType", projectType);
		out.put("curPage", curPage);
		if(dealRecordtCnt == 0){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "暂无记录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		int dealRecordtPage = (dealRecordtCnt-1)/pageSize +1;//总页数
		out.put("dealRecordtPage", Integer.toString(dealRecordtPage));
		out.put("dealRecordtCnt", dealRecordtCnt);
		out.put("dealRecordList", dealRecordList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
        return null;
	}

}
