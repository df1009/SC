package com.zhongchou.common.login.controller.personalCenter;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import sun.misc.BASE64Decoder;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.ILoginService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.RSAUtil;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.util.VerificationUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;


@Controller
public class PersonalInfo extends BaseController {

	private static final long serialVersionUID = 1L;
	private ServletFileUpload upload;
	private final long MAXSize = 4194304*2L;//4*2MB
	private String filedir=null;

	Logger logger=Logger.getLogger(PersonalInfo.class);

	String privateKey = Config.getString("RSAPrivate");
	//用户没有上传头像时的默认头像
	String defaultAvatarurl = Config.getString("defaultAvatarurl");
	String HeadpicUrl = Config.getString("HeadpicUrl");//头像文件路径
	String registUrl = Config.getString("registUrl");//壹盐双创域名

	@Autowired
	private IUserService userService;

	@Autowired
	private ILoginService loginService;

	@Autowired
	private IUserManageService userManageService;
	@Autowired
	private IBankService bankService;

	/**
	 * 取用户基本信息
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/queryPersonalInfo.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView queryPersonalInfo(HttpServletRequest request,HttpServletResponse response,Model model) {
		logger.info("PersonalInfo.queryPersonalInfo  start");

		Map<String,Object> out = new HashMap<String,Object>();

		UserDto sessionUserDto = getUser();
		UserDto user = userService.getUserInfo(sessionUserDto.getOidUserId());
		Map<String,Object> personalInfo = new HashMap<String,Object>();
		personalInfo.put("mobile", StringUtils.encryptMobile(user.getMobile()));
		personalInfo.put("email", StringUtil.encryptEmail(user.getEmail()));
		personalInfo.put("userPwd", user.getUserPwd());
		personalInfo.put("userPayPwd", user.getUserPayPwd());
		personalInfo.put("riskLvl", user.getRiskLvl());
		personalInfo.put("userIconFileId", /*registUrl+"/"+*/user.getUserIconFileId());
		personalInfo.put("userName", user.getUserRealName());
		personalInfo.put("idCard", StringUtil.maskIdCard(user.getIdCard()));
		personalInfo.put("investmentFlg", user.getInvestmentFlg());
		if(StringUtils.isEmpty(user.getUserIconFileId())){
			personalInfo.put("userIconFileId", defaultAvatarurl);
		}
		out.put("user", personalInfo);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	/**
	 * 保存头像
	 * @param headPortrait 头像图片地址
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/saveHeadPortrait.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveHeadPortrait(HttpServletRequest request,HttpServletResponse response,Model model,String headPortrait) {
		logger.info("PersonalInfo.saveHeadPortrait  start");
		Map<String,Object> out = new HashMap<String,Object>();

		if(StringUtils.isEmpty(headPortrait)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "头像地址为空");
			JsonUtil.writeJson(response,out);
			return null;
		}

		UserDto sessionUserDto = getUser();

		if(userService.updUserIconFileId(sessionUserDto.getOidUserId(),headPortrait)){

			sessionUserDto.setUserIconFileId(headPortrait);
			setUser(sessionUserDto);

			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "保存失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
	}

	/**
	 * 保存新手机号
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/saveNewUserPhone.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveNewUserPhone(HttpServletRequest request,HttpServletResponse response,Model model,String newUserPhone) {
		logger.info("PersonalInfo.saveNewUserPhone  start");
		Map<String,Object> out = new HashMap<String,Object>();


		if(isApp()){
			newUserPhone = (String)getSessionObj("updUserPhone");
		}else{
			if (StringUtils.isEmpty(newUserPhone)||!StringUtils.isMobileNum(newUserPhone)) {
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "格式错误");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
		boolean existFlag =  VerificationUtil.checkPhoneInput(userManageService,newUserPhone);
		if (!existFlag) {
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "手机号已存在");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(!checkLoginSmsCode(true)){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "未验证原手机号验证码");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(!checkSmsCode(true)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "请先验证短信动态码");
			JsonUtil.writeJson(response,out);
			return null;
		}

		UserDto sessionUserDto = getUser();
		//用户信息更改记录
		Map paramMap = new HashMap();
		paramMap.put("beforeChange", sessionUserDto.getMobile());
		paramMap.put("afterChange", newUserPhone);
		paramMap.put("count", "用户更改手机号");
		paramMap.put("loginId", sessionUserDto.getOidUserId());
		if(userService.updNewUserPhone(sessionUserDto.getOidUserId(),newUserPhone,paramMap)){

			sessionUserDto.setMobile(newUserPhone);
			setUser(sessionUserDto);
			this.clearSessionObj(Constants.LOGIN_VERIFY_CODE);
			this.clearSessionObj(Constants.LOGIN_OUT_VERIFY_CODE);
			this.clearSessionObj("updUserPhone");
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "保存失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
	}

	/**
	 * 修改登录密码
	 * @param 	password  密码
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/updRetrievePwd.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updRetrievePwd(HttpServletRequest request,HttpServletResponse response,Model model,String password,String newPassword){

		UserDto userDto = getUser();

		Map<String,Object> out = new HashMap<String,Object>();
		String resortPwd = null;
		String newResortPwd = null;
		if(StringUtils.isEmpty(password)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "原密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(StringUtils.isEmpty(newPassword)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "新密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		/*if(!checkLoginSmsCode(true)){
			out.put(Constants.RET_CODE, "008");
			out.put(Constants.RET_MSG, "请先验证短信动态码");
			JsonUtil.writeJson(response,out);
			return null;
		}*/
		try {
			resortPwd = RSAUtil.decryptByPrivate(privateKey,password);
			newResortPwd = RSAUtil.decryptByPrivate(privateKey,newPassword);
		} catch (UnsupportedEncodingException e) {
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "密码解密有误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		try {
			resortPwd = MD5.encode(userDto.getOidUserId() + resortPwd + userDto.getSalt());
		} catch (Exception e) {
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "密码加密错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(!resortPwd.equals(userDto.getUserPwd())){
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "原密码错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(!StringUtils.isPasswordStrength(newResortPwd)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "新密码格式不正确");
			JsonUtil.writeJson(response,out);
			return null;
		}

		UserDto user = new UserDto();
		user.setOidUserId(ConvUtils.convToString(userDto.getOidUserId()));

		if (StringUtils.isAsciiAlphaNumCharOnly(newResortPwd)) {
			user.setUserPwdStrength("1");
		} else {
			user.setUserPwdStrength("2");
		}

		try {
			newResortPwd = MD5.encode(userDto.getOidUserId() + newResortPwd + userDto.getSalt());
		} catch (Exception e) {
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "密码加密错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		user.setUserPwd(newResortPwd);

		boolean result = userManageService.updateUser(user);
		if (result) {
			this.clearSessionObj(Constants.LOGIN_VERIFY_CODE);
			userDto.setUserPwd(newResortPwd);
			setUser(userDto);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "007");
			out.put(Constants.RET_MSG, "密码修改失败");
			JsonUtil.writeJson(response,out);
			return null;
		}

	}

	/**
	 * 修改支付密码
	 * @param payPassword 支付密码
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/updNewPayPassword.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updNewPayPassword(HttpServletRequest request,HttpServletResponse response,Model model,String payPassword , String newPayPassword) {
		logger.info("PersonalInfo.saveNewPayPassword  start");

		Map<String,Object> out = new HashMap<String,Object>();

		String resortPwd;
		String newResortPwd;

		if(StringUtils.isEmpty(payPassword)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "原密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(StringUtils.isEmpty(newPayPassword)){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "新密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		try {
			payPassword = payPassword.replaceAll(" ","+");
			newPayPassword = newPayPassword.replaceAll(" ","+");
			resortPwd = RSAUtil.decryptByPrivate(privateKey,payPassword);
			newResortPwd = RSAUtil.decryptByPrivate(privateKey,newPayPassword);
		} catch (Exception e) {
			e.printStackTrace();
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "密码解密有误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		String regex = "^\\d{6}$";
		if(resortPwd.length() != 6 || !StringUtils.match(regex, resortPwd) ){
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "原支付密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(newResortPwd.length() != 6 || !StringUtils.match(regex, newResortPwd)){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "新支付密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		UserDto sessionUserDto = getUser();

		String userPayPwd = null;
		String newUserPayPwd = null;
		try {
			userPayPwd = MD5.encode(resortPwd);
			newUserPayPwd = MD5.encode(newResortPwd);
		} catch (Exception e) {
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "密码加密错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		if(!sessionUserDto.getUserPayPwd().equals(userPayPwd) ){
			out.put(Constants.RET_CODE, "007");
			out.put(Constants.RET_MSG, "原密码错误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		Map updPwd = userService.updPayPassword(sessionUserDto.getOidUserId(),newUserPayPwd,userPayPwd);
		if(updPwd==null){
			out.put(Constants.RET_CODE, "008");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			if("000000".equals(updPwd.get("rsp_code"))){

				sessionUserDto.setUserPayPwd(newUserPayPwd);
				setUser(sessionUserDto);

				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response,out);
				return null;
			}else{
				out.put(Constants.RET_CODE, "008");
				out.put(Constants.RET_MSG, StringUtil.splitDesc(ConvUtils.convToString(updPwd.get("rsp_desc"))));
				JsonUtil.writeJson(response,out);
				return null;

			}
		}
	}
	/**
	 * 重置支付密码获取短信
	 * @param user
	 * @return
	 */
	/*@RequestMapping(value = "/SCLogin/resetPaypwdCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView resetPaypwdCode(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> out = new HashMap<>();
		UserDto user = getUser();
		String yzmSsn = userService.getSmsCode(user);
		if(StringUtils.isEmpty(yzmSsn)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "获取短信失败");
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			request.getSession().setAttribute("yzmSsn", yzmSsn);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}
	}*/

	/**
	 * 忘记支付密码
	 * @param payPassword 支付密码
	 * @return 无
	 */
	@RequestMapping(value = "/SCLogin/saveNewPayPassword.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveNewPayPassword(HttpServletRequest request,HttpServletResponse response,String newPayPassword) {

		Map<String,Object> paramMap = new HashMap<String,Object>();
		Map<String,Object> out = new HashMap<String,Object>();
		String smsCode = (String)request.getParameter("smsCode");
		String yzmSsn = (String)this.getSessionObj("shortMessageReqSsn");
		String newResortPwd;

		if(StringUtils.isEmpty(newPayPassword)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "新密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		try {
			newResortPwd = RSAUtil.decryptByPrivate(privateKey,newPayPassword);
		} catch (Exception e) {
			e.printStackTrace();
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "密码解密有误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		String regex = "^\\d{6}$";

		if(!StringUtils.match(regex, newResortPwd)){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "新支付密码格式错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		UserDto sessionUserDto = getUser();

		String newUserPayPwd = null;
		try {
			newUserPayPwd = MD5.encode(newResortPwd);
		} catch (Exception e) {
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "密码加密错误");
			JsonUtil.writeJson(response,out);
			return null;
		}

		paramMap.put("newPwd", newUserPayPwd);
		paramMap.put("loginId", sessionUserDto.getOidUserId());
		paramMap.put("yzmSsn", yzmSsn);
		paramMap.put("yzmContent", smsCode);
		try {
			Map<String, Object> resetPayPassword = userService
					.resetPayPassword(paramMap);
			if("000000".equals((String)resetPayPassword.get("rsp_code"))){
				out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
				out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				JsonUtil.writeJson(response, out);
				return null;
			}else if("000011".equals((String)resetPayPassword.get("rsp_code"))){
				logger.info("用户："+sessionUserDto.getOidUserId()+"重置支付密码时更新user表支付密码失败");
			}else if("092180".equals(resetPayPassword.get("rsp_code"))){
				out.put(Constants.RET_CODE, "006");
				out.put(Constants.RET_MSG, "请先发送短信验证码");
				JsonUtil.writeJson(response, out);
				return null;
			}else{
				out.put(Constants.RET_CODE, "005");
				out.put(Constants.RET_MSG, resetPayPassword.get("rsp_desc"));
				JsonUtil.writeJson(response, out);
				return null;
			}

		} catch (Exception e) {
			out.put(Constants.RET_CODE, "006");
			out.put(Constants.RET_MSG, "重置支付密码失败");
			JsonUtil.writeJson(response, out);
			return null;
		}
		return null;
	}


	@RequestMapping(value = "/SCLogin/saveHeadpic.do", method = RequestMethod.POST)
	public ModelAndView saveHeadpic(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception {
		Map<String,Object> out = new HashMap<String,Object>();
		String Randoms = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String picName = DateUtils.convertDate2String(new Date(), "yyyyMMdd")+VerificationUtil.generatingRandomNum(Randoms,10)+"-";
		UserDto user = getUser();
		String imgStr = request.getParameter("file");
		imgStr = imgStr.replace(" ", "+");
		if (StringUtils.isEmpty(imgStr)){//图像数据为空
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response, out);
			return null;
		}
		BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try
        {
            //Base64解码
        	byte[] b = decoder.decodeBuffer(imgStr);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
         // HeadpicUrl = "D:/apache-tomcat-7.0.65/webapps/yilicai/app/data/avatar/";
            String imgFilePath = HeadpicUrl+picName+".jpg";//新生成的图片
            OutputStream outstr = new FileOutputStream(imgFilePath);
            outstr.write(b);
            outstr.flush();
            outstr.close();
            if(userService.updUserIconFileId(user.getOidUserId(),imgFilePath)){
            	user.setUserIconFileId(imgFilePath);
            	setUser(user);
            	out.put("path", imgFilePath);
    			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
    			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
    			JsonUtil.writeJson(response,out);
    			return null;
    		}else{
    			out.put(Constants.RET_CODE, "002");
    			out.put(Constants.RET_MSG, "保存失败");
    			JsonUtil.writeJson(response,out);
    			return null;
    		}
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response, out);
			return null;
        }
	}
	/**
	 * 实名及认证合格投资人
	 */
	@RequestMapping(value = "/SCLogin/realNameAuthenty.do", method = RequestMethod.POST)
	public ModelAndView realNameAuthenty(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception {
		Map<String,Object> out = new HashMap<String,Object>();
		String realName = request.getParameter("realName");//姓名
		String idCard = request.getParameter("idCard");//身份证号码
		String investment = request.getParameter("investment");//投资人条件
		UserDto user = getUser();
		out.put("userHeadImg", user.getUserIconFileId());
		if(!StringUtils.isEmpty(idCard)&&idCard.equals(StringUtil.maskIdCard(user.getIdCard()))){
			idCard = user.getIdCard();
		}
		String tcFlag = user.getTcFlag();//是否帮过卡1已绑  0未绑
		if(StringUtils.isEmpty(realName)){
			return outparamMethod(response, out, "001", "姓名不可为空");
		}
		if(!"1".equals(tcFlag)&&StringUtils.isEmpty(idCard)){
			return outparamMethod(response, out, "002", "身份证号不可为空");
		}
		if(!"1".equals(tcFlag)&&!StringUtils.isIdCard(idCard)){
			return outparamMethod(response, out, "003", "身份证号格式有误");
		}
		if(StringUtils.isEmpty(investment)){
			return outparamMethod(response, out, "004", "投资人条件不可为空");
		}
		Map<String,String> realInvest = new HashMap<>();
		realInvest.put("realName", realName);
		realInvest.put("idCard", idCard);
		realInvest.put("investment", investment);
		realInvest.put("userId", user.getOidUserId());
		if("1".equals(tcFlag)){//已绑卡不可更改姓名和身份证号
			realInvest.put("realName", user.getUserRealName());
			realInvest.put("idCard", user.getIdCard());
		}
		try {
			if(userService.certifiedInvestor(realInvest)>0){
				user.setCertifiedInvestor(investment);
				user.setVerifyFlag("1");
				user.setUserRealName(realInvest.get("realName"));;
				user.setIdCard(realInvest.get("idCard"));
				if("4".equals(investment)){
					user.setAuthFlag("0");
				}else{
					user.setAuthFlag("1");
				}
				this.setUser(user);
				return outparamMethod(response, out, Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
			}else{
				return outparamMethod(response, out, "006","认证投资人失败");
			}
		} catch (Exception e) {
			return outparamMethod(response, out, "005","认证投资人异常");
		}

	}
	/**
	 * 用户状态（是否实名；认证（认证标识；及内容）；绑卡(包括绑卡信息)；风险评测（风险等级；投资行为分类：进取型））
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/SCLogin/getUserState.do", method = RequestMethod.POST)
	public ModelAndView getUserState(HttpServletRequest request,HttpServletResponse response,Model model)throws Exception {
		Map<String,Object> out = new HashMap<String,Object>();
		UserDto user = getUser();
		String verifyFlag = user.getVerifyFlag();
		try {
			out.put("authFlag", "1");//实名认证
			out.put("tipCardFlag", "1");//绑卡
			out.put("riskFlag", "1");//风险测评
			out.put("singFlag", "1");//是否签约
			out.put("investorFlag", "1");//是否认证投资人

			//Map<String,Map<String,String>> userState = userService.getUserState(userId);

			if(!"1".equals(user.getAuthFlag())){//未认证投资人
				out.put("investorFlag", "0");
			}
			if(!"1".equals(user.getTcFlag())){//未绑卡
				out.put("tipCardFlag", "0");
			}
			if(StringUtils.isEmpty(verifyFlag)){//未实名
				out.put("authFlag", "0");//实名认证
			}else{
				out.put("userName", user.getUserRealName());//姓名
				out.put("maskIdCard", StringUtil.maskIdCard(user.getIdCard()));//身份证号
			}
			if(!"1".equals(user.getRaFlag())){//未测评
				out.put("riskFlag", "0");
			}else{
				Map<String,Object> outRisk = new HashMap<String,Object>();
				outRisk = StringUtil.levelNumberTOString(user.getRiskLvl());
				out.put("riskHand",outRisk.get("riskHand"));
				out.put("profitExpectation",outRisk.get("profitExpectation"));
				out.put("riskLevel", user.getRiskLvl());
			}
			if(!"1".equals(user.getSignFlag())){//未签约
				out.put("singFlag", "0");
			}
			//用户绑定的银行卡信息
			Map bankListMap = bankService.selBankMap(user.getOidUserId());
			out.put("payMoblie", StringUtils.encryptMobile(ConvUtils.convToString(bankListMap.get("mobile"))));//绑卡支付手机号
			return outparamMethod(response, out, Constants.RET_SUCCESS_CODE, Constants.RET_SUCCESS_MSG);
		} catch (Exception e) {
			logger.info(e);
			e.printStackTrace();
			return outparamMethod(response, out, "001", "获取用户状态异常");
		}
	}



}
