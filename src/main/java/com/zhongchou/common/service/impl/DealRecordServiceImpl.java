package com.zhongchou.common.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yanshang.util.DateUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IDealRecordDao;
import com.zhongchou.common.dao.IMyProjectDao;
import com.zhongchou.common.dao.IUserTenderDao;
import com.zhongchou.common.dto.DealRecordDto;
import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.IDealRecordService;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.util.StringUtil;




/**
 * 个人中心逻辑的实现类。
 */
@Service
public class DealRecordServiceImpl implements IDealRecordService {
	Logger logger=Logger.getLogger(LoginController.class);

	@Autowired
	private IDealRecordDao dealRecordDao;

	@Autowired
	private IUserTenderDao userTenderDao;

	@Override
	public int getDealRecordCnt(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName){

		return dealRecordDao.getDealRecordCnt(oidUserId, beginDate, endDate,dateType, projectType,projectName);

	}

	@Override
	public List<DealRecordDto> getDealRecordList(String oidUserId,String beginDate,String endDate,String dateType,String projectType,String projectName, int pageSize,int curPage) {
		List<DealRecordDto> returnMessage = new ArrayList<DealRecordDto>();
		List<Row> message = dealRecordDao.getDealRecordList(oidUserId, beginDate, endDate,dateType, projectType,projectName,pageSize,curPage);
		for (Row row : message) {
			if (!row.isEmpty()) {
				DealRecordDto myProjectlDto = new DealRecordDto();
				myProjectlDto.setTransactionDate(DateUtils.convertDate2String((Date)row.get("INS_DATE"), "yyyy-MM-dd HH:mm:ss"));
				//myProjectlDto.setDealType((String)row.get("TENDER_STATUS"));
				myProjectlDto.setTenderImg((String)row.get("APP_LIST_IMG"));
				myProjectlDto.setProjectName((String)row.get("PLATFORM_PROJECTS_SHORT_NM"));
				String cardNum = (String)row.get("CARD_NO");
				if(StringUtils.isEmpty(cardNum)){
					myProjectlDto.setIdCardNum("");
				}else{
					myProjectlDto.setIdCardNum(cardNum.substring(cardNum.length()-4,cardNum.length()));
				}

				Row rowBankName = dealRecordDao.getBankName(row.getString("BANK"));
				if (!rowBankName.isEmpty()) {
					myProjectlDto.setIdCardName(rowBankName.getString("bankName"));
				}
				myProjectlDto.setProjectId((String)row.get("OID_PLATFORM_PROJECTS_ID"));
				myProjectlDto.setArrivalDate(DateUtils.convertDate2String((Date)row.get("INS_DATE"), "yyyy-MM-dd"));
				myProjectlDto.setTenderId(row.getString("OID_TENDER_ID"));
				myProjectlDto.setAmount(StringUtil.changeSalesQuota(String.valueOf(row.getBigDecimal("TENDER_AMOUNT").intValue())));
				String state = row.getString("TENDER_STATUS");
				String type = row.getString("type");
				if("1".equals(type)){
					myProjectlDto.setTransactionStatus("认购");
					if("3".equals(state)){
						myProjectlDto.setMoneyNum("-"+StringUtil.changeSalesQuota(String.valueOf(row.getBigDecimal("TENDER_AMOUNT").intValue())));
						myProjectlDto.setDealType("成功");
						returnMessage.add(myProjectlDto);
					}else if("0".equals(state)){
						myProjectlDto.setMoneyNum("+"+StringUtil.changeSalesQuota(String.valueOf(row.getBigDecimal("TENDER_AMOUNT").intValue())));
						myProjectlDto.setDealType("失败");
						returnMessage.add(myProjectlDto);
					}else if("1".equals(state)){
						myProjectlDto.setMoneyNum("-"+StringUtil.changeSalesQuota(String.valueOf(row.getBigDecimal("TENDER_AMOUNT").intValue())));
						myProjectlDto.setDealType("进行中");
						returnMessage.add(myProjectlDto);
					}
				}else{
					myProjectlDto.setTransactionStatus("撤单");
					if("1".equals(state)){
						myProjectlDto.setDealType("退款中");
						returnMessage.add(myProjectlDto);
					}else if("3".equals(state)){
						myProjectlDto.setMoneyNum("+"+StringUtil.changeSalesQuota(String.valueOf(row.getBigDecimal("TENDER_AMOUNT").intValue())));
						myProjectlDto.setDealType("成功");
						returnMessage.add(myProjectlDto);
					}
				}
			}
		}
		return returnMessage;
	}


}
