package com.zhongchou.common.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.yanshang.util.Row;
import com.yanshang.util.StringUtils;
import com.zhongchou.common.dao.IPCMyProjectDao;
import com.zhongchou.common.dao.IUserAccountBindBankDao;
import com.zhongchou.common.dao.IUserDetailDao;
import com.zhongchou.common.dto.MyProjectlDto;
import com.zhongchou.common.service.IPCMyProjectService;
import com.zhongchou.common.util.StringUtil;




/**
 * 个人中心逻辑的实现类。
 */
@Service
public class PCMyProjectServiceImpl extends BaseSaoServiceImpl implements IPCMyProjectService {
	Logger logger=Logger.getLogger(PCMyProjectServiceImpl.class);

	@Autowired
	private IPCMyProjectDao myProjectDao;
	@Autowired
	private IUserAccountBindBankDao userAccountBindBankDao;
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
	public List getMyProjectList(String projectState,String oidUserId, int pageSize,int curPage) {
		List returnMessage = new ArrayList<MyProjectlDto>();
		List<Row> message = myProjectDao.getMyProjectList(projectState,oidUserId,pageSize,curPage);
		for (Row row : message) {
			if (!row.isEmpty()) {
				Map<String, String> myProjectlMap = new HashMap<String, String>(); 
				String proSt = ConvUtils.convToString(row.get("PLATFORM_PROJECTS_ST"));
				if(1==1){//其他期没有字段暂定
					myProjectlMap.put("projectSt", "1");//产品状态1.预热期2.认购期3.募集成功4.募集失败5.交割期6.交割完成7.退出
				}
				if("1".equals(proSt)
						||"2".equals(proSt)){//认购期
					myProjectlMap.put("projectSt", "2");//产品状态
				}
				if("0".equals(proSt)
						){//预热期
					myProjectlMap.put("projectSt", "1");//产品状态
				}
				if("1".equals(row.get("SHOW_PROJECT_FLAG"))
						){//展示项目
					myProjectlMap.put("projectSt", "0");//产品状态
				}
				myProjectlMap.put("projectImg", ConvUtils.convToString(row.get("APP_LIST_IMG")));//产品列表图
				myProjectlMap.put("projectName", ConvUtils.convToString(row.get("PLATFORM_PROJECTS_FULL_NM")));//产品名
				myProjectlMap.put("projectId", ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品id
				
				//int allAmount = ConvUtils.convToString("PLATFORM_PROJECTS_ST");//
				myProjectlMap.put("Percentage", ConvUtils.convToString(row.get("ISS_BGN_DT")));//募集百分比
				myProjectlMap.put("projectOnlineFlag", ConvUtils.convToString(row.get("ONLINE_FLAG")));//上线下
				String lastSt = ConvUtils.convToString(row.get("LAST_STATE"));//0：初审  1:初审通过
				//产品审核状态0:待编辑1：审核中，2：驳回3:审核通过
				myProjectlMap.put("projectReviewedSt", ConvUtils.convToString(row.get("STATUS")));//产品审核状态
				if("1".equals(lastSt)&&"1".equals(ConvUtils.convToString(row.get("DRAFT_FLAG")))){//初审通过且为存草稿状态
					myProjectlMap.put("projectReviewedSt", "3");//产品审核状态
				}else if(!StringUtils.isEmpty(projectState)){
					myProjectlMap.put("projectReviewedSt", projectState);//产品审核状态
				}
				myProjectlMap.put("mainTitle", ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//个性主标题
				if(StringUtils.isEmpty(row.getString("desid"))&&!StringUtils.isEmpty(row.getString("detailid"))){//审核通过的只更改模块或进展（审核中，驳回）的数据 在detail表中的数据
					Row detailRow = myProjectDao.selProjectToDetail(row.getString("OID_PLATFORM_PROJECTS_ID"));
					if(!detailRow.isEmpty()){
						System.out.println("DRAFT_FLAG:"+detailRow.get("DRAFT_FLAG"));
						myProjectlMap.put("projectOnlineFlag", ConvUtils.convToString(detailRow.get("ONLINE_FLAG")));//上线下
						myProjectlMap.put("projectReviewedSt", ConvUtils.convToString(detailRow.get("STATUS")));//产品审核状态
						if("1".equals(ConvUtils.convToString(detailRow.get("DRAFT_FLAG")))){//初审通过且为存草稿状态
							myProjectlMap.put("projectReviewedSt", "3");//产品审核状态
						}
					}
				}
				returnMessage.add(myProjectlMap);
			}
		}
		return returnMessage;
	}
	/**
	 * 查询我的产品详细内容
	 * 用户的IOD  oidUserId
	 * 产品的code      projectCode
	 * @return   
	 */
	@Override
	public Map selProductDetail(Map selMap) {
		Map myProMap = new HashMap();
		//项目信息
		Row row = myProjectDao.getMyProjectSupDetail(selMap);
		if(row.isEmpty()){
			row = myProjectDao.getMyProjectOnlineDetail(selMap);
		}
		if (!row.isEmpty()) {
			myProMap.put("projectName", ConvUtils.convToString(row.get("PLATFORM_PROJECTS_FULL_NM")));//产品名
			myProMap.put("projectId", ConvUtils.convToString(row.get("OID_PLATFORM_PROJECTS_ID")));//产品代码
			myProMap.put("salesQuota",StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SALES_QUOTA"))));//产品目标额度
			myProMap.put("sellShare", ConvUtils.convToString(row.get("SELL_SHARE")));//出让股份
			myProMap.put("projectAdds", ConvUtils.convToString(row.get("PROJECT_ADDS")));//项目所在地
			myProMap.put("riskLvl", ConvUtils.convToString(row.get("RISK_LVL_DES")));//产品风险等级
			myProMap.put("comPanyName", ConvUtils.convToString(row.get("PROJECT_COMPANY_NAME")));//企业名称
			myProMap.put("orgCode", ConvUtils.convToString(row.get("ENTERPRISE_ORG_CODE")));//企业组织机构代码
			myProMap.put("legalPerson", ConvUtils.convToString(row.get("ENTERPRISE_LEGAL_PERSON_NAME")));//企业法人
			myProMap.put("shareholdersNum", ConvUtils.convToString(row.get("SHAREHOLDERS_NUM")));//现有股东人数
			myProMap.put("companyType", ConvUtils.convToString(row.get("COMPANY_TYPE")));//企业行业类别
			myProMap.put("canShareholdersNum", ConvUtils.convToString(row.get("CAN_SHAREHOLDERS_NUM")));//可认购股东数
			myProMap.put("companyRegistAdds", ConvUtils.convToString(row.get("COMPANY_REGIST_ADDS")));//企业注册地址
			myProMap.put("companyHoldTime", ConvUtils.convToString(row.get("COMPANY_HOLD_TIME")));//企业成立时间
			myProMap.put("minSellingShare", ConvUtils.convToString(row.get("MIN_SELLING_SHARE")));//最低出让股份
			myProMap.put("maxTargetAmt", ConvUtils.convToDouble(ConvUtils.convToString(row.get("MAX_TARGET_AMT")))/1000000);//最高目标额度（万元）
			myProMap.put("minTargetAmt", ConvUtils.convToDouble(row.get("MIN_TARGET_AMT"))/1000000);//最低目标额度（万元）
			myProMap.put("maxSellingShare", row.get("MAX_SELLING_SHARE"));//最高出让股份
			myProMap.put("minSubsAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("MIN_SUBS_AMT_DES"))));//最低认购金额（元）
			myProMap.put("subsAddAmt", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("SUBS_ADD_AMT_DES"))));//最小追加金额（元）
			myProMap.put("maxSubsSmt", StringUtil.changeSalesQuota(ConvUtils.convToString(row.get("MAX_SUBS_AMT_DES"))));//最高认购金额（元）
			myProMap.put("issBgnDt", ConvUtils.convToString(row.get("ISS_BGN_DT_DES")));//发行开始日期
			myProMap.put("issEndDt", ConvUtils.convToString(row.get("ISS_END_DT_DES")));//发行结束日期
			myProMap.put("productprospectus", StringUtils.isEmpty(ConvUtils.convToString(row.get("PRODUCTPROSPECTUS")))?ConvUtils.convToString(row.get("SC_PRODUCTPROSPECTUS")):ConvUtils.convToString(row.get("PRODUCTPROSPECTUS")));//产品募集说明书
			myProMap.put("deliveryTime", ConvUtils.convToString(row.get("DELIVERY_TIME")));//交割完成时间
			myProMap.put("guideCompany", ConvUtils.convToString(row.get("GUIDE_COMPANY")));//领投方
			myProMap.put("signoutTime", ConvUtils.convToString(row.get("SIGNOUT_TIME")));//退出时间
			myProMap.put("majorBusiness", ConvUtils.convToString(row.get("MAJOR_BUSINESS")));//主营业务
			myProMap.put("financingPurpose", ConvUtils.convToString(row.get("FINANCING_PURPOSE")));//融资用途
			myProMap.put("financingMode", ConvUtils.convToString(row.get("FINANCING_MODE")));//融资方式
			myProMap.put("financingCost", ConvUtils.convToString(row.get("FINANCING_COST")));//融资成本
			myProMap.put("exitMode", ConvUtils.convToString(row.get("EXIT_MODE")));//退出方式
			myProMap.put("annotation", ConvUtils.convToString(row.get("ANNOTATION")));//注释
			
			myProMap.put("industry", ConvUtils.convToString(row.get("INDUSTRY")));//行业类型
			myProMap.put("projectCharacter", ConvUtils.convToString(row.get("PROJECT_CHARACTER")));//产品标签
			myProMap.put("title", ConvUtils.convToString(row.get("PROJECT_MAIN_TITLE")));//项目标题
			myProMap.put("summarytext", ConvUtils.convToString(row.get("SUMMARYTEXT")));//项目简介
			myProMap.put("linkUrl", ConvUtils.convToString(row.get("LINK_URL")));//项目视频
			myProMap.put("imgPath", ConvUtils.convToString(row.get("IMG_PATH")));//PC端背景
			myProMap.put("appListImg", ConvUtils.convToString(row.get("APP_LIST_IMG")));//PC/APP列表图
		}
		return myProMap;
	}
	//查询项目模块信息
	@Override
	public List selProductModularDetailList(Map selMap) {
		List<Row> proList = myProjectDao.selProductModularDetailList(selMap);
		List ProductList = new ArrayList<>();
		for (Row row : proList) {
			if (!row.isEmpty()) {
				Map productIntroduceMap = new HashMap(); 
				productIntroduceMap.put("id", ConvUtils.convToString(row.get("PROJECTS_INFO_ID")));//产品介绍序号
				productIntroduceMap.put("partyNum", ConvUtils.convToString(row.get("PARTY_NUM")));//产品介绍序号
				productIntroduceMap.put("name", ConvUtils.convToString(row.get("TITLE")));//产品介绍标题
				productIntroduceMap.put("content", ConvUtils.convToString(row.get("SUMMARYTEXT")));//产品介绍内容
				ProductList.add(productIntroduceMap);
			}
		}
		return ProductList;
	}
	//添加项目介绍信息
	@Override
	public int saveMyProjectInfo(Map selMap) {
		try {
			myProjectDao.saveInsMyProjectInfo(selMap);//复制online数据
		} catch (Exception e) {
			logger.info("从online中复制成功");
		}
		return myProjectDao.saveMyProjectInfo(selMap);//修改sup数据
	}
	/**
	 * 插入项目模块信息(保存草稿)
	 * 若是新增项目模块直接像detail_sup表中插入一条草稿记录
	 * 若是修改草稿,审核中,驳回,审核通过的项目模块  查询projectId在表sup是否存在
	 * 不存在(修改审核通过的)将online表中对应的记录复制到sup表再对sup表update
	 * 存在对sup表update
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map insertProjectInformation(Map paramsMap) {
		Map retMap = new HashMap();
		retMap.put("proCode", "0");
		retMap.put("proMsg", "成功");
		String strType = ConvUtils.convToInt(paramsMap.get("type"))==1?"项目模块":"项目进展";
		if(StringUtils.isEmpty(ConvUtils.convToString(paramsMap.get("projectId")))){//新增项目信息或模块
			if(myProjectDao.AddSaveMyProjectModular(paramsMap)==0){
				logger.info("新增"+strType+"信息失败");
				retMap.put("proCode", "3");
				retMap.put("proMsg", "更改异常");
				return retMap;
			}
		}else{//修改项目信息
			Row ProjectRow = myProjectDao.selMyProjectModularToId(paramsMap);
			logger.info("用户："+paramsMap.get("oidUserId")+"status修改状态为："
					+ProjectRow.getString("STATUS")
					+"草稿状态为："+ProjectRow.getString("DRAFT_FLAG")
					+ "的"+strType
					+ProjectRow.getString("PROJECTS_INFO_ID")
					+"项目code为："+ProjectRow.getString("OID_PLATFORM_PROJECTS_ID"))
					;
			if(!ProjectRow.isEmpty()){//sup表中有信息 修改草稿,审核中,驳回
				if(myProjectDao.updSaveMyProjectModular(paramsMap)==0){//修改项目信息
					logger.info("修改草稿,审核中,驳回的"+strType+"异常");
					retMap.put("proCode", "1");
					retMap.put("proMsg", "更改异常");
					return retMap;
				}
				logger.info("用户："+paramsMap.get("oidUserId")+"修改草稿,审核中,驳回的项目"+strType+"保存草稿成功");
			}else{//sup表中没有信息 修改审核通过的信息  将online表中对应的记录复制到sup表再对sup表update
				if(ConvUtils.convToInt(paramsMap.get("type"))==1){//只有项目模块才能修改审核通过的信息
					if(myProjectDao.updSaveMyProjectModularCopy(paramsMap)==0){
						logger.info("复制online表数据异常");
						retMap.put("proCode", "2");
						retMap.put("proMsg", "复制online表数据异常");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return retMap;
					}
					if(myProjectDao.updSaveMyProjectModular(paramsMap)==0){
						logger.info("修改审核通过的"+strType+"异常");
						retMap.put("proCode", "3");
						retMap.put("proMsg", "修改审核通过的"+strType+"异常");
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return retMap;
					}
					logger.info("用户："+paramsMap.get("oidUserId")+"修改项目"+strType+"保存草稿成功");
				}
			}
		}
		//保存模块和进展时更改项目状态为草稿
		/*myProjectDao.saveInsMyProjectInfo(paramsMap);
		myProjectDao.updateDesSupSta(paramsMap);*/
		return retMap;
	}

	/**
	 * 查询我的项目的项目进展
	 * return progressDate 进展时间
	 * 		  title  标题
	 * 		  imgPath 图片
	 */
	@Override
	public Map selMyProjectProgress(Map paramsMap) {
		Map retMap = new HashMap();
		//审核通过的项目进展
		List<Row> OnlineRowList = myProjectDao.selProductProgressOnlineList(paramsMap);
		List OnlineList = new ArrayList<>();
		for (Row row : OnlineRowList) {
			if(!row.isEmpty()){
				Row r = new Row();
				r = row;
				String img = row.getString("imgPath");
				if(!StringUtils.isEmpty(img)){//不要问我为什么有这个if，我也很无奈啊!
					r.put("imgPathList", img.split(","));
				}else{
					r.put("imgPathList", new ArrayList<>());
				}
				String time = ConvUtils.convToString(row.get("progressDate"));
				if(!StringUtils.isEmpty(time)){
					r.put("time", time.substring(0,10));//时间轴日期
					r.put("timeYear", time.substring(0,7));//时间轴年月
					r.put("timeDate", time.substring(8,10));//时间轴月日
				}
				OnlineList.add(r);
			}
		}
		//草稿，审核中和驳回的项目进展
		List<Row> SupRowList = myProjectDao.selProductProgressSupList(paramsMap);
		List SupList = new ArrayList<>();
		for (Row row : SupRowList) {
			if(!row.isEmpty()){
				Row r = new Row();
				r = row;
				String img = row.getString("imgPath");
				if(!StringUtils.isEmpty(img)){
					r.put("imgPath", img.split(","));
				}else{
					r.put("imgPath", new ArrayList<>());
				}
				SupList.add(r);
			}
		}
		retMap.put("OnlineList", OnlineList);
		retMap.put("SupList", SupList);
		return retMap;
	}
	
	/**
	 * 我的项目草稿状态提交审核
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean submitMyProject(Map paramsMap) {
		boolean flag = true;
		int count = 0;
		if(myProjectDao.submitProjectExaminerDes(paramsMap)==0){
			logger.info("更新Des失败");
			count+=1;
		}
		if(myProjectDao.submitProjectExaminerDetail(paramsMap)==0){
			logger.info("更新detail失败");
			count+=1;
		}
		if(count==2)
			flag = false;
		return flag;
	}
	//删除模块
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteProjectInformation(Map reqMap) {
		boolean flag = true;
		if(myProjectDao.deleteProjectInformationSup(reqMap)==0){
			logger.info("删除Sup失败");
		}
		if("1".equals(reqMap.get("projectType"))){//不能删除审核通过的项目进展
			if(myProjectDao.deleteProjectInformationOnline(reqMap)==0){
				logger.info("删除online失败");
			}
		}
		return flag;
	}
	//查询项目的驳回原因
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
	//查询用户是否有我的项目的权限
	@Override
	public int getMyProject(String loginId) {
		return myProjectDao.getMyProject(loginId);
	}
	//设置模块序号
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean setMyProjectModularSort(Map reqMap) {
    	Map paramsMap = new HashMap();
    	int count = 0;
    	paramsMap.put("projectCode", reqMap.get("projectCode"));
    	paramsMap.put("oidUserId", reqMap.get("oidUserId"));
    	//修改第一条数据
    	paramsMap.put("projectId", reqMap.get("fistModId"));
    	paramsMap.put("partyNum", reqMap.get("fistSort"));
    	if(myProjectDao.setMyProjectModularSortSup(paramsMap)==0){
			logger.info("修改第一个模块排序更新sup失败");
			count++;
		}
		if(myProjectDao.setMyProjectModularSortOnline(paramsMap)==0){
			logger.info("修改第一个模块排序更新Online失败");
			count++;
		}
		//修改第二条数据
		paramsMap.put("projectId", reqMap.get("secondModId"));
    	paramsMap.put("partyNum", reqMap.get("secondSort"));
		if(myProjectDao.setMyProjectModularSortSup(paramsMap)==0){
			logger.info("修改第二个模块排序更新sup失败");
			count++;
		}
		if(myProjectDao.setMyProjectModularSortOnline(paramsMap)==0){
			logger.info("修改第二个模块排序更新Online失败");
			count++;
		}
		if(count==4){
			return false;
		}
		return true;
    }
}
