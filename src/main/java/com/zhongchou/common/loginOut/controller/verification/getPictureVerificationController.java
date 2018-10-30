package com.zhongchou.common.loginOut.controller.verification;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.service.IRegistService;
import com.zhongchou.common.service.IUserManageService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.VerificationUtil;

@Controller
public class getPictureVerificationController extends BaseController {
	@Autowired
	private IRegistService registService;

	@Autowired
	private IUserManageService userManageService;

	 /**
     * 图片验证码取得
     */
	@RequestMapping(value = "getImgVerifyCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
	 public void ImgVerifyCode(HttpServletRequest request, HttpServletResponse response)
	            throws IOException {
	        int width = 63;
	        int height = 37;
	        Random random = new Random();
	        //设置response头信息
	        //禁止缓存
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "no-cache");
	        response.setDateHeader("Expires", 0);

	        //生成缓冲区image类
	        BufferedImage image = new BufferedImage(width, height, 1);
	        //产生image类的Graphics用于绘制操作
	        Graphics g = image.getGraphics();
	        //Graphics类的样式
	        g.setColor(this.getRandColor(200, 250));
	        g.setFont(new Font("Times New Roman",0,28));
	        g.fillRect(0, 0, width, height);
	        //绘制干扰线
	        for(int i=0;i<40;i++){
	            g.setColor(this.getRandColor(130, 200));
	            int x = random.nextInt(width);
	            int y = random.nextInt(height);
	            int x1 = random.nextInt(12);
	            int y1 = random.nextInt(12);
	            g.drawLine(x, y, x + x1, y + y1);
	        }

	        //绘制字符
	        String strCode = "";
	        for(int i=0;i<4;i++){
	            String rand = VerificationUtil.generateVerifyCode(1);
	            strCode = strCode + rand;
	            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
	            g.drawString(rand, 13*i+6, 28);
	        }
	        //将字符保存到session中用于前端的验证
	        this.setSessionObj(Constants.VERIFY_CODE, strCode);
	        g.dispose();

	      //  ImageIO.write(image, "JPEG", response.getOutputStream());

	     // 创建编码对象
	     //  Base64.Encoder base64 = Base64.getEncoder();
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
	    }

	 	//创建颜色
	    Color getRandColor(int fc,int bc){
	        Random random = new Random();
	        if(fc>255)
	            fc = 255;
	        if(bc>255)
	            bc = 255;
	        int r = fc + random.nextInt(bc - fc);
	        int g = fc + random.nextInt(bc - fc);
	        int b = fc + random.nextInt(bc - fc);
	        return new Color(r,g,b);
	    }
		 /**
			 * 验证图片验证码
			 * @param userPhone 手机号
			 * @param phoneCode 验证码
			 * @param sendType 	类型
			 * @return 无
			 */
			@RequestMapping(value = "verifiedPicCode.do", method ={ RequestMethod.POST,RequestMethod.GET})
			public ModelAndView getVerifyCodeToImg(HttpServletRequest request,HttpServletResponse response,Model model){
				Map<String,Object> out = new HashMap<String,Object>();
				String imgCode = request.getParameter("imgCode");//图片验证码
				if(StringUtils.isEmpty(imgCode)){
					out.put(Constants.RET_CODE, "001");
					out.put(Constants.RET_MSG, "格式错误");
					JsonUtil.writeJson(response,out);
					return null;
				}
				//图形验证码
				if(!checkImgCode(imgCode)){
					out.put(Constants.RET_CODE, "002");
					out.put(Constants.RET_MSG, "图形验证码错误");
					JsonUtil.writeJson(response,out);
					return null;
				}
				this.setSessionObj(Constants.VERIFIEDPICODE, Constants.VERIFIEDPICODESUCCESS);
				out.put(Constants.RET_CODE, "000");
				out.put(Constants.RET_MSG, "success");
				JsonUtil.writeJson(response,out);
				return null;
			}
}
