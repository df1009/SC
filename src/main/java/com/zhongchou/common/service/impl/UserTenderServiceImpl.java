package com.zhongchou.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanshang.config.Config;
import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.MessageUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCMessageDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dao.IUserTenderDao;
import com.zhongchou.common.dao.ProductDao;
import com.zhongchou.common.dto.InvestorDto;
import com.zhongchou.common.dto.OrderInfoDto;
import com.zhongchou.common.dto.ProductDetailDto;
import com.zhongchou.common.dto.UserTenderDto;
import com.zhongchou.common.service.IUserTenderService;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;
import com.zhongchou.common.zhongzhengSao.ProductDetailSao;
import com.zhongchou.common.zhongzhengSao.RedeemProductSao;


/**
 * 用户中心-订单管理
 */
@Service
public class UserTenderServiceImpl extends BaseSaoServiceImpl implements IUserTenderService {
	String defaultAvatarurl = Config.getString("defaultAvatarurl");
	Logger logger=Logger.getLogger(UserTenderServiceImpl.class);
	/**
	 * 用户的数据访问对象。
	 */
	@Autowired
	private IUserTenderDao userTenderDao;
	@Autowired
	private ProductDao productDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IPCMessageDao messageDao;

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的数量()
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * @return   list的总数   listCnt
	 */
	@Transactional(propagation = Propagation.NEVER)
	public int getUserTenderCnt(Map tenderMap){

		return userTenderDao.getUserTenderCnt(tenderMap);

	}

	/**
	 * 根据oid和用户的搜索内容获取已经投资项目的list数组数据
	 * 用户的IOD  oidUserId
	 * 产品的名称      projectName
	 * 认购时间         开始beginDate 结束 endDate
	 * 产品的状态      projectState
	 * 每页面的条数  pageSize
	 * 当前页数          curPage
	 */
	@Transactional(propagation = Propagation.NEVER)
	public List<UserTenderDto> getUserTenders(Map tenderMap){
		List<Row> rowList =  userTenderDao.getUserTenders(tenderMap);
		List<UserTenderDto> userTenderDtoList = new ArrayList<UserTenderDto>();
		String coolingPeriodTime = "150000";//HHmmss
		if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
			coolingPeriodTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
		}
		String endTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+coolingPeriodTime;
		for (Row row : rowList) {
			UserTenderDto userTenderDto = new UserTenderDto();
			userTenderDto.setOidTenderID(row.getString("oidTenderID"));
			userTenderDto.setTenderSsn((row.getString("TENDER_SSN")));
			userTenderDto.setProductName(row.getString("productName"));
			userTenderDto.setAmountStr(StringUtil.changeSalesQuota(row.getString("amountStr")));
			userTenderDto.setInsDate(DateUtils.convertDate2String((Date)row.get("insDateDetal"), "yyyy-MM-dd HH:mm"));
			userTenderDto.setProductStatus(row.getString("productStatus"));
			userTenderDto.setProductStatusName(getProductStateName(row.getString("productStatus")));
			userTenderDto.setTenderStatus(row.getString("tenderStatus"));
			userTenderDto.setProjectCode(row.getString("projectCode"));
			userTenderDto.setSmallImg(row.getString("smallImg"));
			String productStatus = row.getString("productStatus");
			String tenderStatus = row.getString("tenderStatus");
			String insDateDetal = row.getString("insDate");//yyyyMMddHHmmss
			//long XX = (long)StringUtil.getDistanceDays(insDateDetal);
			Date sysDate = DateUtils.getDateTime();
			//Date insDate = DateUtils.convertString2Date(insDateDetal,"yyyyMMddHHmmss");
			userTenderDto.setIsWithdraw(sysDate.before(DateUtils.convertString2Date(endTransactionDate,"yyyyMMddHHmmss"))
					&&DateUtils.convertDate2String(new Date(),"yyyyMMdd").equals(insDateDetal.substring(0,8))?"0":"1");
			//获取撤单状态
			Row rowRe = userTenderDao.getReturnAllState(row.getString("oidTenderID"),ConvUtils.convToString(tenderMap.get("oidUserId")));
			if (!rowRe.isEmpty()) {
				String returnState = rowRe.getString("TENDER_STATUS");
				if("3".equals(returnState)){
					userTenderDto.setTenderStatusName("失败");//撤单成功项目投资失败
					userTenderDto.setIsWithdraw("1");//撤单成功后不能撤单
				}else if("1".equals(returnState)){
					userTenderDto.setTenderStatusName("撤单中");
					userTenderDto.setIsWithdraw("1");//撤单后不能撤单
				}else if("0".equals(returnState)){
					userTenderDto.setTenderStatusName("撤单失败");
					if("3".equals(tenderStatus)){//中证返回投资成功
						userTenderDto.setTenderStatusName("成功");
					}else if("0".equals(tenderStatus)){//中证返回投资失败
						userTenderDto.setTenderStatusName("失败");
					}
				}else{
					userTenderDto.setTenderStatusName("超时");
				}
			}else{
			if("1".equals(productStatus)//募集期
					||"2".equals(productStatus)){//开放期
				if("1".equals(tenderStatus)){//投资中证成功
					if("0".equals(userTenderDto.getIsWithdraw())){
						userTenderDto.setTenderStatusName("冷静期");//当天3点前
					}else{
						userTenderDto.setTenderStatusName("进行中");//投资成功，中证未返回结果
					}
				}else if("3".equals(tenderStatus)){//中证返回投资成功
					userTenderDto.setTenderStatusName("成功");
				}else if("0".equals(tenderStatus)){//中证返回投资失败
					userTenderDto.setTenderStatusName("失败");
					userTenderDto.setIsWithdraw("1");//投资失败不能撤单
				}else{
					userTenderDto.setTenderStatusName("超时");
					userTenderDto.setIsWithdraw("1");//超时不能撤单
				}
			}else if("3".equals(productStatus)//封闭期
					||"4".equals(productStatus)){//清盘
				if("3".equals(tenderStatus)){
					userTenderDto.setTenderStatusName("成功");
					userTenderDto.setIsWithdraw("1");//非购买期不能撤单
				}else{
					userTenderDto.setTenderStatusName("失败");
					userTenderDto.setIsWithdraw("1");//非购买期不能撤单
				}
			}
		}
			userTenderDtoList.add(userTenderDto);
		}

