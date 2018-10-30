package com.zhongchou.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IDealRecordDao;
import com.zhongchou.common.dao.IUserAccountBindBankDao;
import com.zhongchou.common.dao.IUserDao;
import com.zhongchou.common.dao.IUserDetailDao;
import com.zhongchou.common.dao.IUserIdVerificationDao;
import com.zhongchou.common.dto.UserDto;
import com.zhongchou.common.service.IBankService;
import com.zhongchou.common.util.StringUtil;
import com.zhongchou.common.zhongzheng.cipher.MD5;
import com.zhongchou.common.zhongzhengSao.BankSmsSao;
import com.zhongchou.common.zhongzhengSao.QueryBankBinSao;
import com.zhongchou.common.zhongzhengSao.QueryTiedBankCardSao;
import com.zhongchou.common.zhongzhengSao.TiedBankCardSao;

@Service
public class BankServiceImpl extends BaseSaoServiceImpl implements IBankService {

	/**
	 * 用户数据操作的接口。
	 */
	@Autowired
	private IUserDao userDao;

	/**
	 * 用户详细数据操作的接口。
	 */
	@Autowired
	private IUserDetailDao userDetailDao;

	/**
	 * 用户身份验证数据操作的接口。
	 */
	@Autowired
	private IUserIdVerificationDao userIdVerificationDao;

	/**
	 * 用户银行卡数据操作的接口。
	 */
	@Autowired
	private IUserAccountBindBankDao userAccountBindBankDao;

	@Autowired
	private IDealRecordDao dealRecordDao;

	/**
	 * 姓名银行卡验证
	 * @param parameter 接口参数
	 * @return 流水号
	 */
	public Map<String,Object> getBankSms(Map<String,Object> parameter) {

		// 返回的参数
		Map<String,Object> retMap = new HashMap<String,Object>();
		// 带入的参数
		Map<String,Object> setMap = new HashMap<String,Object>();

		String seq = nextSeq();
		BankSmsSao bankSmsSao = new BankSmsSao(seq);

		//用户账号
		String loginId = (String)parameter.get("loginId");

		setMap.put("loginId",loginId );//登录帐号
		setMap.put("userNmCn", (String)parameter.get("userName"));//姓名
		setMap.put("idTp", (String)parameter.get("idTp"));//证件类型1：身份证2：护照3：军官证4：士兵证5：港澳通行证6：户口本7：其他
		setMap.put("idNo", (String)parameter.get("idCard"));//证件号码
		setMap.put("cardNo", (String)parameter.get("bankCode"));//银行卡
		setMap.put("mobileNo", (String)parameter.get("mobile"));//银行预留手机号

		// 调用接口，返回报文体
		retMap = bankSmsSao.setEncryptData(setMap);

		if("000000".equals(retMap.get("rsp_code").toString())){//成功

			retMap.put("code", "000");
		    //流水号
            return retMap;
		}else{//错误

			retMap.put("code", "001");
			return retMap;
		}
	}

	//查询用户绑卡信息
	@Override
	public Map selBankCardList(String userId) {
		logger.info("BankServiceImpl.selBankCardList       start  userId:"+userId);
		List<Row> bankRow  = userAccountBindBankDao.queryBankCard(userId);
		logger.info("用户："+userId+"有效银行卡数量"+bankRow.size());
		Map BankMap = new HashMap();
		List retList = new ArrayList();
		List bankList = new ArrayList();
		BankMap.put("bankList", retList);
		BankMap.put("userBank", bankList);
		for (int i = 0; i < bankRow.size(); i++) {
			Row row = bankRow.get(i);
			if (!row.isEmpty()) {
				Map bankMap = new HashMap();
				Map retMap = new HashMap();
				String card = row.get("CARD_NO").toString();
				retMap.put("bankCd", row.get("BANK"));//行别
				retMap.put("cardNoLast", card.substring(card.length()-4,card.length()));//银行卡
				retMap.put("cardNoMask", StringUtils.encryptBankCard(card));//银行卡
				retMap.put("cardNm", row.get("BANK_NM"));//银行卡名字
				retMap.put("isDefault", row.get("MAIN_FLG"));//是否是默认银行卡 0否1是
				retMap.put("bankNoIndex", card.hashCode());//银行卡index
				//银行卡号和index
				bankMap.put("bankNo", card);
				bankMap.put("bankNoIndex", card.hashCode());
				bankMap.put("cardNm", row.get("BANK_NM"));
				bankMap.put("bankCd", row.get("BANK_CD"));
				retList.add(retMap);
				bankList.add(bankMap);
			}
		}
		return BankMap;
	}

	@Override
	public List selBankQuota(String bankCd) {
		List<Row> bankRow  = userAccountBindBankDao.selBankWithholdQuota(bankCd);
		List bankList = new ArrayList();
		for (Row row : bankRow) {
			if (!row.isEmpty()) {
				Map bankMap = new HashMap();
				bankMap.put("bankId", ConvUtils.convToString(row.get("BANK_CD")));
				bankMap.put("bankNm", ConvUtils.convToString(row.get("BANK_NM")));
				bankMap.put("singleLimitAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SINGLE_LIMIT_AMT"))));
				bankMap.put("dayLimitAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("DAY_LIMIT_AMT"))));
				bankList.add(bankMap);
			}
		}
		 return bankList;
	}

	@Override
	public Map selSupportBank(String bankId) {
		QueryBankBinSao bankBinSao = new QueryBankBinSao(nextSeq());
		Map reqMap = new HashMap();
		reqMap.put("cardNo", bankId);
		Map retMap = bankBinSao.setEncryptData(reqMap);
		String bankCd = ConvUtils.convToString(retMap.get("bankCd"));//银行行别
		return dealRecordDao.getBankName(bankCd);
	}

	//壹理财老用户注册第二部返显用户绑卡信息
	@Override
	public Map selBankSmg(String userId) {
		return  userAccountBindBankDao.selBankSmg(userId);
	}
	/**
	 * 查询用户在中证的绑卡信息
	 * @param parameter 接口参数
	 * @return 成功失败
	 */
	public Map<String,Object> queryTiedBankCard(Map<String,Object> parameter) {
		QueryTiedBankCardSao queryCard = new QueryTiedBankCardSao(nextSeq());
		Map retMap = queryCard.setEncryptData(parameter);
		return retMap;
	}

	//查询中证支持的银行
	public List selSupportBankList() {
		List<Row> bankRow  = userAccountBindBankDao.selSupportBankList();
		List bankList = new ArrayList();
		for (Row row : bankRow) {
			if (!row.isEmpty()) {
				Map bankMap = new HashMap();
				bankMap.put("bankId", ConvUtils.convToString(row.get("BANK_CD")));
				bankMap.put("bankNm", ConvUtils.convToString(row.get("BANK_NM")));
				bankMap.put("singleLimitAmt", ConvUtils.convToString(row.get("SINGLE_LIMIT_AMT")));
				bankMap.put("dayLimitAmt", ConvUtils.convToString(row.get("DAY_LIMIT_AMT")));
				bankList.add(bankMap);
			}
		}
		 return bankList;
	}
	//查询用户银行卡绑定手机号
	public Map selBankMap(String oidUserId) {
		return userAccountBindBankDao.queryBankMobile(oidUserId);
	}
}
