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
import com.zhongchou.common.dao.PCProductListDao;
import com.zhongchou.common.dto.BannerDto;
import com.zhongchou.common.dto.PcGoodProductListDto;
import com.zhongchou.common.dto.PcProductListDto;
import com.zhongchou.common.service.PCProductListService;
import com.zhongchou.common.util.StringUtil;
@Service
public class PCProductListServiceImpl implements PCProductListService {
	Logger logger=Logger.getLogger(PCProductListServiceImpl.class);
	@Autowired
	private PCProductListDao pcproductListDao;

	@Override
	public List<PcProductListDto> getProjectList(int pageSize,int curPage) {
		logger.info("ProductServiceImpl.selProductInitList start");
		List<Row> rowList = pcproductListDao.selProductList(pageSize,curPage);
		List<PcProductListDto> retList = new ArrayList();
		for (Row row : rowList) {
			if (!row.isEmpty()) {
				String state = (String)row.get("PLATFORM_PROJECTS_ST");
				String lvl = (String)row.get("RISK_LVL");
				PcProductListDto PcProductListDto = new PcProductListDto();

				retList.add(PcProductListDto);
			}
		}
		return retList;
	}

	//分页条件查询产品列表
		@Override
		public List<PcProductListDto> selPcProductList(Map selMap) {
			List<Row> rowList = pcproductListDao.selPcProductList(selMap);
			List<PcProductListDto> retList = new ArrayList();
			SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd");
			Date dNow = new Date();
			Date dEnd = new Date();
			 Double a = 0.0 ;
			 Double b = 0.0 ;
			 String num ="0";
			String str=df.format(dNow);
			for (Row row : rowList) {
				if (!row.isEmpty()) {
					PcProductListDto PcProductListDto = new PcProductListDto();
					//oid_platform_projects_id
					PcProductListDto.setProductId(ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
					PcProductListDto.setSmallImgPath(ConvUtils.convToString(row.get("APP_LIST_IMG")));//列表缩略图
					PcProductListDto.setProjectAdds(ConvUtils.convToString(row.get("PROJECT_ADDS")));//项目所在地：上海
					PcProductListDto.setProjectcompanyName(ConvUtils.convToString(row.get("PROJECT_COMPANY_NAME")));//企业名称
					String[] characters = new String[]{};
					if(null!=row.getString("PROJECT_CHARACTER")&&""!=row.getString("PROJECT_CHARACTER")){
						 characters = row.getString("PROJECT_CHARACTER").split(",");
					}
					PcProductListDto.setProjectCharacter(characters);//项目特点（标签）
					PcProductListDto.setProjectMainTitle(ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//产品名称
					 String state = ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST"));
					PcProductListDto.setProjectState(state);//项目状态
					PcProductListDto.setFirstMinBuy(StringUtil.changebSalesQuota(ConvUtils.convToString(row.get("MIN_SUBS_AMT_DES"))));//个人最低认购金额(元)info表
					PcProductListDto.setSalesQuota(StringUtil.changebSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//募集金额(万元)
					//PcProductListDto.setSurplusQuota(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA")));//剩余销售额度info表
					if(null!=row.get("SALES_QUOTA")&&""!=row.get("SALES_QUOTA")){

						a =  Double.parseDouble(StringUtil.changeSalesQuota((String)row.get("SALES_QUOTA")));//(元)
					}
					if(null!=row.get("SURPLUS_SALES_QUOTA")&&""!=row.get("SURPLUS_SALES_QUOTA")){

						b =  Double.parseDouble(StringUtil.changeSalesQuota((String)row.get("SURPLUS_SALES_QUOTA")));//(元)
					}
					 	DecimalFormat dfNum = new DecimalFormat("0");//格式化小数
					 	if((a-b)>0){

					 		 num = dfNum.format((a-b)/a*100);//返回的是String类型
					 	}else{
					 		num = "0";
					 	}
			        PcProductListDto.setPercentage(num);//募集百分比
					PcProductListDto.setBuyTotalAmt(dfNum.format((a-b)/10000));//已认购额度(万元)
					PcProductListDto.setDeliveryTime(ConvUtils.convToString(row.get("ISS_END_DT_DES")));//剩余时间
					try {
						if(null!=row.get("ISS_END_DT_DES")&&""!=row.get("ISS_END_DT_DES")){
							dEnd = df.parse(ConvUtils.convToString(row.get("ISS_END_DT_DES")));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					long diff = dEnd.getTime()-dNow.getTime();
					if(diff>0){
						PcProductListDto.setRemainDays(String.valueOf(diff / (1000 * 60 * 60 * 24)));//剩余天数
					}else{
						PcProductListDto.setRemainDays("0");//剩余天数
					}
					PcProductListDto.setPayPersonNum(ConvUtils.convToString(row.get("payPersonNum")));//认筹人数

					retList.add(PcProductListDto);
				}
			}
			return retList;
		}

		//分页条件查询精品产品列表
				@Override
				public List<PcGoodProductListDto> selPcGoodProductList(Map selMap) {
					List<Row> rowList = pcproductListDao.selPcGoodProductList(selMap);
					List<PcGoodProductListDto> retList = new ArrayList();
					for (Row row : rowList) {
						if (!row.isEmpty()) {
							PcGoodProductListDto PcGoodProductListDto = new PcGoodProductListDto();
							//oid_platform_projects_id
							PcGoodProductListDto.setProductId(ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
							PcGoodProductListDto.setSmallImgPath(ConvUtils.convToString(row.get("APP_LIST_IMG")));//列表缩略图
							PcGoodProductListDto.setProjectAdds(ConvUtils.convToString(row.get("PROJECT_ADDS")));//项目所在地：上海
							PcGoodProductListDto.setProjectcompanyName(ConvUtils.convToString(row.get("PROJECT_COMPANY_NAME")));//企业签）
							PcGoodProductListDto.setProjectMainTitle(ConvUtils.convToString(row.get("PLATFORM_PROJECTS_SHORT_NM")));//产品名名称
							//PcGoodProductListDto.setProjectCharacter(ConvUtils.convToString(row.get("PROJECT_CHARACTER")));//项目特点（标称
							String[] characters = new String[]{};
							if(null!=row.getString("PROJECT_CHARACTER")&&""!=row.getString("PROJECT_CHARACTER")){
								 characters = row.getString("PROJECT_CHARACTER").split(",");
							}
							PcGoodProductListDto.setProjectCharacter(characters);//项目特点（标签）
							retList.add(PcGoodProductListDto);
						}
					}
					return retList;
				}
		//查询产品总数
		@Override
		public int countPcProduct(Map selMap) {
			return pcproductListDao.countPcProduct(selMap);
		}
		//查询精选产品总数
		@Override
		public int countPcGoodProduct(Map selMap) {
			return pcproductListDao.countPcGoodProduct(selMap);
		}
		//查询行业类别
		@Override
		public Set seldustryType() {
			List<Row> listRow = pcproductListDao.selindustryType();
			//List<Set> listSet = new ArrayList<Set>();
			Set proSet = new HashSet();
			for (Row row : listRow) {
				if (!row.isEmpty()) {
					proSet.add((String)row.get("INDUSTRY_NM"));
					}
				}
			return proSet;
		}
		//查询产品列表banner图
		@Override
		public List<BannerDto> getBannerList() {
			List<BannerDto> returnMessage = new ArrayList<BannerDto>();
			List<Row> message = pcproductListDao.getBannerList("2");//产品列表banner
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
}
