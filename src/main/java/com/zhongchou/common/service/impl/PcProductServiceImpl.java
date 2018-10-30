package com.zhongchou.common.service.impl;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.PcProductDao;
import com.zhongchou.common.service.PcProductService;
import com.zhongchou.common.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author gaoming
 * @create 2017-09-05-17:36
 **/
@Service
public class PcProductServiceImpl implements PcProductService {
    Logger logger=Logger.getLogger(ProductServiceImpl.class);
    @Autowired
    private PcProductDao pcProductDao;

    /**
     * /产品信息
     * @param productId
     * @return
     */
    @Override
    public Map getProductDetail(String productId) {
        Row productRow = pcProductDao.selProductDetaile(productId);

        Map productDetail=null;
        if(!productRow.isEmpty()){
            productDetail = new HashMap();
            if(StringUtils.isEmpty(productRow.getString("SHOW_PROJECT_FLAG"))){
            	productDetail.put("showProjectFlag", "0");//展示项目标识
            }else{
            	productDetail.put("showProjectFlag", ConvUtils.convToString(productRow.get("SHOW_PROJECT_FLAG")));//展示项目标识
            }
            productDetail.put("riskLvl", ConvUtils.convToString(productRow.get("RISK_LVL_DES")));//风险级别
            productDetail.put("state", ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST")));//项目状态
            String totalAmt = StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SALES_QUOTA")));
            productDetail.put("salesQuota",Double.valueOf(totalAmt)/10000);//目标筹资额(万元)
            productDetail.put("canShareholdersNum",productRow.getString("CAN_SHAREHOLDERS_NUM"));//可认购股东数
            productDetail.put("allotmentShares", ConvUtils.convToString(productRow.get("SELL_SHARE")));//出让股份
            String startTime = ConvUtils.convToString(productRow.get("ISS_BGN_DT_DES"));
            if(!StringUtils.isEmpty(startTime)){
                productDetail.put("issBgnDt", productRow.getString("ISS_BGN_DT_DES"));//开始时间
            }else {
                productDetail.put("issBgnDt","");//开始时间
            }
            String endTime = ConvUtils.convToString(productRow.get("ISS_END_DT_DES"));
            if(!StringUtils.isEmpty(endTime)){
                long dayTime = DateUtils.convertString2Date(endTime, "yyyy-MM-dd").getTime() - new Date().getTime();
                if(dayTime<0){
                    productDetail.put("remainDays","0");//剩余时间(天)
                }else {
                    long day = 24*60*60*1000;
                    productDetail.put("remainDays",Math.ceil(dayTime/day));//剩余时间(天)
                }
            }else {
                productDetail.put("remainDays","0");
            }
            DecimalFormat df = new DecimalFormat("0");
            String remainAmt = StringUtil.changeSalesQuota(productRow.getString("SURPLUS_SALES_QUOTA"));//剩余销售额度(元)
            Double totalAmount = Double.parseDouble(totalAmt);
            Double remainAmount = Double.parseDouble(remainAmt);
            productDetail.put("buyTotalAmt",df.format((totalAmount-remainAmount)/10000));//已认购总额(万元)
            if(totalAmount<=0){
            	productDetail.put("buyProgress","0");//完成度
            }else{
            	productDetail.put("buyProgress",df.format((totalAmount-remainAmount)/totalAmount*100));//完成度
            }
            String state = ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST"));
            productDetail.put("minPayAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_SUBS_AMT_DES"))));//最低认购金额
            productDetail.put("addAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SUBS_ADD_AMT_DES"))));//最小追加金额（元）
            productDetail.put("projectName",productRow.getString("PROJECT_COMPANY_NAME"));//TODO项目方
            productDetail.put("projectAdds",productRow.getString("PROJECT_ADDS"));//TODO项目方地址
            productDetail.put("projectMainTitle", ConvUtils.convToString(productRow.get("PROJECT_MAIN_TITLE")));//产品主标题
            productDetail.put("summaryText", ConvUtils.convToString(productRow.get("SUMMARYTEXT")));//文字介绍
            productDetail.put("linkUrl", ConvUtils.convToString(productRow.get("LINK_URL")));//项目视频介绍
            productDetail.put("imgPath", ConvUtils.convToString(productRow.get("IMG_PATH")));//项目背景大图
            productDetail.put("appImgPath", ConvUtils.convToString(productRow.get("APP_IMG")));//APP背景图
            if(StringUtils.isEmpty(productRow.getString("PROJECT_CHARACTER"))){
            	productDetail.put("projectCharacter",new ArrayList<>());//TODO环保,健康,盈利不错(项目特点)
            }else{
            	String[] characters = productRow.getString("PROJECT_CHARACTER").split(",");
            	productDetail.put("projectCharacter",characters);//TODO环保,健康,盈利不错(项目特点)
            }
            
            
            productDetail.put("majorBusiness", ConvUtils.convToString(productRow.get("MAJOR_BUSINESS")));//主营业务
            productDetail.put("financingPurpose", ConvUtils.convToString(productRow.get("FINANCING_PURPOSE")));//融资用途
            productDetail.put("financingMode", ConvUtils.convToString(productRow.get("FINANCING_MODE")));//融资方式
            productDetail.put("financingCost", ConvUtils.convToString(productRow.get("FINANCING_COST")));//融资成本
            productDetail.put("exitMode", ConvUtils.convToString(productRow.get("EXIT_MODE")));//退出方式
            productDetail.put("annotation", ConvUtils.convToString(productRow.get("ANNOTATION")));//注释

        }
        return productDetail;
    }
    /**
     * 项目介绍
     * @param productId
     * @return
     */
    @Override
    public List<Map<String, String>> getProductIntroduce(String productId) {
        //查询产品模块信息
        List<Row> productListRow = pcProductDao.selProductIntroduce(productId,"1");
        List<Map<String,String>> productIntroduceList = new ArrayList<>();
        for (Row row : productListRow) {
            Map<String,String>  productIntroduceMap = new HashMap();
            if (!row.isEmpty()) {
                productIntroduceMap.put("partyNum", ConvUtils.convToString(row.get("PARTY_NUM")));//产品介绍序号
                productIntroduceMap.put("introduceName", ConvUtils.convToString(row.get("TITLE")));//产品介绍标题
                productIntroduceMap.put("introduceValues", ConvUtils.convToString(row.get("SUMMARYTEXT")));//产品介绍内容
                productIntroduceList.add(productIntroduceMap);
            }
        }
        return productIntroduceList;
    }

    /**
     * 融资信息
     * @param productId
     * @return
     */
    @Override
    public Map getProductTradingInfo(String productId) {
        Row productRow = pcProductDao.selProductDetaile(productId);

        Map tradingInfo=null;
        if(!productRow.isEmpty()){
            tradingInfo = new HashMap();
            //企业信息
            Map<String,String> componyInfo = new HashMap();
            componyInfo.put("companyName",productRow.getString("PROJECT_COMPANY_NAME"));//企业名称
            componyInfo.put("companyPersonName",productRow.getString("ENTERPRISE_LEGAL_PERSON_NAME"));//企业法人
            componyInfo.put("companyHoldTime",productRow.getString("COMPANY_HOLD_TIME"));//TODO企业成立时间
            componyInfo.put("partnerNum",productRow.getString("SHAREHOLDERS_NUM"));//TODO股东人数
            componyInfo.put("companyCode",productRow.getString("ENTERPRISE_ORG_CODE"));//企业组织机构代码
            componyInfo.put("companyRegistAdds",productRow.getString("COMPANY_REGIST_ADDS"));//TODO企业注册地址
            componyInfo.put("companyTradeType",productRow.getString("INDUSTRY"));//企业行业类别
            //项目信息
            Map<String,Object> productInfo = new HashMap();
            productInfo.put("projectName",productRow.getString("PLATFORM_PROJECTS_FULL_NM"));//项目名称
            productInfo.put("riskLvl",productRow.getString("RISK_LVL_DES"));//风险等级
            productInfo.put("canShareholdersNum",productRow.getString("CAN_SHAREHOLDERS_NUM"));//可认购股东数
            productInfo.put("projectAdds",productRow.getString("PROJECT_ADDS"));//TODO项目所在地址
            productInfo.put("projectCode",productRow.getString("OID_PLATFORM_PROJECTS_ID"));//产品代码
            String targetAmt = StringUtil.changeSalesQuota(productRow.getString("SALES_QUOTA"));
            productInfo.put("targetAmt",Double.parseDouble(targetAmt)/10000);//TODO最低目标额度（万元）
            String minTargetAmt = StringUtil.changeSalesQuota(productRow.getString("MIN_TARGET_AMT"));
            productInfo.put("minTargetAmt",Double.parseDouble(minTargetAmt)/10000);//TODO最低目标额度（万元）
            String maxTargetAmt = StringUtil.changeSalesQuota(productRow.getString("MAX_TARGET_AMT"));
            productInfo.put("maxTargetAmt",Double.parseDouble(maxTargetAmt)/10000);//TODO最高目标额度（万元）
            String state = ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST"));
            productInfo.put("minPayAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_SUBS_AMT_DES"))));//最低认购金额
            productInfo.put("addAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SUBS_ADD_AMT_DES"))));//最小追加金额（元）
            productInfo.put("maxPayAmt",StringUtil.changeSalesQuota(productRow.getString("MAX_SUBS_AMT_DES")));//最大认购金额（元）
            productInfo.put("minSellingShare",productRow.getString("MIN_SELLING_SHARE"));//TODO最低出让股份
            productInfo.put("sellingShare",productRow.getString("SELL_SHARE"));//TODO出让股份
            productInfo.put("maxSellingShare",productRow.getString("MAX_SELLING_SHARE"));//TODO最高出让股份

            String endTime = ConvUtils.convToString(productRow.get("ISS_END_DT_DES"));
            if(!StringUtils.isEmpty(endTime)){
                productInfo.put("issEndDt",productRow.getString("ISS_END_DT_DES"));//发行结束日期
            }else {
                productInfo.put("issEndDt","");//发行结束日期
            }
            String startTime = ConvUtils.convToString(productRow.get("ISS_BGN_DT_DES"));
            if(!StringUtils.isEmpty(startTime)){
                productInfo.put("issBgnDt",productRow.getString("ISS_BGN_DT_DES"));//发行开始日期
            }else {
                productInfo.put("issBgnDt","");//发行开始日期
            }
            productInfo.put("productProspectus",productRow.getString("SC_PRODUCTPROSPECTUS"));//产品募集说明书

            tradingInfo.put("componyInfo",componyInfo);
            tradingInfo.put("productInfo",productInfo);
        }
        return tradingInfo;
    }

    /**
     * 项目进展
     * @param productId
     * @return
     */
    @Override
    public List<Map<String, Object>> getProductProgress(String productId) {
        //查询产品模块信息
        List<Row> productListRow = pcProductDao.selProductIntroduce(productId,"2");
        List<Map<String,Object>> productProgressList = new ArrayList<>();
        for (Row row : productListRow) {
            Map<String,Object> productProgressMap = new HashMap();
            if (!row.isEmpty()) {
                String time = ConvUtils.convToString(row.get("PROGRESS_DATE"));
                if(StringUtils.isEmpty(time)){
                	productProgressMap.put("time", "");//时间轴日期
                	productProgressMap.put("timeYear", "");//时间轴年月
                	productProgressMap.put("timeDate", "");//时间轴日
                }else{
                	productProgressMap.put("time", time.substring(0,10));//时间轴日期
                	productProgressMap.put("timeYear", time.substring(0,7));//时间轴年月
                	productProgressMap.put("timeDate", time.substring(8,10));//时间轴日
                }
                productProgressMap.put("introduceValues", ConvUtils.convToString(row.get("SUMMARYTEXT")));//时间轴内容
                String imgPath = ConvUtils.convToString(row.get("IMG_PATH"));
                if(StringUtils.isEmpty(imgPath)){
                	productProgressMap.put("imgPathList", new ArrayList<>());//图片集合分号间隔
                }else{
                	productProgressMap.put("imgPathList", ConvUtils.convToString(row.get("IMG_PATH")).split(","));//图片集合分号间隔
                }
                productProgressList.add(productProgressMap);
            }
        }
        return productProgressList;
    }
    /**
     * 展示其它项目信息列表
     * @param productId
     * @return
     */
    @Override
    public List getOtherProjectList(String productId) {
        List<Row> otherProjectList = pcProductDao.getOtherProjectList(productId);
        List<Map<String,Object>> payList = new ArrayList<>();
        List<Map<String,Object>> waitPayList = new ArrayList<>();
        List<Map<String,Object>> deliveryPayList = new ArrayList<>();
        List<Map<String,Object>> projectList = new ArrayList<>();
        if(otherProjectList!=null && otherProjectList.size()>0){
            for(Row row : otherProjectList){
                String projectState = row.getString("projectState");
                row.put("salesQuota",StringUtil.changebSalesQuota(row.getString("salesQuota")));
                row.put("firstMinBuy",StringUtil.changebSalesQuota(row.getString("firstMinBuy")));
                row.put("productId",row.getString("projectId"));

                Double totalAmount = Double.parseDouble(row.getString("salesQuota"));
                Double remainAmount = Double.parseDouble(StringUtil.changebSalesQuota(row.getString("surplusQuota")));
                DecimalFormat df = new DecimalFormat("0");
                if(totalAmount<=0){
                	row.put("buyTotalAmt", 0);
                }else{
                	row.put("buyTotalAmt", df.format((totalAmount-remainAmount)));
                }
                String[] characters = row.getString("PROJECT_CHARACTER").split(",");
                row.put("projectCharacter",characters);//TODO环保,健康,盈利不错(项目特点)
                long issEndDt = 0;
                if(!StringUtils.isEmpty(row.getString("issEndDt"))){
                	issEndDt = DateUtils.convertString2Date(row.getString("issEndDt"), "yyyy-MM-dd").getTime();
                }

                long nowtime = new Date().getTime();
                long time = issEndDt-nowtime;
                long daytime = 24*60*60*1000;
                if(time<0){
                    row.put("remainDays",0);
                }else {
                    row.put("remainDays",Math.ceil(time/daytime));
                }
                row.put("percentage","0");//完成度
                if("2".equals(projectState)||"1".equals(projectState)){//可认购期
                    if(totalAmount<=0){
                    	row.put("percentage","0");//完成度
                    }else{
                    	row.put("percentage",df.format((totalAmount-remainAmount)/totalAmount*100));//完成度
                    }
                    payList.add(row);
                }else if("0".equals(projectState)){//预热中
                    waitPayList.add(row);
                }else {
                    deliveryPayList.add(row);//募集成功
                }
            }

            projectList.addAll(payList);//可认购项目
            projectList.addAll(waitPayList);//预热中项目
            projectList.addAll(deliveryPayList);//已募集完成交割的项目
            if(projectList.size()>3){
                for(int i=3;i<projectList.size();i++){
                    projectList.remove(i);
                    i--;
                }
            }
        }
        return projectList;
    }

    //查询单个项目跟投数量
    @Override
    public int countProductInvestor(String productId) {
        return pcProductDao.countProductInvestor(productId);//查询产品跟投人
    }

    //查询单个项目跟投人信息
    @Override
    public List selProductInvestor(String productId,int sumNum,int pageSize) {
        List<Row> productRow = pcProductDao.selProductInvestor(productId,sumNum,pageSize);//查询产品跟投人
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


    @Override
    public Map selprojectAmount(String productId,String userId) {
        logger.info("ProductServiceImpl.selprojectAmount  start"+userId);
        //查询用户是否投资过该产品
        int tenderCount = pcProductDao.selUserTenderPro(productId,userId);
        logger.info("用户："+userId+"购买产品"+productId+"的次数"+tenderCount);
        Row productRow = pcProductDao.selProductDetaile(productId);
        String state = ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_ST"));
        Map retMap = new HashMap();
        String minAmount = StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MIN_SUBS_AMT_DES")));
        String addAmount = StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SUBS_ADD_AMT_DES")));
        String maxAmount = StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MAX_SUBS_AMT_DES")));
        retMap.put("minAmount", minAmount);//最低认购金额(元）
        retMap.put("addAmount", addAmount);//个人认购追加金额(元)
        retMap.put("maxAmount", maxAmount);//个人认购追加金额(元)
        retMap.put("productcontracts", ConvUtils.convToString(productRow.get("PRODUCTCONTRACTS")));//产品合同
        retMap.put("productprospectus", ConvUtils.convToString(productRow.get("SC_PRODUCTPROSPECTUS")));//产品募集说明书
        retMap.put("investmentconfirmation", ConvUtils.convToString(productRow.get("INVESTMENTCONFIRMATION")));//投资确认书
        retMap.put("productsignbook", ConvUtils.convToString(productRow.get("PRODUCTSIGNBOOK")));//电子签名约定书
        retMap.put("riskWarningBook", ConvUtils.convToString(productRow.get("RISKWARNINGBOOK")));//风险提示书
        retMap.put("userName", ConvUtils.convToString(productRow.get("GUIDE_COMPANY")));//领头人姓名
        return retMap;
    }

    //查询项目风险等级及app认购页数据
    @Override
    public Row selRiskLvlToBuy(String productId) {
        return pcProductDao.selProjectToBuy(productId);//查询产品信息
    }

}
