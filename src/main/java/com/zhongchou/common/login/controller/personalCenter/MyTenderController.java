package com.zhongchou.common.login.controller.personalCenter;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.dto.UserTenderDto;
import com.zhongchou.common.service.ILoginService;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.StringUtil;

/**
 * 用户中心-订单管理
 */
@Controller
public class MyTenderController extends BaseController {

	Logger logger=Logger.getLogger(MyTenderController.class);

	@Autowired
	private IUserTenderService userTenderService;

	@Autowired
	private IMyProjectService myProjectService;

	@Autowired
	private ILoginService loginService;

	@Autowired
	private ProductService productService;

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
		@RequestMapping(value = "/SCLogin/getMyTenderList.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView getMyTenderList(HttpServletRequest request,HttpServletResponse response,Model model){
			Map out = new HashMap();
			logger.info("MyTenderController.getMyTenderList  start");

			UserDto userDto = getUser();
			String oidUserId = userDto.getOidUserId();
			String projectName = request.getParameter("projectName");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			String projectState = request.getParameter("projectState");
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
			if(isApp()){
				selTenderMap.put("pageSize", pageSize);
				selTenderMap.put("PageCount", PageCount);
				selTenderMap.put("oidUserId", oidUserId);
				selTenderMap.put("projectName", projectName);
			}else{
				selTenderMap.put("oidUserId", oidUserId);
				selTenderMap.put("projectName", projectName);
				selTenderMap.put("beginDate", beginDate);
				selTenderMap.put("endDate", endDate);
				selTenderMap.put("projectState", projectState);
				selTenderMap.put("pageSize", pageSize);
				selTenderMap.put("PageCount", PageCount);
			}
			int myTenderListCnt = userTenderService.getUserTenderCnt(selTenderMap);
			int pageCount = (myTenderListCnt-1)/pageSize+1;
			List<UserTenderDto> userTenders = userTenderService.getUserTenders(selTenderMap);
			out.put("MyTenderListCnt", pageCount);
			out.put("userTenders", userTenders);
			out.put("oidUserId", oidUserId);
			out.put("projectName", projectName);
			out.put("beginDate", beginDate);
			out.put("endDate", endDate);
			out.put("projectState", projectState);
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
		@RequestMapping("/SCLogin/orderDetails.do")
		public ModelAndView orderDetails(HttpServletRequest request,HttpServletResponse response,Model model){
			logger.info("MyTenderController.orderDetails  start");

			Map<String,Object> out = new HashMap<String,Object>();

			String tenderId = request.getParameter("tenderId");//前台取的订单Id
			String projectCode = request.getParameter("projectCode");//前台取项目code

			if(StringUtils.isEmpty(tenderId)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "参数有误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			UserDto userDto = getUser();
			String oidUserId = userDto.getOidUserId();
			//当前产品基本信息
			Map<String,Object> productInfoMap = userTenderService.getProductinfo(tenderId,oidUserId);

			if(productInfoMap==null){
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "查不到信息");
				JsonUtil.writeJson(response,out);
				return null;
			}

			//当前产品详细信息
			Map productDetailMap = userTenderService.selProductDetailList(projectCode,"2");
			if(!"不限".equals(productDetailMap.get("salesQuota"))){//目标金额不为0
				productDetailMap.put("salesQuota", Double.parseDouble((String)productDetailMap.get("salesQuota"))/10000);
			}
			if(productDetailMap==null){
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "产品id有误");
				JsonUtil.writeJson(response,out);
				return null;
			}

			List produtInvestor = userTenderService.selProductInvestor(projectCode);

			out.put("productInfoMap", productInfoMap);
			out.put("productDetailMap", productDetailMap);
			out.put("produtInvestor", produtInvestor);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}



