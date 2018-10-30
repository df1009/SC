package com.zhongchou.common.login.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.PcProductService;

@Controller
public class pcProductController extends BaseController {
	Logger logger=Logger.getLogger(pcProductController.class);
	@Autowired
	private PcProductService pcProductService;

	//产品详情
	@RequestMapping(value = "SCLogin/getProductDetail.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getProductDetail(HttpServletRequest request,HttpServletResponse response){
		Map out = new HashMap();
		UserDto user = getUser();
		String productId = request.getParameter("productId");//产品id
		if(StringUtils.isEmpty(productId)){
			return	outparamMethod(response,out,"001","产品id不可为空");
		}

		Map productInfo = pcProductService.getProductDetail(productId);//产品信息
		List<Map<String,String>> productIntroduceList = pcProductService.getProductIntroduce(productId);//项目介绍
		Map productTrading = pcProductService.getProductTradingInfo(productId);//融资信息
		List<Map<String,Object>> productProgressList = pcProductService.getProductProgress(productId);//项目进展
		//展示其它项目信息列表
		List otherProjectList = pcProductService.getOtherProjectList(productId);
		Map<String,Object> productHeadInfo = new HashMap<>();
		//查询用户是否已认证
		if("0".equals(user.getAuthFlag())){
			productHeadInfo.put("projectName", ConvUtils.convToString(productInfo.get("projectName")));//TODO项目方
			productHeadInfo.put("projectAdds",ConvUtils.convToString(productInfo.get("projectAdds")));//TODO项目方地址
			productHeadInfo.put("projectMainTitle",ConvUtils.convToString(productInfo.get("projectMainTitle")));//产品主标题
			productHeadInfo.put("summaryText",ConvUtils.convToString(productInfo.get("summaryText")));//文字介绍
			productHeadInfo.put("linkUrl",ConvUtils.convToString(productInfo.get("linkUrl")));//项目视频介绍
			productHeadInfo.put("imgPath",ConvUtils.convToString(productInfo.get("imgPath")));//项目背景大图
			productHeadInfo.put("projectCharacter",productInfo.get("projectCharacter"));//TODO环保,健康,盈利不错(项目特点)
			productHeadInfo.put("appImgPath",ConvUtils.convToString(productInfo.get("appImgPath")));//APP背景图
			productHeadInfo.put("state",ConvUtils.convToString(productInfo.get("state")));//项目状态
			productHeadInfo.put("showProjectFlag",ConvUtils.convToString(productInfo.get("showProjectFlag")));//展示项目标识
			out.put("productIntroduceList",productIntroduceList);
			out.put("authFlag","0");//未认证
			out.put("productInfo",productHeadInfo);

		}else{
			/*String state = ConvUtils.convToString(productInfo.get("state"));
			if("0".equals(state)){//预热期不能查询项目进展
				return outparamMethod(response,out,"002","预热期不能查询项目进展");
			}*/
			out.put("productInfo",productInfo);
			out.put("authFlag","1");//认证
			out.put("productIntroduceList",productIntroduceList);
			out.put("productTrading",productTrading);
			out.put("productProgressList",productProgressList);
		}
		out.put("otherProjectList", otherProjectList);
		return outparamMethod(response,out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
	}



	//查询跟投人
	@RequestMapping(value = "SCLogin/getProductInvestor.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getProductInvestor(HttpServletRequest request,HttpServletResponse response){
		Map out = new HashMap();
		String productId = request.getParameter("productId");//产品id
		String StrsumNum = request.getParameter("sumNum");//加载更多次数,当前页数
		int pageSize =16;//每页显示数量
		int page = 1;//当前页数
		if("1".equals(StrsumNum)){
			pageSize =32;
		}
		int initCount = 0;//查询初始位置
		if(StringUtils.isEmpty(productId)){
			return outparamMethod(response,out,"001","产品id不可为空");
		}
		if(!StringUtils.isEmpty(StrsumNum)){
			try {
				page = Integer.parseInt(StrsumNum);
				page=page<1?1:page;
			} catch (Exception e) {
				return outparamMethod(response,out,"002","当前页数有误");
			}
		}
		initCount = (page-1)*pageSize;
		int sumCount = pcProductService.countProductInvestor(productId);
		//总页数 (totalCount-1)/10+1;

		int pageCount = 1;
		if(sumCount>32){
			pageCount = (sumCount-33)/pageSize+2;
		}
		List produtInvestor = pcProductService.selProductInvestor(productId,initCount,pageSize);
		out.put("produtInvestor", produtInvestor);
		out.put("pageCount", pageCount);
		return outparamMethod(response,out,Constants.RET_SUCCESS_CODE,Constants.RET_SUCCESS_MSG);
	}

}
