package com.zhongchou.common.loginOut.controller.verification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.ISmsService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class verificationCodeController extends BaseController {
	String[] sendStr = new String[]{"1","2","3","4","5","6","7"};
	String[] sendStrBuy = new String[]{"6","7"};
	/*6 认购申购
	  7 撤单
	*/
	@Autowired
	private ISmsService smsService;
	@Autowired
	private IBankService bankService;
		 /**
		 * 登录外验证短信验证码
		 * @param userPhone 手机号
		 * @param phoneCode 验证码
		 * @param sendType 	类型
		 * @return 无
		 */
		@RequestMapping(value = "loginOut/loginOutVerifiedCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView verifyCode(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone,String phoneCode,String sendType){
			Map out = new HashMap();

			if(StringUtils.isEmpty(userPhone)
					||!StringUtils.isMobileNum(userPhone)
					||StringUtils.isEmpty(phoneCode)
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
			//短信验证码1:验证码错误   2：验证码超时  3：验证码错误次数超限
			int retmsg = VerificationUtil.checkInput(smsService,userPhone,phoneCode,sendType);
			if(retmsg==2){
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, "验证码超时");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==1){
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "验证码错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==3){
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "验证码错误次数超限");
				JsonUtil.writeJson(response,out);
				return null;
			}
			this.setSessionObj(Constants.LOGIN_OUT_VERIFY_CODE, Constants.LOGIN_OUT_VERIFY_CODE_SECCESS);
			out.put(Constants.RET_CODE, "000");
			out.put(Constants.RET_MSG, "success");
			JsonUtil.writeJson(response,out);
			return null;
		}

		 /**
		 * 登录内功能掉登录外验证短信验证码
		 * (修改手机号)
		 * @param userPhone 手机号
		 * @param phoneCode 验证码
		 * @param sendType 	类型
		 * @return 无
		 */
		@RequestMapping(value = "SCLogin/verifiedCodeUpdMoblie.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView loginOutVerifiedCode(HttpServletRequest request,HttpServletResponse response,Model model,String userPhone,String phoneCode,String sendType){
			Map out = new HashMap();

			if(StringUtils.isEmpty(userPhone)
					||!StringUtils.isMobileNum(userPhone)
					||StringUtils.isEmpty(phoneCode)
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
			//短信验证码1:验证码错误   2：验证码超时  3：验证码错误次数超限
			int retmsg = VerificationUtil.checkInput(smsService,userPhone,phoneCode,sendType);
			if(retmsg==2){
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, "验证码超时");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==1){
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "验证码错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==3){
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "验证码错误次数超限");
				JsonUtil.writeJson(response,out);
				return null;
			}

			this.setSessionObj(Constants.LOGIN_OUT_VERIFY_CODE, Constants.LOGIN_OUT_VERIFY_CODE_SECCESS);
			this.setSessionObj("updUserPhone",userPhone);
			out.put(Constants.RET_CODE, "000");
			out.put(Constants.RET_MSG, "success");
			JsonUtil.writeJson(response,out);
			return null;
		}

		 /**
		 * 登录内验证短信验证码
		 * @param userPhone 手机号
		 * @param phoneCode 验证码
		 * @param sendType 	类型
		 * @return 无
		 */
		@RequestMapping(value = "SCLogin/loginVerifiedCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView loginVerifyCode(HttpServletRequest request,HttpServletResponse response,Model model,String phoneCode,String sendType){
			Map out = new HashMap();
			UserDto user = getUser();
			String userPhone = user.getMobile();
			if(StringUtils.isEmpty(userPhone)
					||!StringUtils.isMobileNum(userPhone)
					||StringUtils.isEmpty(phoneCode)
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
			//短信验证码1:验证码错误   2：验证码超时  3：验证码错误次数超限
			int retmsg = VerificationUtil.checkInput(smsService,userPhone,phoneCode,sendType);
			if(retmsg==2){
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, "验证码超时");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==1){
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "验证码错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==3){
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "验证码错误次数超限");
				JsonUtil.writeJson(response,out);
				return null;
			}

			this.setSessionObj(Constants.LOGIN_VERIFY_CODE, Constants.LOGIN_VERIFY_CODE_SECCESS);
			out.put(Constants.RET_CODE, "000");
			out.put(Constants.RET_MSG, "success");
			JsonUtil.writeJson(response,out);
			return null;
		}
		
		 /**
		 * 登录内验证短信验证码(购买和撤单)
		 * @param userPhone 手机号
		 * @param phoneCode 验证码
		 * @param sendType 	类型
		 * @return 无
		 */
		@RequestMapping(value = "SCLogin/loginVerifiedCodeToBuy.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView loginVerifyCodeToBuy(HttpServletRequest request,HttpServletResponse response,Model model,String phoneCode,String sendType){
			Map out = new HashMap();
			UserDto user = getUser();
			if(StringUtils.isEmpty(phoneCode)
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
			//查询用户绑卡手机号
			//用户绑定的银行卡信息
			Map bankListMap = bankService.selBankMap(user.getOidUserId());
			if(StringUtils.isEmpty((String)bankListMap.get("mobile"))){
				out.put(Constants.RET_CODE, "006");
				out.put(Constants.RET_MSG, "请先绑卡");
				JsonUtil.writeJson(response,out);
				return null;
			}
			//短信验证码1:验证码错误   2：验证码超时  3：验证码错误次数超限
			int retmsg = VerificationUtil.checkInput(smsService,(String)bankListMap.get("mobile"),phoneCode,sendType);
			if(retmsg==2){
				out.put(Constants.RET_CODE, "003");
				out.put(Constants.RET_MSG, "验证码超时");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==1){
				out.put(Constants.RET_CODE, "004");
				out.put(Constants.RET_MSG, "验证码错误");
				JsonUtil.writeJson(response,out);
				return null;
			}else if(retmsg==3){
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, "验证码错误次数超限");
				JsonUtil.writeJson(response,out);
				return null;
			}

			this.setSessionObj(Constants.LOGIN_VERIFY_CODE_BUY, Constants.LOGIN_VERIFY_CODE_BUY_SECCESS);
			out.put(Constants.RET_CODE, "000");
			out.put(Constants.RET_MSG, "success");
			JsonUtil.writeJson(response,out);
			return null;
		}
}
