package com.zhongchou.common.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.zhongchou.common.dao.APPProductListDao;
import com.zhongchou.common.dto.APPProductListDto;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.PcGoodProductListDto;
import com.zhongchou.common.service.APPProductListService;
import com.zhongchou.common.util.StringUtil;
@Service
public class APPProductListServiceImpl implements APPProductListService {
	Logger logger=Logger.getLogger(APPProductListServiceImpl.class);
	@Autowired
	private APPProductListDao appproductListDao;

	@Override
	public List<APPProductListDto> getProjectList(int pageSize,int curPage) {
		logger.info("ProductServiceImpl.selProductInitList start");
		List<Row> rowList = appproductListDao.selProductList(pageSize,curPage);
		List<APPProductListDto> retList = new ArrayList();
		for (Row row : rowList) {
			if (!row.isEmpty()) {
				String state = (String)row.get("PLATFORM_PROJECTS_ST");
				String lvl = (String)row.get("RISK_LVL");
				APPProductListDto APPProductListDto = new APPProductListDto();

				retList.add(APPProductListDto);
			}
		}
		return retList;
	}

	//分页查询产品列表
		@Override
		public List<APPProductListDto> selAppProductList(Map selMap) {
			List<Row> rowList = appproductListDao.selAppProductList(selMap);
			List<APPProductListDto> retList = new ArrayList();
			SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
			Date dNow = new Date();
			Date dEnd = new Date();
			Double a = 0.0;
			Double b = 0.0;
			String num ="0";
			String str=df.format(dNow);
			for (Row row : rowList) {
				if (!row.isEmpty()) {
					APPProductListDto APPProductListDto = new APPProductListDto();
					//oid_platform_projects_id
					APPProductListDto.setProductId(ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
					APPProductListDto.setSmallImgPath(ConvUtils.convToString(row.get("APP_LIST_IMG")));//列表缩略图
					APPProductListDto.setProjectAdds(ConvUtils.convToString(row.get("PROJECT_ADDS")));//项目所在地：上海
					APPProductListDto.setProjectcompanyName(ConvUtils.convToString(row.get("PROJECT_COMPANY_NAME")));//企业名称
					String[] characters = new String[]{};
					if(null!=row.getString("PROJECT_CHARACTER")&&""!=row.getString("PROJECT_CHARACTER")){
						 characters = row.getString("PROJECT_CHARACTER").split(",");
					}
					APPProductListDto.setProjectCharacter(characters);//项目特点（标签）
					//APPProductListDto.setProjectCharacter(ConvUtils.convToString(row.get("PROJECT_CHARACTER")));//项目特点（标签）
					APPProductListDto.setProjectMainTitle(ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//产品名称
					APPProductListDto.setFirstMinBuy(StringUtil.changebSalesQuota(ConvUtils.convToString(row.get("MIN_SUBS_AMT_DES"))));//个人最低认购金额(元)info表
					APPProductListDto.setSalesQuota(StringUtil.changebSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//募集金额(万元)
					//APPProductListDto.setSurplusQuota(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA")));//剩余销售额度info表
					 if(null!=row.get("SALES_QUOTA")&&""!=row.get("SALES_QUOTA")){
						 a = Double.parseDouble(StringUtil.changebSalesQuota((String)row.get("SALES_QUOTA")));//元
					 }
					 if(null!=row.get("SURPLUS_SALES_QUOTA")&&""!=row.get("SURPLUS_SALES_QUOTA")){
						 b = Double.parseDouble(StringUtil.changebSalesQuota((String)row.get("SURPLUS_SALES_QUOTA")));//元
					 }
						DecimalFormat dfNum = new DecimalFormat("0.00");//格式化小数
						if((a-b)>0){

							num = dfNum.format((a-b)/a);//返回的是String类型
						}else{
							num = "0";
						}
				        APPProductListDto.setPercentage(num);//募集百分比
					APPProductListDto.setBuyTotalAmt(dfNum.format((a-b)/10000));//被购买的金额(万元)
					//APPProductListDto.setDeliveryTime(ConvUtils.convToString(row.get("DELIVERY_TIME")));//剩余时间
					try {
						if(null!=row.get("ISS_END_DT_DES")&&""!=row.get("ISS_END_DT_DES")){
							dEnd = df.parse(ConvUtils.convToString(row.get("ISS_END_DT_DES")));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					long diff = dEnd.getTime()-dNow.getTime();//关闭时间减去募集开始时间
					if(diff>0){
						APPProductListDto.setRemainDays(String.valueOf(diff / (1000 * 60 * 60 * 24)));//剩余天数
					}else{
						APPProductListDto.setRemainDays("0");//剩余天数
					}
					APPProductListDto.setPayPersonNum(ConvUtils.convToString(row.get("payPersonNum")));//认筹人数
					APPProductListDto.setProjectState(ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST")));//项目状态
					APPProductListDto.setSummaryText(ConvUtils.convToString(row.get("SUMMARYTEXT")));//文字介绍
					if(null==row.get("SHOW_PROJECT_FLAG")||"".equals(row.get("SHOW_PROJECT_FLAG"))){
						APPProductListDto.setShowProjectFlag(ConvUtils.convToString("0"));
					}else{
						APPProductListDto.setShowProjectFlag(ConvUtils.convToString(row.get("SHOW_PROJECT_FLAG")));//是否为展示项目
					}
					retList.add(APPProductListDto);
				}
			}
			return retList;
		}
		//分页条件查询精品产品列表
		@Override
		public List<APPProductListDto> selAppGoodProductList(Map selMap) {
			List<Row> rowList = appproductListDao.selAppGoodProductList(selMap);
			List<APPProductListDto> retList = new ArrayList();
			for (Row row : rowList) {
				if (!row.isEmpty()) {
					APPProductListDto APPProductListDto = new APPProductListDto();
					//oid_platform_projects_id
					APPProductListDto.setProductId(ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
					APPProductListDto.setSmallImgPath(ConvUtils.convToString(row.get("APP_LIST_IMG")));//列表缩略图
					APPProductListDto.setProjectAdds(ConvUtils.convToString(row.get("PROJECT_ADDS")));//项目所在地：上海
					APPProductListDto.setProjectcompanyName(ConvUtils.convToString(row.get("PROJECT_COMPANY_NAME")));//企业签）
					APPProductListDto.setProjectMainTitle(ConvUtils.convToString(row.get("PLATFORM_PROJECTS_SHORT_NM")));//产品名名称
					String[] characters = new String[]{};
					if(null!=row.getString("PROJECT_CHARACTER")&&""!=row.getString("PROJECT_CHARACTER")){
						 characters = row.getString("PROJECT_CHARACTER").split(",");
					}
					APPProductListDto.setProjectCharacter(characters);//项目特点（标签）
					APPProductListDto.setSummaryText(ConvUtils.convToString(row.get("SUMMARYTEXT")));//文字介绍
					APPProductListDto.setShowProjectFlag(ConvUtils.convToString(row.get("SHOW_PROJECT_FLAG")));//是否为展示项目
					retList.add(APPProductListDto);
				}
			}
			return retList;
		}
		//查询产品总数
		@Override
		public int countAppProduct(Map selMap) {
			return appproductListDao.countAppProduct(selMap);
		}
		//查询精选产品总数
		@Override
		public int countAppGoodProduct(Map selMap) {
			return appproductListDao.countAppGoodProduct(selMap);
		}
		//查询行业类别
				@Override
				public Set seldustryType() {
					List<Row> listRow = appproductListDao.selindustryType();
					//List<Set> listSet = new ArrayList<Set>();
					Set proSet = new HashSet();
					for (Row row : listRow) {
						if (!row.isEmpty()) {
							proSet.add((String)row.get("INDUSTRY_NM"));
							}
						}
					return proSet;
				}

				@Override
				public List<BannerDto> getBannerList() {
					// TODO Auto-generated method stub
					return null;
				}
}