		/**撤单
		 * @param projectCode 项目ID
		 * @param reqSsn 验证码流水
		 * @param checkCode 验证码
		 * @param payPwd 密码
		 * @param beginDate 开始时间
		 * @param endDate 结束时间
		 * @return
		 * @throws Exception
		 */
		@RequestMapping("/SCLogin/returnOrder.do")
		public ModelAndView returnOrder(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
			logger.info("MyTenderController.returnOrder  start");
			Map out = new HashMap();
			String coolingPeriodTime = "150000";//HHmmss
			if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
				coolingPeriodTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
			}
			String endTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+coolingPeriodTime;
			UserDto userDto = getUser();
			String projectCode = request.getParameter("projectCode");//项目id
			String tenderId = request.getParameter("tenderId");//投资id
/*			String reqSsn =(String) getSessionObj("shortMessageReqSsn");
			String checkCode = request.getParameter("checkCode");//验证码
*/			if(!checkLoginSmsCodeToBuy()){
				return outparamMethod(response, out,"010","请先发送短信验证码");
			}
			//解密密码
			String resortPwd = null;
			String payPwd = request.getParameter("payPwd");//交易密码
			try {
				resortPwd = RSAUtil.decryptByPrivate(privateKey,payPwd);
			} catch (Exception e) {
				e.printStackTrace();
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "密码解密有误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			String beginDate = DateUtils.convertDate2String(new Date(), "yyyy-MM-dd");
			String endDate = beginDate;
			String loginId = userDto.getOidUserId();
			//当前产品基本信息
			Map<String,Object> productInfoMap = userTenderService.getProductinfo(tenderId,loginId);
			if(productInfoMap.isEmpty()){
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "只能操作本人项目");
				JsonUtil.writeJson(response,out);
				return null;
			}else{
				String insDateDetal = (String)productInfoMap.get("insDate");//购买时间
				Date insDate = DateUtils.convertString2Date(insDateDetal,"yyyyMMddHHmmss");
				if(insDate.after(DateUtils.convertString2Date(endTransactionDate,"yyyyMMddHHmmss"))){
					out.put(Constants.RET_CODE, "003");
					out.put(Constants.RET_MSG, "该项目不在冷静期");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			Map reqMap = new HashMap();
			reqMap.put("projectCode", projectCode);
			/*reqMap.put("reqSsn", reqSsn);
			reqMap.put("checkCode", checkCode);*/
			reqMap.put("payPwd", resortPwd);
			reqMap.put("loginId", loginId);
			reqMap.put("beginDate", beginDate);
			reqMap.put("endDate", endDate);
			reqMap.put("tenderId", tenderId);
			reqMap.put("userIp", getRemoteAddr());
			reqMap.put("projectName", productInfoMap.get("projectName"));
			 Map revokeOrder = userTenderService.returnOrder(reqMap);//以订单编号取信息
			 if(!"000000".equals(revokeOrder.get("rsp_code"))){
				 /*if("092180".equals(revokeOrder.get("rsp_code"))){
					 out.put(Constants.RET_CODE, "005");
					 out.put(Constants.RET_MSG, "请先发送验证码");
					 JsonUtil.writeJson(response,out);
					 return null;
				 }*/
				 out.put(Constants.RET_CODE, "001");
				 out.put(Constants.RET_MSG, revokeOrder.get("rsp_desc"));
				 JsonUtil.writeJson(response,out);
				 return null;
			 }
			 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			 /*clearSessionObj("shortMessageReqSsn");*/
			 this.clearSessionObj(Constants.LOGIN_VERIFY_CODE_BUY);
			 JsonUtil.writeJson(response,out);
	         return null;
		}

		/**
		 * 模糊查询用户投资信息
		 * @param
		 */
		@RequestMapping("/SCLogin/likeUserTender.do")
		public ModelAndView likeUserTender(HttpServletRequest request,HttpServletResponse response,Model model){
			UserDto userDto = getUser();
			String oidUserId = userDto.getOidUserId();
			String proNm = request.getParameter("proName");
			Map out = new HashMap();
			List tenderList =userTenderService.likeTender(oidUserId,proNm);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			out.put("tenderList",tenderList);
			JsonUtil.writeJson(response,out);
	        return null;
		}
}
