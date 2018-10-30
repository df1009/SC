package com.zhongchou.common.loginOut.controller.verification;

import java.util.Arrays;
import java.util.HashMap;
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
import com.zhongchou.common.dto.SendSmsDto;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.enums.SmsContentType;
import com.zhongchou.common.enums.SmsType;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.IRegistService;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class getVerificationCodeController extends BaseController {
	@Autowired
	private IBankService bankService;
	Logger logger=Logger.getLogger(getVerificationCodeController.class);
	String[] sendStr = new String[]{"1","2","3","4","5","6","7"};
	/*
	1：个人注册
	2：我要融资
	3：修改手机号
	4：手机认证找回密码
	5：修改登录密码
	
*/
	
	String[] sendStrBuy = new String[]{"6","7"};
	/*6 认购申购
	  7 撤单
	*/
	
	
	/*String[] sendSmsType = new String[]{"02","03","04","05","06","07","08"};
	02 登陆密码重置
	03 支付密码重置
	04 充值
	05 提现
	06 认购申购
	07 赎回
	08 撤单*/


	@Autowired
	private IRegistService registService;

	@Autowired
	private IUserManageService userManageService;

	@Autowired
	private ISmsService smsService;

	    /**
		 * 登录外获取短信验证码
		 * @param userPhone 手机号
		 * @return 无
		 */
		@RequestMapping(value = "loginOut/getVerifyCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView getVerifycode(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone,String sendType){
			logger.info("getVerificationCodeController.getVerifyCode  start");
			Map<String,Object> out = new HashMap<String,Object>();
			if(StringUtils.isEmpty(userPhone)
					||!StringUtils.isMobileNum(userPhone)
					||StringUtils.isEmpty(sendType)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			if(Arrays.binarySearch(sendStr,sendType)<0){
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "短信类型错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);
			if("1".equals(sendType)||"3".equals(sendType)){//注册时,修改手机号不校验手机号是否存在
				if (!existFlag) {
					out.put(Constants.RET_CODE, "004");
					out.put(Constants.RET_MSG, "手机号已存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}else if("2".equals(sendType)){
				existFlag =  VerificationUtil.checkFinanPhoneInput(userManageService,userPhone);//检查融资申请是否已存在此手机号
				if (!existFlag) {
					out.put(Constants.RET_CODE, "004");
					out.put(Constants.RET_MSG, "手机号已存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}else{
				if (existFlag) {
					out.put(Constants.RET_CODE, "003");
					out.put(Constants.RET_MSG, "手机号不存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			SendSmsDto sendSms = new SendSmsDto();
			sendSms.setUserId("");
			sendSms.setSmsContentType(SmsContentType.IDENTITY);
			sendSms.setSmsType(SmsType.TEXT);
			sendSms.setPhone(userPhone);
			sendSms.setOperateType(sendType);

			boolean isSend = registService.sendSms(sendSms);
			if (isSend) {
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			} else {
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "验证码发送失败");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}

	    /**
			 * 登录内获取短信验证码
			 * @param userPhone 手机号
			 * @return 无
			 */
			@RequestMapping(value = "SCLogin/getLoginVerifyCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
			public ModelAndView getLoginVerifyCode(HttpServletRequest request,HttpServletResponse response,Model model,String sendType){
				logger.info("getVerificationCodeController.getLoginVerifyCode  start");
				Map<String,Object> out = new HashMap<String,Object>();
				UserDto user = getUser();
				String userPhone = user.getMobile();
				if(Arrays.binarySearch(sendStr,sendType)<0){
					out.put(Constants.RET_CODE, "001");
					out.put(Constants.RET_MSG, "短信类型错误");
					JsonUtil.writeJson(response,out);
					return null;
				}
				SendSmsDto sendSms = new SendSmsDto();
				sendSms.setUserId("");
				sendSms.setSmsContentType(SmsContentType.IDENTITY);
				sendSms.setSmsType(SmsType.TEXT);
				sendSms.setPhone(userPhone);
				sendSms.setOperateType(sendType);

				boolean isSend = registService.sendSms(sendSms);
				if (isSend) {
					out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
					out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
					JsonUtil.writeJson(response,out);
					return null;
				} else {
					out.put(Constants.RET_CODE, "002");
					out.put(Constants.RET_MSG, "验证码发送失败");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}

		 /**
		 * 登录外获取短信验证码
		 * @param userPhone 手机号
		 * @param imgCode  图形验证码
		 * @return 无
		 */
		@RequestMapping(value = "loginOut/getVerifyCodeToImg.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView getVerifyCodeToImg(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone,String imgCode,String sendType){
			logger.info("getVerificationCodeController.getVerifyCodeToImg  start");
			Map<String,Object> out = new HashMap<String,Object>();
			if(StringUtils.isEmpty(userPhone)
					||!StringUtils.isMobileNum(userPhone)
					||StringUtils.isEmpty(imgCode)
					||StringUtils.isEmpty(sendType)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			if(Arrays.binarySearch(sendStr,sendType)<0){
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "短信类型错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			//图形验证码
			if(!checkImgCode(imgCode,true)){//图形验证码session不清除
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "图形验证码错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);
			if("1".equals(sendType)||"3".equals(sendType)){//注册时,修改手机号不校验手机号是否存在
				if (!existFlag) {
					out.put(Constants.RET_CODE, "004");
					out.put(Constants.RET_MSG, "手机号已存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}else{
				if (existFlag) {
					out.put(Constants.RET_CODE, "003");
					out.put(Constants.RET_MSG, "手机号不存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			SendSmsDto sendSms = new SendSmsDto();
			sendSms.setUserId("");
			sendSms.setSmsContentType(SmsContentType.IDENTITY);
			sendSms.setSmsType(SmsType.TEXT);
			sendSms.setPhone(userPhone);
			sendSms.setOperateType(sendType);

			boolean isSend = registService.sendSms(sendSms);
			if (isSend) {
				//清除图形验证码
				this.clearSessionObj(Constants.VERIFY_CODE);
/*				request.getSession().setAttribute("VerifyCodeStep", "1");//防跨步
*/				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			} else {
				out.put(Constants.RET_CODE, "006");
				out.put(Constants.RET_MSG, "短信发送失败");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}

		/**
		 * 登录外获取短信验证码
		 * @param userPhone 手机号
		 * @param imgCode  图形验证码
		 * @return 无
		 */
		@RequestMapping(value = "loginOut/getVerifyAppCodeToImg.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView getVerifyAppCodeToImg(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone,String sendType){
			logger.info("getVerificationCodeController.getVerifyCodeToImg  start");
			Map<String,Object> out = new HashMap<String,Object>();
			if(StringUtils.isEmpty(userPhone)
					||!StringUtils.isMobileNum(userPhone)
					||StringUtils.isEmpty(sendType)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			if(Arrays.binarySearch(sendStr,sendType)<0){
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "短信类型错误");
				JsonUtil.writeJson(response,out);
				return null;
			}

			boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,userPhone);
			if("1".equals(sendType)||"3".equals(sendType)){//注册时,修改手机号不校验手机号是否存在
				if (!existFlag) {
					out.put(Constants.RET_CODE, "004");
					out.put(Constants.RET_MSG, "手机号已存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}else{
				if (existFlag) {
					out.put(Constants.RET_CODE, "003");
					out.put(Constants.RET_MSG, "手机号不存在");
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
			SendSmsDto sendSms = new SendSmsDto();
			sendSms.setUserId("");
			sendSms.setSmsContentType(SmsContentType.IDENTITY);
			sendSms.setSmsType(SmsType.TEXT);
			sendSms.setPhone(userPhone);
			sendSms.setOperateType(sendType);

			boolean isSend = registService.sendSms(sendSms);
			if (isSend) {
				//清除图形验证码
				this.clearSessionObj(Constants.VERIFY_CODE);
/*				request.getSession().setAttribute("VerifyCodeStep", "1");//防跨步
*/				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			} else {
				out.put(Constants.RET_CODE, "006");
				out.put(Constants.RET_MSG, "短信发送失败");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
		
		/**
		 * 登录内获取短信验证码(购买和撤单)
		 * @param userPhone 手机号
		 * @return 无
		 */
		@RequestMapping(value = "SCLogin/getLoginVerifyCodeToBuy.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView getLoginVerifyCodeToBuy(HttpServletRequest request,HttpServletResponse response,Model model,String sendType){
			logger.info("getVerificationCodeController.getLoginVerifyCode  start");
			Map<String,Object> out = new HashMap<String,Object>();
			UserDto user = getUser();
			if(Arrays.binarySearch(sendStrBuy,sendType)<0){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "短信类型错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			//查询用户绑卡手机号
			//用户绑定的银行卡信息
			Map bankListMap = bankService.selBankMap(user.getOidUserId());
			if(StringUtils.isEmpty((String)bankListMap.get("mobile"))){
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, "请先绑定银行卡");
				JsonUtil.writeJson(response,out);
				return null;
			}
			SendSmsDto sendSms = new SendSmsDto();
			sendSms.setUserId("");
			sendSms.setSmsContentType(SmsContentType.IDENTITY);
			sendSms.setSmsType(SmsType.TEXT);
			sendSms.setPhone((String)bankListMap.get("mobile"));
			sendSms.setOperateType(sendType);
			boolean isSend = registService.sendSms(sendSms);
			if (isSend) {
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			} else {
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "验证码发送失败");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
		
		/**
		 * 中证发短信验证码
		 * @return
		 *//*
		@RequestMapping(value = "/SCLogin/sendShortMessage.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView sendShortMessage(HttpServletRequest request,HttpServletResponse response,Model model){
			Map out = new HashMap();
			String sendType = request.getParameter("sendType");
			if(Arrays.binarySearch(sendSmsType,sendType)<0){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "短信类型错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
			UserDto userDto = getUser();
			String mobileNum = userDto.getMobile();
			String loginId = userDto.getOidUserId();
			 Map<String,Object> sendSmd = smsService.sendShortMessage(mobileNum,loginId,sendType);//
			 if("091018".equals(sendSmd.get("rsp_code").toString())){
				 out.put(Constants.RET_CODE, "002");
				 out.put(Constants.RET_MSG, "用户未绑定手机号");
				 JsonUtil.writeJson(response,out);
				 return null;
			 }
			 if(!"000000".equals(sendSmd.get("rsp_code").toString())){
				 out.put(Constants.RET_CODE, "001");
				 out.put(Constants.RET_MSG, "验证码发送失败");
				 JsonUtil.writeJson(response,out);
				 return null;
			 }
			 this.setSessionObj("shortMessageReqSsn",sendSmd.get("reqSsn").toString() );
			 out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			 out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			 JsonUtil.writeJson(response,out);
	         return null;
		}*/

}
