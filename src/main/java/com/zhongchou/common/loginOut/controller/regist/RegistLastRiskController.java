package com.zhongchou.common.loginOut.controller.regist;

import java.util.Date;
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

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IRiskService;
import com.zhongchou.common.service.IUserService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class RegistLastRiskController extends BaseController {

	@Autowired
	private IRiskService riskService;
	@Autowired
	private IUserService userService;

	@RequestMapping(value = "SCLogin/getRiskQuestions.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView riskQuestions(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> out = new HashMap<String, Object>();
		Map<String, Object> result = riskService.getQuestions();

		if(result != null){
			out.put("list",result.get("subjectList"));
			out.put("totalResults",result.get("totalResults"));
			out.put("testqId",result.get("testqId"));
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		}else{
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "取题库失败");
		}

		JsonUtil.writeJson(response,out);
		return null;
	}

	@RequestMapping(value = "SCLogin/riskSubmit.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView riskSubmit(HttpServletRequest request,HttpServletResponse response,String testqId,String answer){

		Map<String, Object> out = new HashMap<String, Object>();
		Map<String, String> params = new HashMap<String, String>();
		UserDto user = getUser();

		logger.info("用户:"+user.getOidUserId()+"--------调用提交评测开始--------testqId："+testqId+"---answer:"+answer);
		if(!"1".equals(user.getAuthFlag())){//未认证投资人
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "请先认证投资人");
			JsonUtil.writeJson(response,out);
			return null;
  		}
		String loginId = user.getOidUserId();
		//获取登录用户ip
		String userIp = getRemoteAddr();
		params.put("userIp", userIp);
		params.put("loginId", loginId);
		params.put("testqId", testqId);
		params.put("answer", answer);
		if(!StringUtils.isEmpty(loginId)){

			if(StringUtils.isEmpty(testqId)||StringUtils.isEmpty(answer)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "题库编号和答题结果不可为空");
				JsonUtil.writeJson(response,out);
				return null;
			}else{

				Map<String, Object> result = riskService.riskSubmit(params);

				if(!result.isEmpty()&&"000000".equals(result.get("rsp_code"))){

					/*UserDto userDto = new UserDto();
					setUser(userService.getUser(userDto));
					request.getSession().removeAttribute("LOGIN_ID");*/

					this.setSessionObj("score", (String)result.get("score"));
					this.setSessionObj("riskLevel", (String)result.get("riskLevel"));
					this.setSessionObj("riskLevelDesc", (String)result.get("riskLevelDesc"));
					out.put("score",result.get("score"));//评测分数
					out.put("riskLevel",result.get("riskLevel"));//风险等级
					String riskHand = "";
					String profitExpectation = "";
					out = StringUtil.levelNumberTOString(ConvUtils.convToString(result.get("riskLevel")));
					//用户信息更改记录
					Map paramMap = new HashMap();
					paramMap.put("beforeChange", StringUtil.levelNumberTOString(user.getRiskLvl()).get("riskHand"));
					paramMap.put("afterChange", StringUtil.levelNumberTOString((String)result.get("riskLevel")).get("riskHand"));
					paramMap.put("count", "用户风险评测");
					paramMap.put("loginId", loginId);
					if(!userService.addUserModifyLog(paramMap)){
						logger.error(loginId+"用户信息更改记录插入失败");
					}
					if(user!=null){
						user.setRaFlag("1");
						user.setRiskLvl((String)result.get("riskLevel"));
						setUser(user);
					}
					out.put("riskLevelDesc",result.get("riskLevelDesc"));//风险描述
					out.put("riskDate",DateUtils.convertDate2String(new Date(), "yyyy年MM月dd日"));//时间
					out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
					out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
					JsonUtil.writeJson(response,out);
					return null;
				}else{
					out.put(Constants.RET_CODE, "003");
					out.put(Constants.RET_MSG, ConvUtils.convToString(result.get("rsp_desc")));
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
		}else{
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "用户未登录");
			JsonUtil.writeJson(response,out);
			return null;

		}
	}
	@RequestMapping(value = "SCLogin/newRiskSubmit.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView newRiskSubmit(HttpServletRequest request,HttpServletResponse response,String testqId,String answer){

		Map<String, Object> out = new HashMap<String, Object>();
		Map<String, String> params = new HashMap<String, String>();
		UserDto user = getUser();

		logger.info("用户:"+user.getOidUserId()+"--------调用提交评测开始--------testqId："+testqId+"---answer:"+answer);

		if(!"1".equals(user.getAuthFlag())){//未认证投资人
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "请先认证投资人");
			JsonUtil.writeJson(response,out);
			return null;
  		}
		String loginId = user.getOidUserId();
		//获取登录用户ip
		String userIp = getRemoteAddr();
		params.put("userIp", userIp);
		params.put("loginId", loginId);
		params.put("testqId", testqId);
		params.put("answer", answer);
		if(!StringUtils.isEmpty(loginId)){

			if(StringUtils.isEmpty(testqId)||StringUtils.isEmpty(answer)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "题库编号和答题结果不可为空");
				JsonUtil.writeJson(response,out);
				return null;
			}else{

				Map<String, Object> result = riskService.newRiskSubmit(params);

				if(!result.isEmpty()&&"000000".equals(result.get("rsp_code"))){

					/*UserDto userDto = new UserDto();
					setUser(userService.getUser(userDto));
					request.getSession().removeAttribute("LOGIN_ID");*/

					this.setSessionObj("score", (String)result.get("score"));
					this.setSessionObj("riskLevel", (String)result.get("riskLevel"));
					this.setSessionObj("riskLevelDesc", (String)result.get("riskLevelDesc"));
					String riskHand = "";
					String profitExpectation = "";
					out = StringUtil.levelNumberTOString(ConvUtils.convToString(result.get("riskLevel")));
					//用户信息更改记录
					Map paramMap = new HashMap();
					paramMap.put("beforeChange", StringUtil.levelNumberTOString(user.getRiskLvl()).get("riskHand"));
					paramMap.put("afterChange", StringUtil.levelNumberTOString((String)result.get("riskLevel")).get("riskHand"));
					paramMap.put("count", "用户风险评测");
					paramMap.put("loginId", loginId);
					paramMap.put("riskHand", riskHand);
					if(!userService.addUserModifyLog(paramMap)){
						logger.error(loginId+"用户信息更改记录插入失败");
					}
					if(user!=null){
						user.setRaFlag("1");
						user.setRiskLvl((String)result.get("riskLevel"));
						setUser(user);
					}
					out.put("score",result.get("score"));//评测分数
					out.put("riskLevel",result.get("riskLevel"));//风险等级
					out.put("riskLevelDesc",result.get("riskLevelDesc"));//风险描述
					out.put("riskDate",DateUtils.convertDate2String(new Date(), "yyyy年MM月dd日"));//时间
					out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
					out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
					JsonUtil.writeJson(response,out);
					return null;
				}else{
					out.put(Constants.RET_CODE, "003");
					out.put(Constants.RET_MSG, ConvUtils.convToString(result.get("rsp_desc")));
					JsonUtil.writeJson(response,out);
					return null;
				}
			}
		}else{
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "用户未登录");
			JsonUtil.writeJson(response,out);
			return null;

		}
	}
}
