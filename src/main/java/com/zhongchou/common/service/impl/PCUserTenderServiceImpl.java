package com.zhongchou.common.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCUserTenderDao;
import com.zhongchou.common.dto.UserTenderDto;
import com.zhongchou.common.service.IPCUserTenderService;
import com.zhongchou.common.util.StringUtil;


/**
 * 用户中心-订单管理
 */
@Service
public class PCUserTenderServiceImpl extends BaseSaoServiceImpl implements IPCUserTenderService {
	String defaultAvatarurl = Config.getString("defaultAvatarurl");
	Logger logger=Logger.getLogger(PCUserTenderServiceImpl.class);
	/**
	 * 用户的数据访问对象。
	 */
	@Autowired
	private IPCUserTenderDao userTenderDao;

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
		List userTenderDtoList = new ArrayList<>();
		String coolingPeriodTime = "150000";//HHmmss
		if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
			coolingPeriodTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
		}
		String endTransactionDate = DateUtils.convertDate2String(new Date(),"yyyyMMdd")+coolingPeriodTime;
		for (Row row : rowList) {
			Map<String, Object> userTenderMap = new HashMap<String, Object>();
			userTenderMap.put("oidTenderID", row.getString("oidTenderID"));//订单号
			userTenderMap.put("smallImg", row.getString("smallImg"));//产品小图
			userTenderMap.put("productName", row.getString("productName"));//产品主标题
			userTenderMap.put("tenderAmount", StringUtil.changeSalesQuota(row.getString("tenderAmount")));//订单投资额
			userTenderMap.put("createDate", row.getString("insDateDetal"));//订单创建时间
			userTenderMap.put("productStatus", row.getString("productStatus"));//产品状态
			userTenderMap.put("productStatusName", getProductStateName(row.getString("productStatus")));
			userTenderMap.put("projectCode", row.getString("projectCode"));//产品编号
			userTenderMap.put("tenderSsn", row.getString("tenderSsn"));//订单流水号
			userTenderMap.put("insDate", row.getString("insDate"));//订单流水号
			String productStatus = row.getString("productStatus");
			String tenderStatus = row.getString("tenderStatus");//0 失败 1进行中 2超时  3成功  4创建订单  5取消订单
			String insDateDetal = row.getString("insDate");//yyyyMMddHHmmss
			//long XX = (long)StringUtil.getDistanceDays(insDateDetal);
			Date sysDate = DateUtils.getDateTime();
			//Date insDate = DateUtils.convertString2Date(insDateDetal,"yyyyMMddHHmmss");
			userTenderMap.put("isWithdraw", sysDate.before(DateUtils.convertString2Date(endTransactionDate,"yyyyMMddHHmmss"))
					&&DateUtils.convertDate2String(new Date(),"yyyy-MM-dd").equals(insDateDetal.substring(0,10))?"0":"1");
			//获取退款
			Row rowRe = userTenderDao.getReturnAmt(row.getString("oidTenderID"),ConvUtils.convToString(tenderMap.get("oidUserId")));
			String returnType = rowRe.getString("BACKAMT_TYPE");//退款类型 1购买失败  2撤单
			//String type = (String)tenderMap.get("type");//1:全部  2:支付成功  3:认购成功  4:购买成功  5:退款订单
			//userTenderMap.put("returnType", returnType);
			//只有全部和退款中才查退款
			if (!rowRe.isEmpty()&&(StringUtils.isEmpty((String)tenderMap.get("projectState"))||"0".equals(tenderMap.get("projectState")))) {
				String returnState = rowRe.getString("TENDER_STATUS");//退款状态 0：退款失败 1：退款中 2：退款超时 3：退款成功
				if("1".equals(returnState)){
					userTenderMap.put("tenderStatusName", "退款中");
					userTenderMap.put("isWithdraw", "1");
				}else if("3".equals(returnState)){
					userTenderMap.put("tenderStatusName", "退款成功");
					userTenderMap.put("isWithdraw", "1");
				}
				/*if("5".equals(type)){
					userTenderDtoList.add(userTenderMap);//退款订单
				}*/
			}else{
				if("1".equals(productStatus)//募集期
						||"2".equals(productStatus)){//开放期
					if("1".equals(tenderStatus)){//当天投资中证成功
						userTenderMap.put("tenderStatusName", "支付成功");
						/*if("2".equals(type)){
							userTenderDtoList.add(userTenderMap);//支付成功订单
						}*/
					}else if("3".equals(tenderStatus)){//中证返回投资成功
						userTenderMap.put("tenderStatusName", "认购成功");
						/*if("3".equals(type)){
							userTenderDtoList.add(userTenderMap);//认购成功订单
						}*/
					}else if("4".equals(tenderStatus)){//
						String endBusinessTime = "150000";//HHmmss
						if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
							endBusinessTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
						}
						DateFormat day = new SimpleDateFormat("yyyyMMdd");
						String format = day.format(new Date())+endBusinessTime;
						//订单30分钟剩余时间
						double orderTime = (row.getDate("overdueDate").getTime()-new Date().getTime())/1000;
						//系统最晚购买剩余时间
						double systemTime = (DateUtils.convertString2Date(format,"yyyyMMddHHmmss").getTime()-new Date().getTime())/1000;
						if(orderTime>systemTime){
							orderTime = systemTime;
						}
						if(orderTime<0){
							orderTime=0;
						}
						userTenderMap.put("RemainTime", orderTime);//订单剩余时间
						if(orderTime==0){
							userTenderMap.put("tenderStatusName", "订单关闭");
							userTenderMap.put("isWithdraw", "1");
						}else{
							userTenderMap.put("tenderStatusName", "待支付");
							userTenderMap.put("isWithdraw", "1");
						}
						/*if("2".equals(type)){
							userTenderDtoList.add(userTenderMap);//支付成功订单
						}*/
					}else if("5".equals(tenderStatus)){//
						userTenderMap.put("tenderStatusName", "订单关闭");
						userTenderMap.put("isWithdraw", "1");
						/*if("2".equals(type)){
							userTenderDtoList.add(userTenderMap);//支付成功订单
						}*/
					}else{
						userTenderMap.put("tenderStatusName", "订单确认中");
						userTenderMap.put("isWithdraw", "1");//超时不能撤单
					}
				}else if("3".equals(productStatus)//封闭期
						||"4".equals(productStatus)){//清盘
					if("3".equals(tenderStatus)){
						userTenderMap.put("tenderStatusName", "购买成功");
						userTenderMap.put("isWithdraw", "1");//非购买期不能撤单
						/*if("4".equals(type)){
							userTenderDtoList.add(userTenderMap);//购买成功订单
						}*/
					}
				}
			}
			userTenderDtoList.add(userTenderMap);//全部订单
			/*if("1".equals(type)){
				userTenderDtoList.add(userTenderMap);//全部订单
			}*/
		}

		return userTenderDtoList;
	}

	//订单详情
	@Override
	public Map<String,Object> getOrderdetailInfo(Map tenderMap) {
		Map<String,Object> map = new HashMap<String,Object>();

		Row productRow = userTenderDao.getUserTenders(tenderMap).get(0);//项目基本信息

		Map<String, Object> userTenderMap = new HashMap<String, Object>();
		if(!productRow.isEmpty()){
			userTenderMap.put("oidTenderID", productRow.getString("oidTenderID"));//订单号
			userTenderMap.put("tenderStatus", productRow.getString("tenderStatus"));//产品小图
			userTenderMap.put("smallImg", productRow.getString("smallImg"));//产品小图
			userTenderMap.put("tenderSsn", productRow.getString("tenderSsn"));//交易流水号
			userTenderMap.put("productName", productRow.getString("productName"));//产品主标题
			userTenderMap.put("tenderAmount", StringUtil.changeSalesQuota(productRow.getString("tenderAmount")));//订单投资额
			userTenderMap.put("insDate", productRow.getString("insDateDetal"));//订单创建时间
			userTenderMap.put("productStatus", productRow.getString("productStatus"));//产品状态
			userTenderMap.put("productStatusName", getProductStateName(productRow.getString("productStatus")));
			userTenderMap.put("projectCode", productRow.getString("projectCode"));//产品编号
			userTenderMap.put("issEndDate", productRow.getString("issEndDate"));//产品结束日期
			userTenderMap.put("payDate", productRow.getString("payDate"));//支付时间
			userTenderMap.put("overdueDate", productRow.getString("overdueDate1"));//订单关闭时间时间
			userTenderMap.put("confirmSuccessDate", productRow.getString("confirmSuccessDate"));//认购成功和失败时间（购买隔天同步中证认购状态）
			userTenderMap.put("confirmSuccessDate1", productRow.getString("confirmSuccessDate1"));//认购成功和失败时间（购买隔天同步中证认购状态）
			userTenderMap.put("confirmSuccessDate2", productRow.getString("confirmSuccessDate2"));//认购成功和失败时间（购买隔天同步中证认购状态）
			String productStatus = productRow.getString("productStatus");
			String tenderStatus = productRow.getString("tenderStatus");//0 失败 1进行中 2超时  3成功  4创建订单  5取消订单
			//订单关闭时间
			String endBusinessTime = "150000";//HHmmss
			if (ConvUtils.convToInt(Config.getString("coolingPeriod")) > 0) {
				endBusinessTime = ConvUtils.convToString(Config.getString("coolingPeriod"));
			}
			DateFormat day = new SimpleDateFormat("yyyyMMdd");
			String format = day.format(new Date())+endBusinessTime;
			//订单30分钟剩余时间
			double orderTime = (productRow.getDate("overdueDate").getTime()-new Date().getTime())/1000;
			//系统最晚购买剩余时间
			double systemTime = (DateUtils.convertString2Date(format,"yyyyMMddHHmmss").getTime()-new Date().getTime())/1000;
			if(orderTime>systemTime){
				orderTime = systemTime;
				userTenderMap.put("overdueDate", format.substring(4, 6)
						+"/"+format.substring(6, 8)
						+" "+format.substring(8, 10)
						+":"+format.substring(10, 12)
						);//订单关闭时间时间
			}
			if(orderTime<0){
				orderTime=0;
			}
			
			//获取退款
			Row rowRe = userTenderDao.getReturnAmt(productRow.getString("oidTenderID"),ConvUtils.convToString(tenderMap.get("oidUserId")));
			String returnType = rowRe.getString("BACKAMT_TYPE");//退款类型 1购买失败  2撤单
			String muji = "";//募集结束字段暂定
			userTenderMap.put("finalPayDate", "01-01 00:00");//购买成功 项目最终募集成功后（状态待定）
			//订单投资人信息
			userTenderMap.put("cardNo", StringUtils.encryptBankCard(productRow.getString("CARD_NO")));//购买用的银行卡号
			userTenderMap.put("bankNm", StringUtil.supportBank(productRow.getString("BANK")));//银行名
			if (!rowRe.isEmpty()) {
				userTenderMap.put("cancelDate", rowRe.getString("INS_DATE"));//撤单成功和退款中时间
				userTenderMap.put("finishDate", "");//退款完成时间
				String returnState = rowRe.getString("TENDER_STATUS");//退款状态 0：退款失败 1：退款中 2：退款超时 3：退款成功
				if("3".equals(returnState)){
					userTenderMap.put("finishDate", rowRe.getString("UPD_DATE"));//退款完成时间
				}
				if("1".equals(returnState)){
					userTenderMap.put("tenderStatusName", "退款中");
					if("1".equals(returnType)){//购买失败
						userTenderMap.put("statusType", 10);
					}else if("2".equals(returnType)){//撤单
						userTenderMap.put("statusType", 6);
					}else if("1".equals(muji)){
						userTenderMap.put("statusType", 8);//   8.募集失败退款中订单
					}
				}else if("3".equals(returnState)){
					userTenderMap.put("tenderStatusName", "退款完成");
					if("1".equals(returnType)){//购买失败
						userTenderMap.put("statusType", 11);
					}else if("2".equals(returnType)){//撤单
						userTenderMap.put("statusType", 7);
					}else if("1".equals(muji)){
						userTenderMap.put("statusType", 9);//   9.募集失败退款完成订单
					}
				}
			}else{
				if("1".equals(productStatus)//募集期
						||"2".equals(productStatus)){//开放期
					if("1".equals(tenderStatus)){//当天投资中证成功
						userTenderMap.put("tenderStatusName", "支付成功");
						userTenderMap.put("statusType", 3);

					}else if("3".equals(tenderStatus)){//中证返回投资成功
						userTenderMap.put("tenderStatusName", "认购成功");
						userTenderMap.put("statusType", 4);
						//2017-02-02
						String date = productRow.getString("insDateDetal").replace("-", "/").substring(5, 10);//隔天同步购买成功日月
						String time = "";
						userTenderMap.put("buySuccessDate",
								productRow.getString("insDateDetal").replace("-", "/"));
					}else if("4".equals(tenderStatus)){//
						userTenderMap.put("RemainTime", orderTime);//订单剩余时间
						if(orderTime==0){
							userTenderMap.put("tenderStatusName", "订单关闭");
							userTenderMap.put("statusType", 2);
						}else{
							userTenderMap.put("tenderStatusName", "待支付");
							userTenderMap.put("statusType", 1);
							
						}
					}else if("5".equals(tenderStatus)){//
						userTenderMap.put("tenderStatusName", "订单关闭");
						userTenderMap.put("statusType", 2);
					}else{
						userTenderMap.put("tenderStatusName", "订单确认中");
						userTenderMap.put("statusType", 12);
					}
				}else if("3".equals(productStatus)//封闭期
						||"4".equals(productStatus)){//清盘
					if("3".equals(tenderStatus)){
						userTenderMap.put("tenderStatusName", "购买成功");
						userTenderMap.put("statusType", 5);
					}
				}
			}
		}
		return userTenderMap;
	}

	public String getProductStateName(String productState){
		String productStateName = "";
		if("0".equals(productState)){
			productStateName = "预热前";
		}else if("1".equals(productState)){
			productStateName = "认购期";
		}else if("2".equals(productState)){
			productStateName = "认购期";
		}else if("3".equals(productState)){
			productStateName = "交割期";
		}else if("4".equals(productState)){
			productStateName = "退出期";
		}
		return productStateName;
	}
	//删除关闭的订单
	@Override
	public boolean delCloseOrder(String tenderId,String oidUserId){
		return userTenderDao.delCloseOrder(tenderId, oidUserId)>0;
	}
	
	//取消的订单
	@Override
	public boolean updCloseOrder(String tenderId,String oidUserId){
		return userTenderDao.updCloseOrder(tenderId, oidUserId)>0;
	}


}
