package com.zhongchou.common.service.impl;

import java.text.DecimalFormat;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.yanshang.util.ConvUtils;
import com.yanshang.util.DateUtils;
import com.yanshang.util.MessageUtils;
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.constant.Constants;
import com.zhongchou.common.dao.IMyProjectDao;
import com.zhongchou.common.dao.IPCMessageDao;
import com.zhongchou.common.dao.IUserAccountBindBankDao;
import com.zhongchou.common.dao.IUserDetailDao;
import com.zhongchou.common.dto.MyAppProjectlDto;
import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.dto.SendEmailDto;
import com.zhongchou.common.loginOut.controller.login.LoginController;
import com.zhongchou.common.service.IMyProjectService;
import com.zhongchou.common.zhongzhengSao.BankSmsSao;
import com.zhongchou.common.zhongzhengSao.QueryBankBinSao;
import com.zhongchou.common.zhongzhengSao.TiedBankCardSao;




/**
 * 个人中心逻辑的实现类。
 */
@Service
public class MyProjectServiceImpl extends BaseSaoServiceImpl implements IMyProjectService {
	Logger logger=Logger.getLogger(LoginController.class);

	@Autowired
	private IMyProjectDao myProjectDao;
	@Autowired
	private IUserAccountBindBankDao userAccountBindBankDao;
	@Autowired
	private IPCMessageDao messageDao;
	/**
	 * 用户详细数据操作的接口。
	 */
	@Autowired
	private IUserDetailDao userDetailDao;
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public int getMyProjectCnt(String oidUserId,String projectState){

		return myProjectDao.getMyProjectCnt(oidUserId,projectState);

	}

