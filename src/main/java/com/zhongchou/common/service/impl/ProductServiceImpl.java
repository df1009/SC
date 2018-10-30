package com.zhongchou.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IIndexDao;
import com.zhongchou.common.dao.IUserTenderDao;
import com.zhongchou.common.dao.ProductDao;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.ProductDetailDto;
import com.zhongchou.common.dto.ProjectDto;
import com.zhongchou.common.login.controller.product.ProductController;
import com.zhongchou.common.service.ProductService;
import com.zhongchou.common.util.StringUtil;

@Service
public class ProductServiceImpl implements ProductService {
	Logger logger=Logger.getLogger(ProductServiceImpl.class);
	//用户没有上传头像时的默认头像
	String defaultAvatarurl = Config.getString("defaultAvatarurl");

	@Autowired
	private ProductDao productDao;
	@Autowired
	private IIndexDao indexDao;
	@Autowired
	private IUserTenderDao userTenderDao;

	public List<ProjectDto> getProjectList() {
		List<ProjectDto> returnMessage = new ArrayList<ProjectDto>();
		List<Row> message = productDao.getProjectList();
		for (Row row : message) {
			if (!row.isEmpty()) {
				ProjectDto projectDto = new ProjectDto();
				String state = (String)row.get("PLATFORM_PROJECTS_ST");
				/*if("1".equals(state)){//认购
					projectDto.setLowestMoney(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("MIN_SUBS_AMT"))/100));//个人最低认购金额(元)
				}else if("2".equals(state)){//申赎
					projectDto.setLowestMoney(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("MIN_BIDS_AMT"))/100));//个人最低申购金额(元)
				}else{
					projectDto.setLowestMoney("0");//个人最低申购金额(分)
				}*/
				projectDto.setLowestMoney(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
				projectDto.setProjectName((String)row.get("PLATFORM_PROJECTS_SHORT_NM"));//产品全称
				projectDto.setRiskLevel((String)row.get("RISK_LVL"));//风险等级
				/*projectDto.setAimMoney(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("SALES_QUOTA"))/100));//总销售额度(元)
				projectDto.setAlreadyMoney(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("SURPLUS_SALES_QUOTA"))/100));//剩余销售额度(元)
*/				projectDto.setOidPlatformProjectsId(row.get("OID_PLATFORM_PROJECTS_ID").toString());///产品id
				projectDto.setIss_st((String)row.get("PLATFORM_PROJECTS_ST"));//发行状态
				projectDto.setAimMoney(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//总销售额度(元)
				projectDto.setAlreadyMoney(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA"))));//剩余销售额度(元)
				projectDto.setProjectMainTitle(ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//项目主标题
				returnMessage.add(projectDto);
			}
		}
		return returnMessage;
	}
	//初始化产品列表
	public List<ProductDetailDto> selProductInitList(){
		logger.info("ProductServiceImpl.selProductInitList start");
		List<Row> rowList = productDao.selProductFirstList();
		List<ProductDetailDto> retList = new ArrayList();
		for (Row row : rowList) {
			if (!row.isEmpty()) {
				String state = (String)row.get("PLATFORM_PROJECTS_ST");
				String lvl = (String)row.get("RISK_LVL");
				ProductDetailDto productDetailDto = new ProductDetailDto();
				productDetailDto.setProduct_short_nm((String)row.get("PLATFORM_PROJECTS_SHORT_NM"));//产品简称
			/*	if("1".equals(state)){//认购
					productDetailDto.setMin_bids_amt(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("MIN_SUBS_AMT"))/100));//个人最低认购金额(元)
				}else if("2".equals(state)){//申赎
					productDetailDto.setMin_bids_amt(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("MIN_BIDS_AMT"))/100));//个人最低申购金额(元)
				}else{
					productDetailDto.setMin_bids_amt("0");//个人最低申购金额(分)
				}*/
				if("1".equals(state)){//认购
					productDetailDto.setSubs_add_amt(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SUBS_ADD_AMT"))));//个人认购追加金额(元)
				}else if("2".equals(state)){//申赎
					productDetailDto.setSubs_add_amt(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("BIDS_ADD_AMT"))));//个人申购追加金额(元)
				}else{
					productDetailDto.setSubs_add_amt("1");//个人追加金额
				}
				productDetailDto.setMin_bids_amt(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("FIRST_MIN_BUY"))));//个人最低申购金额(分)
				productDetailDto.setFirst_min_buy(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
				productDetailDto.setRisk_lvl(lvl);//风险级别
				/*productDetailDto.setSales_quota(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("SALES_QUOTA"))/100));//总销售额度(元)
*/				productDetailDto.setIss_st((String)row.get("PLATFORM_PROJECTS_ST"));//发行状态
				/*productDetailDto.setSurplus_sales_quota(StringUtil.doubleDecimalFormat(Double.parseDouble((String)row.get("SURPLUS_SALES_QUOTA"))/100));//剩余销售额度(元)
*/				productDetailDto.setProduct_cd((String)row.get("OID_PLATFORM_PROJECTS_ID"));//产品id
				productDetailDto.setSales_quota(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//总销售额度(元)
				productDetailDto.setSurplus_sales_quota(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA"))));//剩余销售额度(元)
				productDetailDto.setProjectMainTitle(ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//项目主标题
				retList.add(productDetailDto);
			}
		}
		return retList;
	}
	//查询产品列表banner图
	@Override
	public List<BannerDto> getBannerList() {
		List<BannerDto> returnMessage = new ArrayList<BannerDto>();
		List<Row> message = indexDao.getBannerList("2");//产品列表banner
		for (Row row : message) {
			if (!row.isEmpty()) {
				BannerDto bannerDto = new BannerDto();
				bannerDto.setBannerUrl((String)row.get("IMG_PATH"));
				bannerDto.setBannerLink((String)row.get("LINK_URL"));
				bannerDto.setBannerAppUrl((String)row.get("APP_IMG_PATH"));
				bannerDto.setBannerAppLink((String)row.get("APP_LINK_URL"));
				returnMessage.add(bannerDto);
			}
		}
		return returnMessage;
	}

	//分页查询产品列表
	@Override
	public List<ProductDetailDto> selProductList(Map selMap) {
		List<Row> rowList = productDao.selProductList(selMap);
		List<ProductDetailDto> retList = new ArrayList();
		for (Row row : rowList) {
			if (!row.isEmpty()) {
				String state = ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST"));
				String lvl = ConvUtils.convToString(row.get("RISK_LVL"));
				ProductDetailDto productDetailDto = new ProductDetailDto();
				productDetailDto.setProduct_short_nm(ConvUtils.convToString(row.get("PLATFORM_PROJECTS_SHORT_NM")));//产品简称
				/*if("1".equals(state)){//认购
					productDetailDto.setMin_bids_amt(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("MIN_SUBS_AMT"))));//个人最低认购金额(元)
				}else if("2".equals(state)){//申赎
					productDetailDto.setMin_bids_amt(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("MIN_BIDS_AMT"))));//个人最低申购金额(元)
				}else{
					productDetailDto.setMin_bids_amt("0");//个人最低申购金额(分)
				}*/
				productDetailDto.setMin_bids_amt(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
				productDetailDto.setRisk_lvl(lvl);//风险级别
				productDetailDto.setSales_quota(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//总销售额度(元)
				productDetailDto.setSurplus_sales_quota(StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA"))));//剩余销售额度(元)
				productDetailDto.setIss_st(ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST")));//发行状态
				productDetailDto.setProduct_cd(ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
				productDetailDto.setProjectMainTitle(ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//项目主标题
				retList.add(productDetailDto);
			}
		}
		return retList;
	}
	//查询产品总数
	@Override
	public int countProduct(Map selMap) {
		return productDao.countProduct(selMap);
	}
	//查询行业类别
	@Override
	public List<Set> seldustryType() {
		List<Row> listRow = productDao.selindustryType();
		List<Set> listSet = new ArrayList<Set>();
		for (Row row : listRow) {
			Set proSet = new HashSet();
			if (!row.isEmpty()) {
				proSet.add((String)row.get("INDUSTRY_NM"));
				listSet.add(proSet);
				}
			}
		return listSet;
	}

	//查询相邻两个产品简略信息
	public List<Map> selAdjacentProductList(Map selMap) {
		logger.info("ProductServiceImpl.selAdjacentProductList start");
		List<Row> rowList = productDao.selProductList(selMap);
		Map UpProduct = new HashMap();
		Map downProduct = new HashMap();
		List<Map> adjacentProduct = new ArrayList<Map>();
		for (int i = 0; i < rowList.size(); i++) {
				Row row = rowList.get(i);
			if (!row.isEmpty()) {
				if(selMap.get("productId").equals(ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")))){//找到当前产品
					if(i<rowList.size()-1){//排序最后一条前
						Row brief = productDao.selProductBrief(rowList.get(i+1).get("OID_PLATFORM_PROJECTS_ID").toString());
						UpProduct.put("oidPlatformProjectsId", ConvUtils.convToString(brief.get("OID_PLATFORM_PROJECTS_ID")));//产品简称
						UpProduct.put("productShortNm", ConvUtils.convToString(brief.get("PLATFORM_PROJECTS_SHORT_NM")));//产品简称
						UpProduct.put("smallImgPath", ConvUtils.convToString(brief.get("IMG_PATH")));//产品图片
						UpProduct.put("userName", ConvUtils.convToString(brief.get("USER_NAME")));//产品发起人
						UpProduct.put("issBgnDt", DateUtils.convertDate2String(DateUtils.convertString2Date(ConvUtils.convToString(brief.get("ISS_BGN_DT")), "yyyyMMdd"), "yyyy年MM月dd日"));//产品发起时间
						UpProduct.put("num", "1");//位置--后一条
						adjacentProduct.add(UpProduct);
					}
					if(i>0){//排序第一条后
						Row brief = productDao.selProductBrief(rowList.get(i-1).get("OID_PLATFORM_PROJECTS_ID").toString());
						downProduct.put("oidPlatformProjectsId", ConvUtils.convToString(brief.get("OID_PLATFORM_PROJECTS_ID")));//产品简称
						downProduct.put("productShortNm", ConvUtils.convToString(brief.get("PLATFORM_PROJECTS_SHORT_NM")));//产品简称
						downProduct.put("smallImgPath", ConvUtils.convToString(brief.get("IMG_PATH")));//产品图片
						downProduct.put("userName", ConvUtils.convToString(brief.get("USER_NAME")));//产品发起人
						downProduct.put("issBgnDt", DateUtils.convertDate2String(DateUtils.convertString2Date(ConvUtils.convToString(brief.get("ISS_BGN_DT")), "yyyyMMdd"), "yyyy年MM月dd日"));//产品发起时间
						downProduct.put("num", "-1");//位置--前一条
						adjacentProduct.add(downProduct);
					}
					break;
				}
			}
		}
		return adjacentProduct;
	}
	//查询产品详情
	@Override
	public Map selProductDetailList(String productId,String type) {
		Row productRow = productDao.selProductDetaile(productId);
		//查询产品模块信息
		List<Row> productListRow = productDao.selProductIntroduce(productId,type);
		Map productIntroduceMap = null;
		Map productDetail=null;
		if(!productRow.isEmpty()){
			productDetail = new HashMap();
			productDetail.put("platformProjectsFullNm", ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_FULL_NM")));//产品全称
			productDetail.put("summaryText", ConvUtils.convToString(productRow.get("SUMMARYTEXT")));//文字介绍
			productDetail.put("linkUrl", ConvUtils.convToString(productRow.get("LINK_URL")));//项目视频介绍
			productDetail.put("imgPath", ConvUtils.convToString(productRow.get("IMG_PATH")));//项目背景大图
			productDetail.put("appImgPath", ConvUtils.convToString(productRow.get("APP_IMG")));//项目背景大图
			productDetail.put("ivsName", ConvUtils.convToString(productRow.get("USER_NAME")));//项目发起人
			productDetail.put("userIconFileId", ConvUtils.convToString(productRow.get("USER_ICON_FILE_ID")));//项目发起人头像
			if(StringUtils.isEmpty((String)productDetail.get("userIconFileId"))){
				productDetail.put("userIconFileId", defaultAvatarurl);
			}
			String bgntime = ConvUtils.convToString(productRow.get("ISS_BGN_DT"));
			if(StringUtils.isEmpty(bgntime)){
				productDetail.put("issBgnDt1",bgntime);
			}else{
				if(bgntime.indexOf("-")>0){
					productDetail.put("issBgnDt1", bgntime.split("-")[0]+"年"+bgntime.split("-")[1]+"月"+bgntime.split("-")[2]+"日");//项目发起时间
				}else{
					productDetail.put("issBgnDt1", bgntime.substring(0,4)+"年"+ bgntime.substring(4,6)+"月"+ bgntime.substring(6,8)+"日");//项目发起时间
				}
			}

			productDetail.put("platformProjectsShortNm", ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_SHORT_NM")));//项目名称
			productDetail.put("productId", ConvUtils.convToString(productRow.get("OID_PLATFORM_PROJECTS_ID")));//项目代码
			productDetail.put("legalPerson", ConvUtils.convToString(productRow.get("ENTERPRISE_LEGAL_PERSON_NAME")));//企业法人
			productDetail.put("ivsNum", ConvUtils.convToString(productRow.get("ENTERPRISE_ORG_CODE")));//企业组织机构代码
			productDetail.put("estDt", ConvUtils.convToString(productRow.get("EST_DT")));//成立时间
			productDetail.put("industry", ConvUtils.convToString(productRow.get("INDUSTRY")));//行业类别
			productDetail.put("productcontracts", ConvUtils.convToString(productRow.get("PRODUCTCONTRACTS")));//产品合同
			productDetail.put("productprospectus", ConvUtils.convToString(productRow.get("PRODUCTPROSPECTUS")));//产品募集说明书
			productDetail.put("investmentconfirmation", ConvUtils.convToString(productRow.get("INVESTMENTCONFIRMATION")));//投资确认书
			productDetail.put("productsignbook", ConvUtils.convToString(productRow.get("PRODUCTSIGNBOOK ")));//电子签名约定书

			productDetail.put("riskLvl", ConvUtils.convToString(productRow.get("RISK_LVL")));//风险级别
			productDetail.put("state", ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST")));//项目状态
			String state = ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST"));
			if("1".equals(state)){//认购
				productDetail.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_SUBS_AMT"))));//最低认购金额
			}else if("2".equals(state)){//申赎
				productDetail.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_BIDS_AMT"))));//最低认购金额
			}else{
				productDetail.put("minAmount", "0");//最低认购金额
			}
			if("1".equals(state)){//认购
				productDetail.put("addAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SUBS_ADD_AMT"))));//个人认购追加金额(元)
			}else if("2".equals(state)){//申赎
				productDetail.put("addAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("BIDS_ADD_AMT"))));//个人申购追加金额(元)
			}else{
				productDetail.put("addAmount", "1");//个人追加金额
			}
			productDetail.put("fistMinAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
			productDetail.put("allotmentShares", ConvUtils.convToString(productRow.get("SELL_SHARE")));//出让股份
			productDetail.put("shareholdersNum", ConvUtils.convToString(productRow.get("SHAREHOLDERS_NUM")));//股东人数
			productDetail.put("maxSubsAmt",StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MAX_SUBS_AMT"))));//最大认购金额
			//productDetail.put("issBgnDt", ConvUtils.convToString(productRow.get("ISS_BGN_DT")));//发行开始日期
			//productDetail.put("issEndDt", ConvUtils.convToString(productRow.get("ISS_END_DT")));//发行结束日期
			if(StringUtils.isEmpty(bgntime)){
				productDetail.put("issBgnDt",bgntime);
			}else{
				if(bgntime.indexOf("-")>0){
					productDetail.put("issBgnDt", bgntime);//项目发起时间
				}else{
					productDetail.put("issBgnDt", bgntime.substring(0,4)+"-"+ bgntime.substring(4,6)+"-"+ bgntime.substring(6,8));//项目发起时间
				}
			}

			String endtime = ConvUtils.convToString(productRow.get("ISS_END_DT"));
			if(StringUtils.isEmpty(endtime)){
				productDetail.put("issEndDt",endtime);
			}else{
				if(bgntime.indexOf("-")>0){
					productDetail.put("issEndDt", endtime);//项目发起时间
				}else{
					productDetail.put("issEndDt", endtime.substring(0,4)+"-"+ bgntime.substring(4,6)+"-"+ bgntime.substring(6,8));//项目发起时间
				}
			}
			productDetail.put("dvidMd", ConvUtils.convToString(productRow.get("DVID_MD")));//分红方式

			productDetail.put("salesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SALES_QUOTA"))));//总销售额度(元)
			productDetail.put("surplusSalesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SURPLUS_SALES_QUOTA"))));//总销售额度(元)
			productDetail.put("projectMainTitle", ConvUtils.convToString(productRow.get("PROJECT_MAIN_TITLE")));//产品主标题
			List productIntroduceList = new ArrayList();
			productDetail.put("productIntroduce", productIntroduceList);//产品介绍
			if("0".equals(productDetail.get("maxSubsAmt"))){
				productDetail.put("maxSubsAmt","不限");
			}
			for (Row row : productListRow) {
				productIntroduceMap = new HashMap();
				if (!row.isEmpty()) {
					if("1".equals(type)){
						productIntroduceMap.put("partyNum", ConvUtils.convToString(row.get("PARTY_NUM")));//产品介绍序号
						productIntroduceMap.put("introduceName", ConvUtils.convToString(row.get("TITLE")));//产品介绍标题
						productIntroduceMap.put("introduceValues", ConvUtils.convToString(row.get("SUMMARYTEXT")));//产品介绍内容
					}else if("2".equals(type)){
						String time = ConvUtils.convToString(row.get("PROGRESS_DATE"));
						productIntroduceMap.put("time", time.substring(0,10));//时间轴日期
						productIntroduceMap.put("timeYear", time.substring(0,4));//时间轴月日
						productIntroduceMap.put("timeDate", time.substring(5,10));//时间轴年
						productIntroduceMap.put("introduceValues", ConvUtils.convToString(row.get("SUMMARYTEXT")));//时间轴内容
						productIntroduceMap.put("imgPathList", ConvUtils.convToString(row.get("IMG_PATH")).split(","));//图片集合分号间隔
					}
					productIntroduceList.add(productIntroduceMap);
				}
			}
			//查询用户是否是首次购买该产品
		}else{
			return null;
		}
		return productDetail;
	}
	//查询单个项目跟投人信息
	@Override
	public List selProductInvestor(String productId,int sumNum,int pageSize) {
		List<Row> productRow = productDao.selProductInvestor(productId,sumNum,pageSize);//查询产品跟投人
		Map productInvestor=null;
		List productInvestorList = new ArrayList();
		for (Row row : productRow) {
			if (!row.isEmpty()) {
			productInvestor = new HashMap();
			productInvestor.put("oid", ConvUtils.convToString(row.get("oid")));//产品id
			productInvestor.put("amount", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("amount"))));//投资金额
			productInvestor.put("insDate", ConvUtils.convToString(row.get("insDate")).substring(0, 10));//投资时间
			productInvestor.put("nickName", ConvUtils.convToString(row.get("nickName")));//用户昵称
			productInvestor.put("userName", StringUtil.userNameMask(ConvUtils.convToString(row.get("userName"))));//用户姓名
			productInvestor.put("iconfile", ConvUtils.convToString(row.get("iconfile")));//头像路径
			productInvestorList.add(productInvestor);
			}
		}
		return productInvestorList;
	}
	//查询单个项目跟投数量
	@Override
	public int countProductInvestor(String productId) {
		return productDao.countProductInvestor(productId);//查询产品跟投人
	}

	//查询符合投资人风险等级的项目
	@Override
	public List<Map> userRiskLvlAccordPro(String userId){
		List<Row> productRow = productDao.userRiskLvlAccordPro(userId);
		List productInvestorList = new ArrayList();
		for (Row row : productRow) {
			if (!row.isEmpty()) {
			Map productInvestor = new HashMap();
			String platformProjectsSt = ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST"));//产品状态
			productInvestor.put("proId", ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
			productInvestor.put("platformProjectsShortNm", ConvUtils.convToString(row.get("PLATFORM_PROJECTS_SHORT_NM")));//产品名称
			productInvestor.put("platformProjectsSt", platformProjectsSt);//产品状态
			/*if("1".equals(platformProjectsSt)){
				productInvestor.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("MIN_SUBS_AMT"))));//最低认购金额
			}else{
				productInvestor.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("MIN_BIDS_AMT"))));//最低申购金额
			}*/
			productInvestor.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
			productInvestor.put("riskLvl", ConvUtils.convToString(row.get("RISK_LVL")));//风险等级
			/*productInvestor.put("salesQuota", (int)Double.parseDouble((String)row.get("SALES_QUOTA"))/100);//总销售额度(分)
			productInvestor.put("surplusSalesQuota", (int)Double.parseDouble((String)row.get("SURPLUS_SALES_QUOTA"))/100);//剩余销售额度(分)
			*/
			productInvestor.put("salesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//总销售额度(元)
			productInvestor.put("surplusSalesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA"))));//剩余销售额度(元)
			productInvestorList.add(productInvestor);
			}
		}
		return productInvestorList;
	}
	//查询邀请人的投资信息
	@Override
	public List<Map> selIntroducerInvest(String userId) {
		List<Row> IntroducerInvest = productDao.selIntroducerInvest(userId);
		List IntroducerInvestList = new ArrayList();
		for (Row row : IntroducerInvest) {
			if (!row.isEmpty()) {
				Map presonMap = new HashMap();
				String status = ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST"));
				presonMap.put("mobile", ConvUtils.convToString(row.get("MOBILE")));
				presonMap.put("userName", ConvUtils.convToString(row.get("USER_NAME")));
				presonMap.put("insDate", ConvUtils.convToString(row.get("INS_DATE")));
				presonMap.put("platformProjectsShortNm", ConvUtils.convToString(row.get("PLATFORM_PROJECTS_SHORT_NM")));
				presonMap.put("platformProjectsSt", status);
				switch (status) {
				case "0":
					presonMap.put("platformProjectsStNm", "募集前");
					break;
				case "1":
					presonMap.put("platformProjectsStNm", "募集期");
					break;
				case "2":
					presonMap.put("platformProjectsStNm", "开放期");
					break;
				case "3":
					presonMap.put("platformProjectsStNm", "封闭期");
					break;
				case "4":
					presonMap.put("platformProjectsStNm", "清盘");
					break;
				default:
					break;
				}
				presonMap.put("amount", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("TENDER_AMOUNT"))));
				IntroducerInvestList.add(presonMap);
				}
			}
		return IntroducerInvestList;
	}
	@Override
	public Map selprojectAmount(String productId,String userId) {
		logger.info("ProductServiceImpl.selprojectAmount  start"+userId);
		//查询用户是否投资过该产品
		int tenderCount = userTenderDao.selUserTenderPro(productId,userId);
		logger.info("用户："+userId+"购买产品"+productId+"的次数"+tenderCount);
		Row productRow = productDao.selProductDetaile(productId);
		String state = ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST"));
		Map retMap = new HashMap();
		if(tenderCount>0){//投资过
			if("1".equals(state)){//认购
				retMap.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_SUBS_AMT"))));//最低认购金额
			}else if("2".equals(state)){//申赎
				retMap.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_BIDS_AMT"))));//最低认购金额
			}else{
				retMap.put("minAmount", "0");//最低认购金额
			}

		}else{//第一次投资
			retMap.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
		}
		if("1".equals(state)){//认购
			retMap.put("addAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SUBS_ADD_AMT"))));//个人认购追加金额(元)
		}else if("2".equals(state)){//申赎
			retMap.put("addAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("BIDS_ADD_AMT"))));//个人申购追加金额(元)
		}else{
			retMap.put("addAmount", "1");//个人追加金额
		}
		retMap.put("productcontracts", ConvUtils.convToString(productRow.get("PRODUCTCONTRACTS")));//产品合同
		retMap.put("productprospectus", ConvUtils.convToString(productRow.get("PRODUCTPROSPECTUS")));//产品募集说明书
		retMap.put("investmentconfirmation", ConvUtils.convToString(productRow.get("INVESTMENTCONFIRMATION")));//投资确认书
		retMap.put("productsignbook", ConvUtils.convToString(productRow.get("PRODUCTSIGNBOOK ")));//电子签名约定书
		return retMap;
	}

	//查询项目风险等级及app认购页数据
	@Override
	public Map selRiskLvlToBuy(String productId) {
		return productDao.selProjectToBuy(productId);//查询产品信息
	}
	//我要融资
	@Override
	public int addFinancing(Map financingMap) {
		return productDao.addFinancing(financingMap);
	}
}
