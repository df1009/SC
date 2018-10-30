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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserTenderDto;
import com.zhongchou.common.service.IPCUserTenderService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

/**
 * 用户中心-订单管理
 */
@Controller
public class PCMyTenderController extends BaseController {

	Logger logger=Logger.getLogger(PCMyTenderController.class);

	@Autowired
	private IPCUserTenderService userTenderService;

	String privateKey = Config.getString("RSAPrivate");
	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的list数组数据
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * 每页面的条数  pageSize
	 * 当前页数          curPage
	 * @return
	 */
	@RequestMapping(value = "/SCLogin/PCgetMyTenderList.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getPCMyTenderList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		logger.info("MyTenderController.getMyTenderList  start");

		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		String projectName = request.getParameter("projectName");
/*			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");*/
		String projectState = request.getParameter("projectState");//空:全部  1:支付成功   3:认购成功  2:购买成功  0:退款订单
		//String StrsumNum = request.getParameter("sumNum");
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
		int pageSize = 10;
	    int PageCount =(curPage-1)*pageSize;//起始位置
	    Map selTenderMap = new HashMap();
	    if(StringUtil.isAllNumber(projectName)){
	    	selTenderMap.put("orderId", projectName);
	    }else{
	    	selTenderMap.put("projectName", projectName);
	    }
		selTenderMap.put("oidUserId", oidUserId);
		selTenderMap.put("projectState", projectState);
		selTenderMap.put("pageSize", pageSize);
		selTenderMap.put("PageCount", PageCount);
		int myTenderListCnt = userTenderService.getUserTenderCnt(selTenderMap);
		int pageCount = (myTenderListCnt-1)/pageSize+1;//总页数
		List<UserTenderDto> userTenders = userTenderService.getUserTenders(selTenderMap);
		out.put("pageCount", pageCount);
		out.put("userTenders", userTenders);
		out.put("curPage", curPage);
		if(myTenderListCnt == 0){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "暂无记录");
			JsonUtil.writeJson(response,out);
			return null;
		}
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);

        return null;

	}
	
	//订单详情
	@RequestMapping("/SCLogin/PCorderDetails.do")
	public ModelAndView orderDetails(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyTenderController.orderDetails  start");

		Map<String,Object> out = new HashMap<String,Object>();

		String orderId = request.getParameter("orderId");//前台取的订单Id

		if(StringUtils.isEmpty(orderId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		UserDto userDto = getUser();
		String oidUserId = userDto.getOidUserId();
		Map selTenderMap = new HashMap();
		selTenderMap.put("oidUserId", oidUserId);
		selTenderMap.put("orderId", orderId);
		selTenderMap.put("pageSize", 5);
		selTenderMap.put("PageCount", 0);
	   //订单详情
		Map<String,Object> orderInfoMap = userTenderService.getOrderdetailInfo(selTenderMap);

		if(orderInfoMap==null){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "查不到信息");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//List produtInvestor = userTenderService.selProductInvestor(projectCode);
		orderInfoMap.put("userName", userDto.getUserRealName());//投资人姓名
		orderInfoMap.put("maskIdCard", StringUtils.newEncryptIdCard(userDto.getIdCard()));//投资人身份证
		out.put("orderInfoMap", orderInfoMap);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
	//删除关闭和取消订单的订单
	@RequestMapping("/SCLogin/delCloseOrder.do")
	public ModelAndView delCloseOrder(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyTenderController.delCloseOrder  start");
		Map<String,Object> out = new HashMap<String,Object>();
		String orderId = request.getParameter("orderId");//前台取的订单Id
		String type = request.getParameter("type");//1：取消  其他 删除
		UserDto userDto = getUser();
		if("1".equals(type)){
			if(userTenderService.updCloseOrder(orderId, userDto.getOidUserId())){
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			}else{
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "取消失败");
			}
		}else{
			if(userTenderService.delCloseOrder(orderId, userDto.getOidUserId())){
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			}else{
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "删除失败");
			}
		}
		
		JsonUtil.writeJson(response,out);
		return null;
	}
	
	//查询投资协议
	@RequestMapping("/SCLogin/selInvestmentAgreement.do")
	public ModelAndView selInvestmentAgreement(HttpServletRequest request,HttpServletResponse response,Model model){
		logger.info("MyTenderController.selInvestmentAgreement  start");
		Map<String,Object> out = new HashMap<String,Object>();
		String orderId = request.getParameter("orderId");//前台取的订单Id
		UserDto userDto = getUser();
		Map selTenderMap = new HashMap();
		selTenderMap.put("oidUserId", userDto.getOidUserId());
		selTenderMap.put("orderId", orderId);
		selTenderMap.put("pageSize", 5);
		selTenderMap.put("PageCount", 0);
		if(!StringUtil.isAllNumber(orderId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "orderId有误");
		}
	   //订单详情
		Map<String,Object> orderInfoMap = userTenderService.getOrderdetailInfo(selTenderMap);
		if(!orderInfoMap.isEmpty()
				&&"3".equals(orderInfoMap.get("tenderStatus"))
				&&"SV4468".equals(orderInfoMap.get("projectCode"))//只有龙臻食品有协议
				){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			out.put("confirmSuccessDate", orderInfoMap.get("confirmSuccessDate1"));//认购成功时间（购买隔天同步中证认购状态）
			out.put("userName", userDto.getUserRealName());//真实姓名
			out.put("idCard", userDto.getIdCard());//身份证号
			out.put("tenderAmount", orderInfoMap.get("tenderAmount"));//投资金额
			out.put("liquidatedDamages", ConvUtils.convToDouble(orderInfoMap.get("tenderAmount"))/10000*0.1);//违约金
			out.put("mobile", userDto.getMobile());//手机号
			out.put("signAddress", "壹盐双创");//签署地点
			/*out.put("accountName", "大连龙臻食品有限公司");//户名
			out.put("openBank", "中国建设银行上海第五支行");//开户行信息
			out.put("bankName", "31050166360000002217");//银行账号
*/			out.put("agreementId", ConvUtils.convToString(orderInfoMap.get("projectCode"))
					+ConvUtils.convToString(orderInfoMap.get("confirmSuccessDate"))
					+userDto.getOidUserId());//协议编号
		}else{
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "系统异常");
		}
		JsonUtil.writeJson(response,out);
		return null;
	}
}
