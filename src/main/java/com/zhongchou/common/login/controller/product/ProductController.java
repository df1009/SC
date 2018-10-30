package com.zhongchou.common.login.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;
import com.zhongchou.common.util.StringUtil;

@Controller
public class ProductController extends BaseController {
	Logger logger=Logger.getLogger(ProductController.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private IUserTenderService userTenderService;

	//初始化产品列表
	@RequestMapping(value = "loginOut/initProductList.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView initProductList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		logger.info("ProductController.initProductList  start");
		List outList = productService.selProductInitList();
		//总数
		int totalCount = productService.countProduct(null);
		//总页数
		int totalNumPages = (totalCount-1)/10+1;
		//行业
		List<Set> setList = productService.seldustryType();
		//产品列表banner图
		List<BannerDto> bannerList = productService.getBannerList();
		out.put("bannerList", bannerList);
		out.put("industryList", setList);
		out.put("initList", outList);
		out.put("totalCount", totalCount);
		out.put("totalNumPages", totalNumPages);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	//查询产品列表
	@RequestMapping(value = "loginOut/selProductList.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selProductList(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		Map selMap = new HashMap();
		logger.info("selProductList.selProductList  start");
		String industryType = request.getParameter("industryType");//行业
		String riskLvl = request.getParameter("riskLvl");//风险等级
		String platformProjectsSt = request.getParameter("ProjectsStatus");//产品状态
		String page = request.getParameter("page");//查询页数
		String sortType = request.getParameter("sortType");//排序0正序 1倒叙
		if(StringUtils.isEmpty(page)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		String regexSt = "^[0-4](,[0-4])*$";
		String regexLvl = "^[1-5](,[1-5])*$";
	    if(!StringUtils.isEmpty(platformProjectsSt)
	    		&&!StringUtils.match(regexSt, platformProjectsSt)){
	    	out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "产品状态有误");
			JsonUtil.writeJson(response,out);
			return null;
	    }
	    if(!StringUtils.isEmpty(riskLvl)
			&&!StringUtils.match(regexLvl, riskLvl)){
	    	out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "，风险等级有误");
			JsonUtil.writeJson(response,out);
			return null;
	    }
		if(!StringUtils.isEmpty(industryType)){
			selMap.put("industryType", industryType);
		}
		if(!StringUtils.isEmpty(riskLvl)){
			selMap.put("riskLvl", riskLvl);
		}
		if(!StringUtils.isEmpty(platformProjectsSt)){
			selMap.put("platformProjectsSt", platformProjectsSt);
		}
		int PageCount = 10;
		int initPage = 0;
		initPage = Integer.parseInt(page);
		initPage = (initPage-1)*PageCount;
		selMap.put("pageIndex", initPage);
		selMap.put("pageSize", PageCount);
		selMap.put("sortType", sortType);
		List outList = productService.selProductList(selMap);
		//总数
		int totalCount = productService.countProduct(selMap);
		//总页数
		int totalNumPages = (totalCount-1)/10+1;
		//行业
		List<Set> setList = productService.seldustryType();
		out.put("industryList", setList);
		out.put("productList", outList);
		out.put("totalCount", totalCount);
		out.put("totalNumPages", totalNumPages);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	//查询产品详细
	@RequestMapping(value = "SCLogin/selProductDetail.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selProductDetail(HttpServletRequest request,HttpServletResponse response,Model model,String username,String password){

		Map out = new HashMap();
		Map selMap = new HashMap();
		logger.info("ProductController.selProductList  start");
		String industryType = request.getParameter("industryType");//行业
		String riskLvl = request.getParameter("riskLvl");//风险等级
		String platformProjectsSt = request.getParameter("ProjectsStatus");//产品状态
		String sortType = request.getParameter("sortType");//排序0正序 1倒叙
		String productId = request.getParameter("productId");//产品id
		String type = request.getParameter("type");//类型1 项目信息  2项目进展
		if(StringUtils.isEmpty(productId)
				||StringUtils.isEmpty(type)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(!StringUtils.isEmpty(industryType)){
			selMap.put("industryType", industryType);
		}
		if(!StringUtils.isEmpty(riskLvl)){
			selMap.put("riskLvl", riskLvl);
		}
		if(!StringUtils.isEmpty(platformProjectsSt)){
			selMap.put("platformProjectsSt", platformProjectsSt);
		}
		selMap.put("sortType", sortType);
		selMap.put("productId", productId);
		//当前产品详细信息
		Map productDetailMap = productService.selProductDetailList(productId,type);
		if(productDetailMap==null){
			out.put(Constants.RET_CODE, "002");
			out.put(Constants.RET_MSG, "产品id有误");
			JsonUtil.writeJson(response,out);
			return null;
		}
		//查询用户最低投资金额，最高投资金额和追加金额
		returnAmount(productDetailMap,getUser().getOidUserId(),productId);
		if("0".equals(productDetailMap.get("state"))&&"2".equals(type)){//募集前不能查询项目进展
			out.put(Constants.RET_CODE, "003");
			out.put(Constants.RET_MSG, "该项目状态不能查询项目进展");
			JsonUtil.writeJson(response,out);
			return null;
		}
		if(isApp()){
			productDetailMap.put("imgPath", productDetailMap.get("appImgPath"));//手机端项目背景大图
		}
		int isAttestation = isAttestation();
		if(isAttestation!=0){//实名测评未完成
			productDetailMap.put("minAmount", "");
			productDetailMap.put("riskLvl", "");
			productDetailMap.put("salesQuota", "");
			productDetailMap.put("allotmentShares", "");
			productDetailMap.put("shareholdersNum", "");
			productDetailMap.put("surplusSalesQuota", "");
			productDetailMap.put("maxSubsAmt", "");
			productDetailMap.put("dvidMd", "");
			productDetailMap.put("issBgnDt", "");
			productDetailMap.put("issEndDt", "");
			productDetailMap.put("addAmount", "");
			productDetailMap.put("isAttestation", isAttestation);
		}
		//当前产品前后两个产品简略信息
		List adjacentProduct = productService.selAdjacentProductList(selMap);
		out.put("isAttestation", isAttestation());
		out.put("adjacentProduct", adjacentProduct);
		out.put("productDetailMap", productDetailMap);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	//查询跟投人
	@RequestMapping(value = "SCLogin/selProductInvestor.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView selProductInvestor(HttpServletRequest request,HttpServletResponse response,Model model){
		Map out = new HashMap();
		String productId = request.getParameter("productId");//产品id
		String StrsumNum = request.getParameter("sumNum");//加载更多次数,当前页数
		int pageSize =15;//每页显示数量
		int page = 1;//当前页数
		int initCount = 0;//查询初始位置
		if(isApp()){
			pageSize =10;//每页显示数量
			page = 1;//当前页数
			initCount = 0;//查询初始位置
		}
		if(StringUtils.isEmpty(productId)){
			out.put(Constants.RET_CODE, "001");
			out.put(Constants.RET_MSG, "参数有误");
			JsonUtil.writeJson(response,out);
			return null;

		}
		if(!StringUtils.isEmpty(StrsumNum)){
			try {
				page = Integer.parseInt(StrsumNum);
				page=page<1?1:page;
			} catch (Exception e) {
				out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "系统异常");
				JsonUtil.writeJson(response,out);
				return null;
			}
		}
		initCount = (page-1)*pageSize;
		int sumCount = productService.countProductInvestor(productId);
		//总页数 (totalCount-1)/10+1;
		int pageCount = (sumCount-1)/pageSize+1;
		List produtInvestor = productService.selProductInvestor(productId,initCount,pageSize);
		out.put("produtInvestor", produtInvestor);
		out.put("pageCount", pageCount);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}

	public void returnAmount(Map proMap,String userId,String proId){
		//查询用户是否投资过该产品
		if(userTenderService.selTenderProAmount(userId, proId)==0){//第一次购买
			proMap.put("minAmount",proMap.get("fistMinAmount"));
		}
		//用户银行卡当日的剩余
		int surplusAmt = (int)userTenderService.selRemainingQuota(userId,proId).get("dayLimitAmt");
		//最大可投金额
		//int maxSubsAmt = Integer.parseInt(ConvUtils.convToString(proMap.get("maxSubsAmt")));
		//最小追投金额
		//int addAmount = Integer.parseInt(ConvUtils.convToString(proMap.get("addAmount")));
		//剩余可投金额
		//String salesQuota = ConvUtils.convToString(proMap.get("surplusSalesQuota"));
		//int surplusSalesQuota = Integer.parseInt(salesQuota);
		//int maxAmount = surplusAmt;
		/*if(surplusSalesQuota != 0){
			maxAmount = surplusAmt<maxSubsAmt?surplusAmt:surplusSalesQuota;
		}*/
		/*if(maxSubsAmt !=0){
			maxAmount = maxAmount<maxSubsAmt?maxAmount:maxSubsAmt;
		}*/
		//maxAmount = maxSubsAmt;//详情只取项目的最大额度
		//proMap.put("maxSubsAmt", String.valueOf(maxAmount));
		proMap.put("surplusAmt", String.valueOf(surplusAmt));
	}

}