		return userTenderDtoList;
	}

	//获取订单详情
		public OrderInfoDto getOrderInfo(String orderNum) {
			OrderInfoDto orderInfo = new OrderInfoDto();
			Row row = userTenderDao.getOrderInfo(orderNum);
				if (!row.isEmpty()) {
					orderInfo.setOrderStatus(ConvUtils.convToString(row.get("ORDER_STATUS")));//订单状态
					orderInfo.setOrderNum(ConvUtils.convToString(orderNum));//订单编号
					orderInfo.setTransactionNum(ConvUtils.convToString(row.get("TRANSACTION_NUM")));//交易编号
					orderInfo.setCreatTime(DateUtils.convertDate2String((Date)row.get("INS_DATE"), "yyyy-MM-dd HH:mm"));//创建时间
					orderInfo.setPaymentTime(DateUtils.convertDate2String((Date)row.get("PAY_TIME"), "yyyy-MM-dd HH:mm"));//付款时间
					orderInfo.setActualPayment(ConvUtils.convToString(row.get("ACTUAL_PAYMENT")));//实付金额
				}
			return orderInfo;
		}
		//获取跟投人list
		public List<InvestorDto> getInvestorList(String projectsId) {
			List<InvestorDto> returnMessage = new ArrayList<InvestorDto>();
			List<Row> message = userTenderDao.getInvestorList(projectsId);
			for (Row row : message) {
				if (!row.isEmpty()) {
					InvestorDto investorDto = new InvestorDto();
					investorDto.setImgPath(ConvUtils.convToString(row.get("USER_ICON_FILE_ID")));//头像路径
					if(StringUtils.isEmpty(investorDto.getImgPath())){
						investorDto.setImgPath(defaultAvatarurl);
					}
					investorDto.setInvestMoney(ConvUtils.convToString(row.get("TENDER_AMOUNT")));//跟投金额
					investorDto.setInvestorName(ConvUtils.convToString(row.get("NICKNAME")));//跟投人姓名
					investorDto.setInvestTime(ConvUtils.convToString(row.get("INS_DATE")));//跟投日期
					returnMessage.add(investorDto);
				}
			}
			return returnMessage;
		}

		//撤单操作

		@Override
		public Map returnOrder(Map reqMap) throws Exception {
			logger.info("用户:"+reqMap.get("loginId")+"撤单产品："+reqMap.get("projectCode")+"开始");
			// 返回的参数
			Map<String,Object> retMap = new HashMap<String,Object>();
			// 带入的参数
			Map<String,Object> setMap = new HashMap<String,Object>();
			//查询用户投资信息
			Map<String,Object> tenderMap = selTenderDetail(ConvUtils.convToString(reqMap.get("tenderId")),ConvUtils.convToString(reqMap.get("loginId")));
			if(tenderMap.isEmpty()){
				logger.info("用户:"+reqMap.get("loginId")+"撤单产品："+reqMap.get("projectCode")+"查询没有交易记录");
				retMap.put("rsp_code", "001");
				retMap.put("rsp_desc", "查无此单");
				return retMap;
			}
			// 返回的参数
			Map<String,Object> retMap1 = new HashMap<String,Object>();
			// 带入的参数
			Map<String,Object> setMap1 = new HashMap<String,Object>();
			String seq1 = nextSeq();
			RedeemProductSao redeemProductSao = new RedeemProductSao(seq1);
			setMap.put("loginId",reqMap.get("loginId") );//登录帐号
			setMap.put("reqSsn",ConvUtils.convToString(tenderMap.get("tenderSsn")));//原交易请求流水
			setMap.put("reqDate",ConvUtils.convToString(tenderMap.get("insDate")).replace("-", "").substring(0,8));//原交易请求日期
			setMap.put("authTp","1" );
			setMap.put("payPwd", MD5.encode(ConvUtils.convToString(reqMap.get("payPwd"))));
			/*setMap.put("yzmSsn",reqMap.get("reqSsn") );
			setMap.put("yzmContent",reqMap.get("checkCode") );*/
			setMap.put("productCd",tenderMap.get("proId"));
			setMap.put("userIp",reqMap.get("userIp"));

			// 调用接口，返回报文体
			retMap = redeemProductSao.setEncryptData(setMap);
			String redeemSsn = ConvUtils.convToString(DateUtils.convertDate2String(new Date(),"yyyyMMdd")+seq1);//撤单流水号
			tenderMap.put("redeemSsn", redeemSsn);
			tenderMap.put("loginId", reqMap.get("loginId"));
			//查询这个撤单信息是否存在
			//Row revokeTender = userTenderDao.getOrderCancellationRecord(ConvUtils.convToString(tenderMap.get("tenderSsn")),loginId);
			if("000000".equals(retMap.get("rsp_code"))){//成功
				logger.info("用户:"+reqMap.get("loginId")+"撤单产品："+reqMap.get("projectCode")+"成功");
				tenderMap.put("status", "3");
				userTenderDao.insOrderCancellationRecord(tenderMap);
				//插入退款记录
				tenderMap.put("type", "2");
				userTenderDao.insRefund(tenderMap);
				//新增用户的撤单消息
				Map msgMap = new HashMap();
				msgMap.put("msgTitle",MessageUtils.getMessage("MSG0002"));
				msgMap.put("styleTitle",reqMap.get("projectName")+";"+reqMap.get("projectCode"));
				msgMap.put("msgContent","去看看");
				msgMap.put("msgType","2");//项目消息
				msgMap.put("msgStuType","1");//1:项目状态消息
				msgMap.put("oidUserId",reqMap.get("loginId"));
				msgMap.put("oidUserId",reqMap.get("loginId"));
				messageDao.insMessage(msgMap);
				//更新中证产品数据
				ProductDetailSao proSao = new ProductDetailSao(nextSeq());
				Map proMap = new HashMap();
				proMap.put("productCd", reqMap.get("projectCode"));
				proMap.put("pageNo", "1");
				proMap.put("pageSize", "5");
				List retList = (List)proSao.setEncryptData(proMap).get("productList");
				ProductDetailDto proDetilDto = new ProductDetailDto();
				if(retList != null && retList.size()>0){
					proDetilDto = (ProductDetailDto)retList.get(0);
				}
				String surplusQuota = proDetilDto.getSurplus_sales_quota();//当前项目剩余投资额
				if(!"-1".equals(surplusQuota)){//不限
					productDao.updateProductToBuy(surplusQuota,ConvUtils.convToString(reqMap.get("projectCode")));
				}
	            return retMap;
			}else if("000010".equals(retMap.get("rsp_code"))
					||"000001".equals(retMap.get("rsp_code"))
					||"000002".equals(retMap.get("rsp_code"))){//超时或异常
				logger.info("用户:"+reqMap.get("loginId")+"撤单产品："+reqMap.get("projectCode")+"超时");
				tenderMap.put("status", "2");
				userTenderDao.insOrderCancellationRecord(tenderMap);
				return retMap;
			}else{//失败
				logger.info("用户:"+reqMap.get("loginId")+"撤单产品："+reqMap.get("projectCode")+"失败");
				if("020403".equals(retMap.get("rsp_code"))
						&&"原交易为不可撤单状态".equals(retMap.get("rsp_desc"))){
					tenderMap.put("status", "3");//撤单成功
					userTenderDao.updateOrderCancellationRecord(tenderMap);
				}else{
					tenderMap.put("status", "0");
					userTenderDao.insOrderCancellationRecord(tenderMap);
				}
				return retMap;
			}

		}

	/*	public void updateTender(Map<String,Object> tenderMap){
			if(revokeTender.isEmpty()){
				userTenderDao.insOrderCancellationRecord(tenderMap);
			}else{
				userTenderDao.updateOrderCancellationRecord(tenderMap);
			}
			userTenderDao.insOrderCancellationRecord(tenderMap);
		}*/

		private Map<String,Object> selTenderDetail(String tenderId,String userId){

			Map<String,Object> param = new HashMap<String,Object>();
			Map<String,Object> userTender = userTenderDao.getUserTender(tenderId,userId);
			if(userTender != null){
				param.put("tenderId",tenderId);//投资id
				param.put("proId",userTender.get("OID_PLATFORM_PROJECTS_ID"));//产品id
				param.put("userId",userTender.get("OID_USER_ID"));//用户id
				param.put("tenderTy",String.valueOf(userTender.get("TENDER_TYPE")));//投资类型
				param.put("amount",userTender.get("TENDER_AMOUNT"));//金额
				param.put("autoStatus",userTender.get("AUTO_STATUS"));//托管状态
				param.put("reqNo",userTender.get("REQUEST_NO"));//托管请求流水号
				param.put("contractNo",userTender.get("CONTRACT_NO"));//投资合同编号
				param.put("tenderSsn",userTender.get("TENDER_SSN"));//投资订单流水号
				param.put("insDate",userTender.get("INS_DATE"));//投资时间
			}
			return param;
		}


		public String getProductStateName(String productState){
			String productStateName = "";
			if("0".equals(productState)){
				productStateName = "募集前";
			}else if("1".equals(productState)){
				productStateName = "募集期";
			}else if("2".equals(productState)){
				productStateName = "开放期";
			}else if("3".equals(productState)){
				productStateName = "封闭期";
			}else if("4".equals(productState)){
				productStateName = "清盘";
			}
			return productStateName;
		}

		@Override
		public Map selProductDetailList(String productId,String type) {
			Row productRow = userTenderDao.selProductDetaile(productId);
			List<Row> productListRow = userTenderDao.selProductIntroduce(productId,type);
			Map productIntroduceMap = null;
			Map productDetail=null;
			if(!productRow.isEmpty()){
				productDetail = new HashMap();
				productDetail.put("platformProjectsFullNm", ConvUtils.convToString(productRow.get("PLATFORM_PROJECTS_FULL_NM")));//产品全称
				productDetail.put("summaryText", ConvUtils.convToString(productRow.get("SUMMARYTEXT")));//文字介绍
				productDetail.put("linkUrl", ConvUtils.convToString(productRow.get("LINK_URL")));//项目视频介绍
				productDetail.put("ivsName", ConvUtils.convToString(productRow.get("IVS_NAME")));//项目发起人
				productDetail.put("imgPath", ConvUtils.convToString(productRow.get("IMG_PATH")));//封面图片路径
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
					productDetail.put("addAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SUBS_ADD_AMT"))));//个人认购追加金额(元)
				}else if("2".equals(state)){//申赎
					productDetail.put("addAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("BIDS_ADD_AMT"))));//个人申购追加金额(元)
				}else{
					productDetail.put("addAmount", "1");//个人追加金额
				}

				productDetail.put("minAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("FIRST_MIN_BUY"))));//首次最低认购金额(元)
				productDetail.put("salesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SALES_QUOTA"))));//总销售额度(元)
				productDetail.put("surplusSalesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("SURPLUS_SALES_QUOTA"))));//剩余销售额度(元)
				productDetail.put("allotmentShares", ConvUtils.convToString(productRow.get("SELL_SHARE")));//出让股份
				productDetail.put("shareholdersNum", ConvUtils.convToString(productRow.get("SHAREHOLDERS_NUM")));//股东人数
				productDetail.put("maxSubsAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("MAX_SUBS_AMT"))));//最大认购金额(元)
				productDetail.put("issBgnDt", ConvUtils.convToString(productRow.get("ISS_BGN_DT")));//发行开始日期
				productDetail.put("issEndDt", ConvUtils.convToString(productRow.get("ISS_END_DT")));//发行结束日期
				productDetail.put("dvidMd", ConvUtils.convToString(productRow.get("DVID_MD")));//分红方式
				productDetail.put("lastState", ConvUtils.convToString(productRow.get("LAST_STATE")));//项目状态0：初审  1：通过初审

				if("0".equals(productDetail.get("salesQuota"))){//目标金额为0
					productDetail.put("salesQuota", "不限");
				}
				if("0".equals(productDetail.get("maxSubsAmt"))){//最大投资金额为0
					productDetail.put("maxSubsAmt", "不限");
				}
				if("0".equals(productDetail.get("minAmount"))){//最大投资金额为0
					productDetail.put("minAmount", "不限");
				}
				List productIntroduceList = new ArrayList();
				productDetail.put("productIntroduce", productIntroduceList);//产品介绍
				for (Row row : productListRow) {
					productIntroduceMap = new HashMap();
					if (!row.isEmpty()) {
						if("1".equals(type)){
							productIntroduceMap.put("id", ConvUtils.convToString(row.get("PROJECTS_INFO_ID")));//产品介绍id
							productIntroduceMap.put("partyNum", ConvUtils.convToString(row.get("PARTY_NUM")));//产品介绍序号
							productIntroduceMap.put("introduceName", ConvUtils.convToString(row.get("TITLE")));//产品介绍标题
							productIntroduceMap.put("introduceValues", ConvUtils.convToString(row.get("SUMMARYTEXT")));//产品介绍内容
						}else if("2".equals(type)){
							String time = ConvUtils.convToString(row.get("PROGRESS_DATE"));
							productIntroduceMap.put("time", time.substring(0,10));//时间轴日期
							productIntroduceMap.put("timeYear", time.substring(0,4));//时间轴年
							productIntroduceMap.put("timeDate", time.substring(5,10));//时间轴月日
							productIntroduceMap.put("introduceValues", ConvUtils.convToString(row.get("SUMMARYTEXT")));//时间轴内容
							if(!StringUtils.isEmpty(row.getString(("IMG_PATH")))){
								String[] imgs = ConvUtils.convToString(row.get("IMG_PATH")).split(",");//图片集合分号间隔
								productIntroduceMap.put("imgPathList", imgs);
							}else{
								productIntroduceMap.put("imgPathList", new ArrayList<>());
							}
							productIntroduceMap.put("status", ConvUtils.convToString(row.get("STATUS")));//进展状态
							productIntroduceMap.put("id", ConvUtils.convToString(row.get("PROJECTS_INFO_ID")));//进展id

						}
						productIntroduceList.add(productIntroduceMap);
					}
				}
			}else{
				return null;
			}
			return productDetail;
		}

		@Override
		public List selProductInvestor(String productId) {
			List<Row> productRow = userTenderDao.selProductInvestor(productId);//查询产品跟投人
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
				if(StringUtils.isEmpty((String)productInvestor.get("iconfile"))){
					productInvestor.put("iconfile",defaultAvatarurl);
				}
				productInvestorList.add(productInvestor);
				}
			}
			return productInvestorList;
		}

		@Override
		public Map<String,Object> getProductinfo(String tenderId,String loginId) {

			Map<String,Object> map = new HashMap<String,Object>();

			Row productRow = userTenderDao.getProductinfo(tenderId,loginId);//项目基本信息

			if(!productRow.isEmpty()){
				map.put("projectName", (String) productRow.get("PLATFORM_PROJECTS_SHORT_NM"));
				map.put("tenderStatus", (String) productRow.get("TENDER_STATUS"));
				map.put("tenderCode", (String) productRow.get("OID_TENDER_ID"));
				map.put("tenderSsn", (String) productRow.get("TENDER_SSN"));
				map.put("insDate", DateUtils.convertDate2String((Date) productRow.get("INS_DATE"), "yyyy-MM-dd HH:mm"));
				map.put("tenderDate", DateUtils.convertDate2String((Date) productRow.get("INS_DATE"), "yyyy-MM-dd HH:mm"));
				map.put("tenderAmount", StringUtil.changeSalesQuota(ConvUtils.convToString(productRow.get("TENDER_AMOUNT"))));
				String productStatus = productRow.getString("PLATFORM_PROJECTS_ST");
				String tenderStatus = productRow.getString("TENDER_STATUS");
				String insDateDetal = productRow.getString("insDate");//yyyyMMddHHmmss
				Date sysDate = DateUtils.getDateTime();
				String coolingPeriodTime = "150000";//HHmmss
				if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
					coolingPeriodTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
				}
				String endTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+coolingPeriodTime;
				String coolingPeriodFlag = sysDate.before(DateUtils.convertString2Date(endTransactionDate,"yyyyMMddHHmmss"))
						&&DateUtils.convertDate2String(new Date(),"yyyyMMdd").equals(insDateDetal.substring(0,8))?"0":"1";

				//获取撤单状态
				Row rowRe = userTenderDao.getReturnAllState(tenderId,loginId);
				if (!rowRe.isEmpty()) {
					String returnState = rowRe.getString("TENDER_STATUS");
					if("3".equals(returnState)){
						map.put("tenderStName","失败");//撤单成功项目投资失败
					}else if("1".equals(returnState)){
						map.put("tenderStName","撤单中");
					}else if("0".equals(returnState)){
						map.put("tenderStName","撤单失败");
						if("3".equals(tenderStatus)){//中证返回投资成功
							map.put("tenderStName","成功");
						}else if("0".equals(tenderStatus)){//中证返回投资失败
							map.put("tenderStName","失败");
						}
					}else{
						map.put("tenderStName","超时");
					}
				}else{
				if("1".equals(productStatus)//募集期
						||"2".equals(productStatus)){//开放期
					if("1".equals(tenderStatus)){//投资中证成功
						if("0".equals(coolingPeriodFlag)){
							map.put("tenderStName","冷静期");//当天3点前
						}else{
							map.put("tenderStName","进行中");//投资成功，中证未返回结果
						}
					}else if("3".equals(tenderStatus)){//中证返回投资成功
						map.put("tenderStName","成功");
					}else if("0".equals(tenderStatus)){//中证返回投资失败
						map.put("tenderStName","失败");
					}else{
						map.put("tenderStName","超时");
					}
				}else if("3".equals(productStatus)//封闭期
						||"4".equals(productStatus)){//清盘
					if("3".equals(tenderStatus)){
						map.put("tenderStName","成功");
					}else{
						map.put("tenderStName","失败");
					}
				}
			}
			}
			return map;
		}
		//app个人中心信息
		@Override
		public Map selPresonMsgToApp(String userId) {
			Map retMap = new HashMap();
			int allTenderAmount = userTenderDao.selAllTenderAmount(userId);
			int allTenderCount = userTenderDao.selTenderCount(userId);
			retMap.put("allTenderAmount", allTenderAmount);
			retMap.put("allTenderCount", allTenderCount);
			return retMap;
		}

		//模糊查询用户投资信息
		@Override
		public List likeTender(String userId, String proName) {
			Map retMap = new HashMap();
			List retList = new ArrayList<>();
			List<Row> tenderList = userTenderDao.likeTender(userId,proName);
			for (Row row : tenderList) {
				if (!row.isEmpty()) {
					retMap = new HashMap();
					retMap.put("tenderId", ConvUtils.convToString(row.get("OID_TENDER_ID")));//产品id
					retMap.put("proName", ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//产品名称
					retMap.put("projectsId", ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品code（id）
					retList.add(retMap);
				}
			}
			return retList;
		}

		//查询用户投资该产品的总额
		@Override
		public int selTenderProAmount(String userId,String proId) {
			return userTenderDao.countUserTenderAmount(userId,proId);
		}
		/**
		 *查询用户银行卡剩余可用额度
		 */
		public Map selRemainingQuota(String userId,String proId){
			//用户当日投资金额
			int tenderAmt = userTenderDao.countUserTenderAmountToday(userId);
			logger.info("tenderAmt:"+tenderAmt);
			//用户银行卡限额 userId
			Row productRow =userDao.selDayLimitAmt(userId);
			int dayLimitAmt = Integer.parseInt(StringUtil.doubleDecimalFormat(productRow.getDouble("amt")));
			logger.info("dayLimitAmt:"+dayLimitAmt);
			Map quotaMap = new HashMap();
			quotaMap.put("dayLimitAmt", dayLimitAmt-tenderAmt);
			quotaMap.put("singleLimit", StringUtil.changeSalesQuota(productRow.getString("singleLimit")));
			return quotaMap;
		}
}
