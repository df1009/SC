package com.zhongchou.common.login.controller.personalCenter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.service.IBuyProductService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.QRUtil;
import com.zhongchou.common.util.VerificationUtil;
@Controller
public class InvitationInformationController extends BaseController {
	String url = Config.getString("registUrl");//壹盐双创网址
	String weixinurl = Config.getString("WEIXINURL");//微信域名
	@Autowired
	private ProductService productService;
	@Autowired
	private IBankService bankService;
	@Autowired
	private IBuyProductService buyProductService;
	@Autowired
	private IUserManageService userManageService;

	//String url = "www.yiyanstar.com?code=";

	//查询我的邀请记录
	@RequestMapping(value = "SCLogin/getInviteLink.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getInviteLink(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		UserDto user = getUser();
		String refCode = user.getRefcode();
		//邀请人列表
		List introducerPresonList = userManageService.selIntroducerPreson(user.getOidUserId());

		//邀请人投资列表
		List IntroducerInvestList = productService.selIntroducerInvest(user.getOidUserId());
		//我的邀请人
		Map introducerMap = userManageService.selIntroducer(user.getOidUserId());
		String registUrl = url+"#/register?registerCode=";
		if(isApp()){
			 registUrl = weixinurl+"/dist/index.html#/register?registerCode=";
		}
		out.put("invCode", registUrl+refCode);
		out.put("introducerPresonList", introducerPresonList);
		if(null!=introducerMap && introducerMap.size()>0){
			out.put("introducerMap", introducerMap);
		}
		out.put("IntroducerInvestList", IntroducerInvestList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	/**
	 * 生成二维码
	 * @param userPhone 手机号
	 * @param phoneCode 验证码
	 * @param sendType 	类型
	 * @return 无
	 */
	@RequestMapping(value = "SCLogin/getInviteQR.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getQR(HttpServletRequest request,HttpServletResponse response,Model model){
		UserDto user = getUser();
		String refCode = user.getRefcode();
		String registUrl = url+"#/register?registerCode=";
		if(isApp()){
			 registUrl = weixinurl+"/dist/index.html#/register?registerCode=";
		}
		String InviteQR = registUrl+refCode;
        int width = 300;
        int height = 300;
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(InviteQR,
			        BarcodeFormat.QR_CODE, width, height, hints);
			BufferedImage image = QRUtil.toBufferedImage(bitMatrix);
	        // 创建字符流
	        ByteArrayOutputStream bs = new ByteArrayOutputStream();
	        // 写入字符流
	        ImageIO.write(image, "jpg", bs);
	        // 转码成字符串
	        String imgsrc = Base64.toBase64String(bs.toByteArray());
	        Map<String,Object> out = new HashMap<String,Object>();
	        out.put(Constants.RET_CODE, "000");
			out.put(Constants.RET_MSG, imgsrc);
			JsonUtil.writeJson(response,out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param request introducerMobile 邀请人手机号
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "SCLogin/setIntroducerMobile.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView setIntroducerMobile(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		UserDto user = getUser();
		UserDto userDto = new UserDto();
		String userPhone = request.getParameter("userPhone");
		if(!StringUtils.isMobileNum(userPhone)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "系统异常");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if (VerificationUtil.checkPhoneInput(userManageService,userPhone)) {
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "手机号不存在");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//我的邀请人
		if(!userManageService.selIntroducer(user.getOidUserId()).isEmpty()){
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "已有邀请人");
			JsonUtil.writeJson(response,out);
			return null;
		}
		userDto.setMobile(userPhone);
		Map userMap = userManageService.getUser(userDto);
		user.setIntroducerUserId(ConvUtils.convToString(userMap.get("OID_USER_ID")));
		user.setUpdUserId(user.getOidUserId());
		user.setUpdUserId(user.getOidUserId());
		if(user.getOidUserId().equals(userMap.get("OID_USER_ID"))){
			out.put(Constants.RET_CODE, "005");
			out.put(Constants.RET_MSG, "不能填写自己");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(userManageService.setIntroducer(user)>0){
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}else{
			out.put(Constants.RET_CODE, "004");
			out.put(Constants.RET_MSG, "设置失败");
			JsonUtil.writeJson(response,out);
			return null;
		}
	}
}
