package com.zhongchou.common.login.controller.product;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import com.yanshang.util.StringUtils;
import com.zhongchou.common.base.BaseController;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dao.ProductDao;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.service.PCProductListService;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.JsonUtil;

@Controller
public class PCProductListController extends BaseController{
	Logger logger=Logger.getLogger(PCProductListController.class);
	@Autowired
	private PCProductListService productService;

	//查询Banner列表
	@RequestMapping(value = "loginOut/selProductBannerList.do", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView initProductList(HttpServletRequest request,HttpServletResponse response){
		Map out = new HashMap();
		List<BannerDto> bannerList = productService.getBannerList();
		out.put("bannerList", bannerList);
		out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
		out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
		JsonUtil.writeJson(response,out);
		return null;
	}
	//查询产品列表
		@RequestMapping(value = "loginOut/selPcProductList.do", method ={ RequestMethod.POST,RequestMethod.GET})
		public ModelAndView selProductList(HttpServletRequest request,HttpServletResponse response,Model model){
			Map out = new HashMap();
			Map selMap = new HashMap();
			logger.info("selPcProductList  start");
			String industryType = request.getParameter("industryType");//行业分类
			String platformProjectsSt = request.getParameter("ProjectsStatus");//产品状态
			String page = request.getParameter("page");//当前页
			String goodPage = request.getParameter("goodPage");//当前页
			/*if(StringUtils.isEmpty(industryType)){
				out.put(Constants.RET_CODE, "001");
				out.put(Constants.RET_MSG, "行业分类有误");
				JsonUtil.writeJson(response,out);
				return null;
			}*/
			/*String regexSt = "^[0-4](,[0-4])*$";
			String regexLvl = "^[1-5](,[1-5])*$";*/
		  /*  if(StringUtils.isEmpty(platformProjectsSt)){
		    	out.put(Constants.RET_CODE, "002");
				out.put(Constants.RET_MSG, "产品状态有误");
				JsonUtil.writeJson(response,out);
				return null;
		    }*/
			if(!StringUtils.isEmpty(industryType)){
				selMap.put("industryType", industryType);
			}
			if(!StringUtils.isEmpty(platformProjectsSt)){
				selMap.put("platformProjectsSt", platformProjectsSt);
			}
			int PageCount = 9;
			int initPage = 1;
			if(page!=null && page!=""){
				initPage = Integer.parseInt(page);
				if(initPage<=0){initPage=1;}
				if(goodPage!=null && goodPage!=""){
					initPage = Integer.parseInt(goodPage);
					if(initPage<=0){initPage=1;}
				}
			}
			initPage = (initPage-1)*PageCount;
			selMap.put("pageIndex", initPage);
			selMap.put("pageSize", PageCount);
			//多条件分页查询产品
			List outList = productService.selPcProductList(selMap);
			//多条件分页查询精品产品
			List outGoodList = productService.selPcGoodProductList(selMap);
			//总数
			int totalCount = productService.countPcProduct(selMap);
			//总数
			int totalGoodCount = productService.countPcGoodProduct(selMap);
			//总页数
			int totalNumPages = (totalCount-1)/9+1;
			//精选展示总页数
			int totalGoodNumPages = (totalGoodCount-1)/9+1;
			//行业
			Set setList = productService.seldustryType();
			out.put("industryList", setList);
			out.put("productList", outList);
			out.put("productGoodList", outGoodList);
			out.put("totalCount", totalCount);
			out.put("totalNumPages", totalNumPages);
			out.put("totalGoodCount", totalGoodCount);
			out.put("totalGoodNumPages", totalGoodNumPages);
			out.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
			out.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
			JsonUtil.writeJson(response,out);
			return null;
		}
}