	@Override
	public List<MyProjectlDto> getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage) {
		List<MyProjectlDto> returnMessage = new ArrayList<MyProjectlDto>();
		List<Row> message = myProjectDao.getMyProjectList(projectState,oidUserId,pageSize,curPage);
		for (Row row : message) {
			if (!row.isEmpty()) {
				MyProjectlDto myProjectlDto = new MyProjectlDto();
				myProjectlDto.setProjectName((String)row.get("PLATFORM_PROJECTS_SHORT_NM"));
				myProjectlDto.setProjectCode((String)row.get("OID_PLATFORM_PROJECTS_ID"));
				myProjectlDto.setIvsName((String)row.get("ENTERPRISE_LEGAL_PERSON_NAME"));
				myProjectlDto.setIssBeginDate((String)row.get("ISS_BGN_DT"));
				myProjectlDto.setIssEndDate((String)row.get("ISS_END_DT"));
				myProjectlDto.setFrzzState((String)row.get("STATUS"));
				myProjectlDto.setInsDate(DateUtils.convertDate2String((Date)row.get("INS_DATE"), "yyyy-MM-dd HH:mm"));
				/*List<Row> rowBankName = myProjectDao.getCheckFrzz(row.getString("OID_PLATFORM_PROJECTS_ID"));
				if (!rowBankName.isEmpty()) {
					myProjectlDto.setFrzzState("0");
				}else{
					myProjectlDto.setFrzzState("1");
				}*/
				returnMessage.add(myProjectlDto);
			}
		}
		return returnMessage;
	}

	@Override
	public List<MyAppProjectlDto> getMyAppProjectList(String oidUserId, int pageSize,int curPage) {
		List<MyAppProjectlDto> returnMessage = new ArrayList<MyAppProjectlDto>();
		List<Row> message = myProjectDao.getMyAppProjectList(oidUserId,pageSize,curPage);
		Integer a = 0;
		Integer b = 0;
		for (Row row : message) {
			if (!row.isEmpty()) {
				MyAppProjectlDto myProjectlDto = new MyAppProjectlDto();

				 if(null!=row.get("SALES_QUOTA")&&""!=row.get("SALES_QUOTA")){
					 a = Integer.parseInt(ConvUtils.convToString(row.get("SALES_QUOTA")));
				 }
				 if(null!=row.get("SURPLUS_SALES_QUOTA")&&""!=row.get("SURPLUS_SALES_QUOTA")){
					 b = Integer.parseInt(ConvUtils.convToString(row.get("SURPLUS_SALES_QUOTA")));
				 }
					DecimalFormat dfNum = new DecimalFormat("0.00");//格式化小数
			        String num = dfNum.format((float)(a-b)/a);//返回的是String类型
		        myProjectlDto.setPercentage(num);//募集百分比
		        myProjectlDto.setProjectState(ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST")));//项目状态
				myProjectlDto.setProjectName((String)row.get("PLATFORM_PROJECTS_SHORT_NM"));
				myProjectlDto.setAppImg((String)row.get("APP_LIST_IMG"));
				myProjectlDto.setShowProjectFlag((String)row.get("SHOW_PROJECT_FLAG"));
				myProjectlDto.setIvsName((String)row.get("ENTERPRISE_LEGAL_PERSON_NAME"));
				myProjectlDto.setIssBeginDate((String)row.get("ISS_BGN_DT"));
				myProjectlDto.setIssEndDate((String)row.get("ISS_END_DT"));
				myProjectlDto.setFrzzState((String)row.get("STATUS"));
				myProjectlDto.setInsDate(DateUtils.convertDate2String((Date)row.get("INS_DATE"), "yyyy-MM-dd HH:mm"));
				/*List<Row> rowBankName = myProjectDao.getCheckFrzz(row.getString("OID_PLATFORM_PROJECTS_ID"));
				if (!rowBankName.isEmpty()) {
					myProjectlDto.setFrzzState("0");
				}else{
					myProjectlDto.setFrzzState("1");
				}*/
				returnMessage.add(myProjectlDto);
			}
		}
		return returnMessage;
	}


	@Override
	public List<String> getIndustry() {
		List<String> returnMessage = new ArrayList<>();
		List<Row> message = myProjectDao.getIndustry();
		for (Row row : message) {
			if (!row.isEmpty()) {
				returnMessage.add((String)row.get("INDUSTRY_NM"));
			}
		}
		return returnMessage;
	}

	@Override
	public List<Map> getVerifyList(String projectCode,String oidUserId) {
		List<Map> returnMessage = new ArrayList<Map>();
		List<Row> message = myProjectDao.getVerifyList(projectCode,oidUserId);
		Map verify;
		for (Row row : message) {
			if (!row.isEmpty()) {
				verify = new HashMap();
				verify.put("raptyNum", ConvUtils.convToString(row.get("PARTY_NUM")));//审核序号
				verify.put("node", ConvUtils.convToString(row.get("NODE")));//审核内容
				verify.put("insDate", ConvUtils.convToString(row.get("INS_DATE")));//审核日期
				returnMessage.add(verify);
			}
		}
		return returnMessage;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public String insertProjectInformation(Map paramsMap) {
		List<Object> paramsDes = new ArrayList<>();
		//myProjectDao.deleteProjectInformation(projectCode,partyNum);
		paramsMap.put("status", "2");
		if(StringUtils.isEmpty(ConvUtils.convToString(paramsMap.get("id")))){//新增项目信息
			if (myProjectDao.insertProjectInformationDet(paramsMap) == 0) {
				return "002";
			}
		}else{//修改项目信息
			paramsMap.put("id", paramsMap.get("id"));
			if(myProjectDao.updateProjectDetal(paramsMap)==0){
				return "002";
			}
		}
		return "000";
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertProjectProgress(List<Object> params) {
		return myProjectDao.insertProjectProgress(params);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int updProjectProgress(List<Object> params) {
		return myProjectDao.updProjectProgress(params);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int submitProject(String projectCode,String oidUserId) {
		/*if (myProjectDao.updateDes(projectCode) == 0) {
			return 0;
		}*/
		if (myProjectDao.updateDet(projectCode,oidUserId) == 0) {
			return 0;
		}
		return 1;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteProjectInformation(String projectCode,String partyNum,String oidUserId,String id) {
		return myProjectDao.deleteProjectInformation(projectCode,partyNum,oidUserId,id);
	}


	/**
	 * 银行卡管理添加银行卡获取验证码
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> checkBankCardAvalid(Map<String, Object> paramMap) {
		String seq = nextSeq();
		BankSmsSao bankSmsSao = new BankSmsSao(seq);
		//获取用户信息
		Row userMap = myProjectDao.getUserInfo((String)paramMap.get("loginId"));
		paramMap.put("userNmCn", (String)userMap.get("USER_NAME"));
		paramMap.put("idTp", (String)userMap.get("ID_CARD_TYPE"));
		paramMap.put("idNo", (String)userMap.get("ID_CARD_NO"));
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap = bankSmsSao.setEncryptData(paramMap);
		retMap.put("userNmCn", (String)userMap.get("USER_NAME"));
		retMap.put("idTp", (String)userMap.get("ID_CARD_TYPE"));
		retMap.put("idNo", (String)userMap.get("ID_CARD_NO"));
		return retMap;
	}
	public String getRiskLevelName(String riskLevel){
		String riskLevelName = "";
		if("1".equals(riskLevel)){
			riskLevelName = "低";
		}else if("2".equals(riskLevel)){
			riskLevelName = "中低";
		}else if("3".equals(riskLevel)){
			riskLevelName = "中";
		}else if("4".equals(riskLevel)){
			riskLevelName = "中高";
		}else if("5".equals(riskLevel)){
			riskLevelName = "高";
		}
		return riskLevelName;
	}

	public String getProfitName(String code){
		String codeName = "";
		if("1".equals(code)){
			codeName = "非保本浮动收益";
		}else if("2".equals(code)){
			codeName = "保本浮动收益";
		}else if("3".equals(code)){
			codeName = "保本保底收益";
		}else if("4".equals(code)){
			codeName = "保本固定收益";
		}else if("5".equals(code)){
			codeName = "非保本固定收益";
		}else if("6".equals(code)){
			codeName = "其他";
		}
		return codeName;
	}

	/**
	 * 银行卡管理添加银行卡提交
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> bankCardMananeSubmit(Map<String, Object> paramMap) {
		//查询用户已绑定的所有卡信息
		List<Row> bankCards = userAccountBindBankDao.queryBankCard((String)paramMap.get("loginId"));
		//判断是否已达到绑卡上限
		if(bankCards.size()==5){
			paramMap.put("rsp_code", "000004");
			return paramMap;
		}
		//判断是否之前已绑定过此卡并做了取消，此时将此卡状态改为有效状态即可，不用去掉中证接口
		/*for (Row row : bankCards) {
			if(((String)paramMap.get("cardNo")).equals((String)row.get("CARD_NO"))){
				if("0".equals((String)row.get("DEL_FLG"))){
					  paramMap.put("rsp_code", "000005");
					  return paramMap;
				}else if("1".equals((String)row.get("DEL_FLG"))){
					System.out.println("更新用户银行卡管理表数据");
					//更新绑卡表中之前此卡的状态
					int upStatus = userAccountBindBankDao.updateBankCard(paramMap);
					if(upStatus>0){
						//更新状态成功
						paramMap.put("rsp_code", "000000");
						return paramMap;
					}else{
						//更新状态失败
						paramMap.put("rsp_code", "000006");
						return paramMap;
					}
				}
			}
		}*/
		//调用中证校验银行卡接口
		String seq = nextSeq();
		TiedBankCardSao tiedBankCard = new TiedBankCardSao(seq);
		Map encryptData = tiedBankCard.setEncryptData(paramMap);
		//调用获取cardBin接口
		String seqBin = nextSeq();
		QueryBankBinSao queryBankBinSao = new QueryBankBinSao(seqBin);
		Map bankCdMap = queryBankBinSao.setEncryptData(paramMap);
		//校验成功更新表数据
		if("000000".equals((String)encryptData.get("rsp_code"))){
			paramMap.put("payChannel", (String)encryptData.get("payChannel"));
			paramMap.put("bankCode", (String)paramMap.get("cardNo"));
			//paramMap.put("mainFlg", "0");//多张卡时
			paramMap.put("mainFlg", "1");//现在只能绑一张卡一直是主卡，绑多张卡时为0
			paramMap.put("bankCd", (String)bankCdMap.get("bankCd"));
			paramMap.put("needBankSign", (String)encryptData.get("needBankSign"));
			// USER_ACCOUNT_BIND_BANK表插入用户银行卡管理表数据
			encryptData.put("bankCd", bankCdMap.get("bankCd"));
			try {
				int upFlag = userAccountBindBankDao.insUserAccountBindBank(paramMap);
				if(upFlag==0){
					logger.error("插入用户银行卡管理表数据失败");
					System.out.println("插入用户银行卡管理表数据失败");
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					encryptData.put("rsp_code", "000003");
				}
			} catch (Exception e) {
				logger.error("插入用户银行卡管理表数据异常");
				System.out.println("插入用户银行卡管理表数据异常");
				e.printStackTrace();
			}
			paramMap.put("beforeChange", bankCards.get(bankCards.size()-1));
			paramMap.put("afterChange", paramMap.get("bankCode"));
			paramMap.put("count", "用户绑卡");
			// user_modify_log表插入用户信息表更改记录表
			if (userDetailDao.addUserModifyLog(paramMap) == 0) {
				logger.error("插入用户更改记录表数据失败");
				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				encryptData.put("rsp_code", "000003");
			}
			//新增用户的绑卡消息
			Map msgMap = new HashMap();
			msgMap.put("msgTitle",MessageUtils.getMessage("MSG0004"));
			msgMap.put("styleTitle","");
			msgMap.put("msgContent","去查看银行卡");
			msgMap.put("msgType","1");//系统消息
			msgMap.put("msgStuType","3");//3:绑卡消息
			msgMap.put("oidUserId",paramMap.get("loginId"));
			msgMap.put("oidUserId",paramMap.get("loginId"));
			messageDao.insMessage(msgMap);
		}
		return encryptData;
	}
	/**
	 * 银行卡管理取消非主银行卡
	 * @param paramMap
	 * @return
	 */

	@Override
	public Map<String, Object> bankCardCancel(Map<String, Object> paramMap) {

		try {
			//查询是否为主卡
			Row isMasterCard = userAccountBindBankDao.queryIsMasterCard(paramMap);
			if(isMasterCard!=null&&isMasterCard.size()>0){
				paramMap.put(Constants.RET_CODE, "002");
				paramMap.put(Constants.RET_MSG, "此卡为主卡不能取消，请联系客服");
			}else{
				//逻辑取消银行卡
				int cancelCard = userAccountBindBankDao.cancelBackCard(paramMap);
				if(cancelCard==0){
					paramMap.put(Constants.RET_CODE, "003");
					paramMap.put(Constants.RET_MSG, "取消银行卡失败");
				}else{
					paramMap.put(Constants.RET_CODE, Constants.RET_SUCCESS_CODE);
					paramMap.put(Constants.RET_MSG, Constants.RET_SUCCESS_MSG);
				}
			}
			return paramMap;
		} catch (Exception e) {
			logger.error("--------------取消银行卡异常--------------");
			paramMap.put(Constants.RET_CODE, "004");
			paramMap.put(Constants.RET_MSG, "取消银行卡异常");
			return paramMap;
		}
	}
	/**
	 * 查询用户所有银行卡
	 */

	@Override
	public List<Map<String,String>> queryBankCard(String loginId) {
		List<Row> bankCards = userAccountBindBankDao.queryBankCard(loginId);
		List<Map<String,String>> cardList = new ArrayList<>();
		if(bankCards.isEmpty()){
			return cardList;
		}
		for (Row row : bankCards) {
			Map<String,String> cards = new HashMap<String,String>();
			Integer hashCode = row.get("CARD_NO").hashCode();
			String bankCardHash = hashCode.toString();
			cards.put("bankCardHash", bankCardHash);
			cards.put("cardNo", (String)row.get("CARD_NO"));
			cards.put("bankName", (String)row.get("BANK_NM"));
			cards.put("bankCd", (String)row.get("BANK"));

			cardList.add(cards);
		}
		return cardList;
	}
	/**
	 * 绑定用户邮箱
	 */

	@Override
	public Map<String, Object> bindUserEmail(String loginId, String userEmail) {
		Map<String,Object> backMap = new HashMap<String,Object>();
		//查询用户表判断是否已绑定过邮箱
		/*Row email = myProjectDao.queryUserEmail(loginId);
		if(!email.isEmpty()&&!email.get("EMAIL").toString().isEmpty()){
			backMap.put("email", (String)email.get("EMAIL"));
			backMap.put("rsp_code", "001");
			return backMap;
		}*/
		int bindEmail = myProjectDao.bindUserEmail(loginId,userEmail);
		if(bindEmail>0){
			backMap.put("email", userEmail);
			backMap.put("rsp_code", "002");
			return backMap;
		}else{
			backMap.put("email", "");
			backMap.put("rsp_code", "003");
			return backMap;
		}
	}
	/**
	 * 检查邮箱是否绑定过
	 */
	@Override
	public Map<String, Object> checkEmailExist(String loginId) {

		Map<String,Object> backMap = new HashMap<String,Object>();
		Row email = myProjectDao.queryUserEmail(loginId);
		if(!email.isEmpty()&&!email.get("EMAIL").toString().isEmpty()){
			backMap.put("email", (String)email.get("EMAIL"));
			backMap.put("rsp_code", "001");
			return backMap;
		}else{
			backMap.put("rsp_code", "002");
			return backMap;
		}
	}


	/**
	 * 发送邮件记录
	 */

	@Override
	public void recordEmailLog(SendEmailDto emailDto) {
		myProjectDao.recordEmailLog(emailDto);

	}
	/**
	 * 添加用户头像
	 */

	@Override
	public void updateHeadImage(String loginId, String newPath) {
		myProjectDao.updateHeadImage(loginId,newPath);

	}
	//查询项目是否是本人发布的项目 true: 不是自己的项目  false：是
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map selProjectAscription(String proId,String userId) {
		return myProjectDao.getprojectIntroduce(proId,userId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean insertProjectIntroduction(Map introductionMap) {
		if (myProjectDao.updateProjectInformationDes(introductionMap) == 0) {
			//return myProjectDao.insertProjectInformationDes(introductionMap) == 1;
			return false;
		}else{
			return true;
		}
	}

	@Override
	public int deleteProjectProgress(String projectCode, String id,
			String oidUserId) {
		return myProjectDao.deleteProjectProgress(projectCode,id,oidUserId);
	}

	@Override
	public List selIntroductionTemplate() {
		List retList = new ArrayList<>();
		List<Row>  tempList = myProjectDao.selIntroductionTemplate();
		for (Row row : tempList) {
			if (!row.isEmpty()) {
				Map tempMap = new HashMap();
				tempMap.put("name", ConvUtils.convToString(row.get("TITLE")));
				//tempMap.put("content", ConvUtils.convToString(row.get("SUMMARYTEXT")));
				tempMap.put("id", ConvUtils.convToString(row.get("PROJECTS_INFO_ID")));
				retList.add(tempMap);
			}
		}
		return retList;
	}

	@Override
	public Map selSingleIntroductionTemplate(String id) {
		Map retMap = new HashMap();
		Row  tempRow = myProjectDao.selSingleIntroductionTemplate(id);
		if(!tempRow.isEmpty()){
			retMap.put("name", ConvUtils.convToString(tempRow.get("TITLE")));
			retMap.put("content", ConvUtils.convToString(tempRow.get("SUMMARYTEXT")));
		}
		return retMap;
	}


}
