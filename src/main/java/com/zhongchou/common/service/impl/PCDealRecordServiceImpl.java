package com.zhongchou.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yanshang.util.Row;
import com.zhongchou.common.dao.IPCDealRecordDao;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.IPCDealRecordService;
import com.zhongchou.common.util.StringUtil;




/**
 * 个人中心逻辑的实现类。
 */
@Service
public class PCDealRecordServiceImpl implements IPCDealRecordService {
	Logger logger=Logger.getLogger(LoginController.class);

	@Autowired
	private IPCDealRecordDao dealRecordDao;


	@Override
	public int getDealRecordCnt(Map reqMap){

		return dealRecordDao.getDealRecordCnt(reqMap);

	}

	@Override
	public List getDealRecordList(Map reqMap) {
		List returnMessage = new ArrayList<>();
		List<Row> message = dealRecordDao.getDealRecordList(reqMap);
		for (Row row : message) {
			if (!row.isEmpty()) {
				Map dealRecordMap = new HashMap();
				dealRecordMap.put("tenderId", row.getString("tenderId"));//订单id
				dealRecordMap.put("projectMainTitle", row.getString("projectMainTitle"));//产品主标题
				dealRecordMap.put("date", row.getString("date"));//交易时间
				dealRecordMap.put("statusTp", row.getString("statusTp"));//交易类型1：购买  2：退款
				if("1".equals(row.getString("statusTp"))){
					dealRecordMap.put("amount", "-"+StringUtil.changeSalesQuota(row.getString("amount")));//购买
				}else{
					dealRecordMap.put("amount", "+"+StringUtil.changeSalesQuota(row.getString("amount")));//退款成功
				}
				
				dealRecordMap.put("tenderSsn", row.getString("tenderSsn"));//订单号
				returnMessage.add(dealRecordMap);
			}
		}
		return returnMessage;
	}


}
