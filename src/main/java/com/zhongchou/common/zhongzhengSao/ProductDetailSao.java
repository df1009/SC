package com.zhongchou.common.zhongzhengSao;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yanshang.util.ConvUtils;
import com.zhongchou.common.base.SCMap;
import com.zhongchou.common.dto.ProductDetailDto;
import com.zhongchou.common.util.Log4jUtil;
import com.zhongchou.common.util.ZhongzhengUtil;

//产品详情
public class ProductDetailSao extends BaseSao {
	Logger logger=Logger.getLogger(ProductDetailSao.class);
	String functionId = "G0000016";
	ProductDetailDto productDetailDto = null;

	public ProductDetailSao(String req_ssn) {
		super.req_ssn = req_date+req_ssn;
	}

	public Map receivEncryptData(String content){
		Map retMap = new HashMap();
		List productList = null;
		//解密后的接口数据
		String encryptData = null;
		try {
			encryptData = ZhongzhengUtil.receivEncryptData(content);
			logger.info("ProductDetailSao receivEncryptData response ("+functionId+") :\r\n"+Log4jUtil.output(encryptData));
			JSONObject obj = JSONObject.fromObject(encryptData);
			JSONObject head= obj.getJSONObject("head");
			JSONObject body= obj.getJSONObject("body");
			String rspCode = head.get("rsp_code").toString();
			String rspDesc = head.get("rsp_desc").toString();
			if("000000".equals(rspCode)){//成功
				productList = new ArrayList();
				JSONArray records = body.getJSONArray("records");
				String totalResults = body.get("total_results").toString();
				for (int i = 0; i < records.size(); i++) {
					//获取每一个json对象
					JSONObject jsonItem = records.getJSONObject(i);
					productDetailDto = new ProductDetailDto();
					setProductDetailDto(jsonItem);
					productList.add(productDetailDto);
					}
				retMap.put("totalResults", totalResults);
				retMap.put("productList", productList);
				retMap.put("rsp_code", rspCode);
				retMap.put("rspDesc", rspDesc);
			}else{//失败
				retMap.put("rsp_code", rspCode);
				retMap.put("rspDesc", rspDesc);
			}
		} catch (Exception e) {
			retMap.put("rsp_code", "000002");
			retMap.put("rsp_desc", "获取中证数据失败");
			e.printStackTrace();
			return retMap;
		}
		return retMap;
	}

	public Map setEncryptData(Map setData){
		logger.info("ProductDetailSao setEncryptData start ");
		SCMap scBody = new SCMap();
		SCMap scHeader = new SCMap();
		SCMap scMap = new SCMap(scHeader,scBody);
		scHeader.setValue("req_ssn", req_ssn);
		scHeader.setValue("req_date", req_date);
		scHeader.setValue("version", version);
		scHeader.setValue("ins_cd", ins_cd);
		scHeader.setValue("channel_no", channel_no);
		scHeader.setValue("function", functionId);

		scBody.setValue("page_no", ConvUtils.convToString(setData.get("pageNo")));//页码
		scBody.setValue("page_size", ConvUtils.convToString(setData.get("pageSize")));//每页获取条数
		scBody.setValue("product_cd", (String)setData.get("productCd"));//产品id
		if(setData.get("productInstTp")!=null){
			scBody.setValue("product_inst_tp", setData.get("productInstTp").toString());//产品大类（一级分类）
		}
		if(setData.get("productInstCls")!=null){
			scBody.setValue("product_inst_cls", setData.get("productInstCls").toString());//产品类型（二级分类）
		}
		logger.info("ProductDetailSao setEncryptData encryptDatarequest("+functionId+") :\r\n"+Log4jUtil.output(scMap.getMap().toString()));
		String responseContent = null;
		try {
			responseContent = ZhongzhengUtil.encryption3Des(scMap.getMap());
		} catch (Exception e) {
			Map retMap = new HashMap();
			retMap.put("rsp_code", "000001");
			retMap.put("rsp_desc", "请求中证失败");
			e.printStackTrace();
			return retMap;
		}//请求中证获取数据
		return receivEncryptData(responseContent);
	}

	public ProductDetailDto setProductDetailDto(JSONObject body){
		productDetailDto.setProduct_cd(body.get("product_cd") == null ? "":body.get("product_cd").toString());//产品代码
		productDetailDto.setProduct_short_nm(body.get("product_short_nm") == null ? "":body.get("product_short_nm").toString());//产品简称
		productDetailDto.setProduct_full_nm(body.get("product_full_nm") == null ? "":body.get("product_full_nm").toString());//产品全称
		productDetailDto.setProduct_inst_tp(body.get("product_inst_tp") == null ? "":body.get("product_inst_tp").toString());//产品大类(一级分类)  1  基金产品类  2  私募基金类 3  私募股权类 4  信托产品类5  资管产品类6  银行产品类7  保险产品类8  债务融资工具类9 衍生品类10 内部产品11 资产支持证券类12 收益凭证类13 服务产品99 其他类型
		productDetailDto.setProduct_inst_cls(body.get("product_inst_cls") == null ? "":body.get("product_inst_cls").toString());//产品类型(二级分类) 1 小集合    2 证券投资类3 另类投资  4 大集合    5 定向计划  6 专项计划
		productDetailDto.setIss_st(body.get("iss_st") == null ? "":body.get("iss_st").toString());//发行状态  0 募集前状态1 募集期2 开放期3 封闭期4 清盘
		productDetailDto.setRisk_lvl(body.get("risk_lvl") == null ? "":body.get("risk_lvl").toString());//风险级别 1 低2 中低3 中4 中高5 高
		productDetailDto.setCurrency(body.get("currency") == null ? "":body.get("currency").toString());//币种0 人民币1 美元2 港币
		productDetailDto.setProduct_emp_code(body.get("product_emp_code") == null ? "":body.get("product_emp_code").toString());//产品负责人
		productDetailDto.setProfit_tp(body.get("profit_tp") == null ? "":body.get("profit_tp").toString());//收益类型1 非保本浮动收益2 保本浮动收益3 保本保底收益4 保本固定收益5 非保本固定收益6 其他
		productDetailDto.setEst_yield(body.get("est_yield") == null ? "":body.get("est_yield").toString());//预计收益率(百分比%)
		productDetailDto.setIss_bgn_dt(body.get("iss_bgn_dt") == null ? "":body.get("iss_bgn_dt").toString());//发行开始日期
		productDetailDto.setIss_end_dt(body.get("iss_end_dt") == null ? "":body.get("iss_end_dt").toString());//发行结束日期
		productDetailDto.setEst_dt(body.get("est_dt") == null ? "":body.get("est_dt").toString());//成立日期
		productDetailDto.setEnd_dt(body.get("end_dt") == null ? "":body.get("end_dt").toString());//终止日期
		productDetailDto.setReg_dt(body.get("reg_dt") == null ? "":body.get("reg_dt").toString());//登记日期
		productDetailDto.setPayment_dt(body.get("payment_dt") == null ? "":body.get("payment_dt").toString());//兑付日期
		productDetailDto.setTa_code(body.get("ta_code") == null ? "":body.get("ta_code").toString());//登记机构
		productDetailDto.setMgr_code(body.get("mgr_code") == null ? "":body.get("mgr_code").toString());//管理人
		productDetailDto.setMgr_short_nm(body.get("mgr_short_nm") == null ? "":body.get("mgr_short_nm").toString());//管理人简称
		productDetailDto.setTrust_code(body.get("trust_code") == null ? "":body.get("trust_code").toString());//托管人
		productDetailDto.setTrust_short_nm(body.get("trust_short_nm") == null ? "":body.get("trust_short_nm").toString());//托管人简称
		productDetailDto.setPro_size(body.get("pro_size") == null ? "":body.get("pro_size").toString());//产品规模(分)
		productDetailDto.setIss_size(body.get("iss_size") == null ? "":body.get("iss_size").toString());//发行规模(分)
		productDetailDto.setFace_amt(body.get("face_amt") == null ? "":body.get("face_amt").toString());//发行面值(分)
		productDetailDto.setIs_zlxs(body.get("is_zlxs") == null ? "":body.get("is_zlxs").toString());//是否主力销售0 否1 是
		productDetailDto.setRxbz_flag(body.get("rxbz_flag") == null ? "":body.get("rxbz_flag").toString());//热销标志0 否1 是
		productDetailDto.setNew_pdt_flag(body.get("new_pdt_flag") == null ? "":body.get("new_pdt_flag").toString());//新产品标志0 否1 是
		productDetailDto.setUnderwr_relship(body.get("underwr_relship") == null ? "":body.get("underwr_relship").toString());//承销关系1 直销2 代销3 居间
		productDetailDto.setImpt_degree(body.get("impt_degree") == null ? "":body.get("impt_degree").toString());//重要程度0 无1 普通2 重要
		productDetailDto.setSell_mrk(body.get("sell_mrk") == null ? "":body.get("sell_mrk").toString());//销售市场1 内部2 沪市3 深市4 银行5 协会6 中登
		productDetailDto.setFirst_min_buy(body.get("first_min_buy") == null ? "":body.get("first_min_buy").toString());//个人首次最低金额(分)
		productDetailDto.setMin_subs_amt(body.get("min_subs_amt") == null ? "":body.get("min_subs_amt").toString());//个人最低认购金额(分)
		productDetailDto.setMin_bids_amt(body.get("min_bids_amt") == null ? "":body.get("min_bids_amt").toString());//个人最低申购金额(分)
		productDetailDto.setOrg_first_min_buy(body.get("org_first_min_buy") == null ? "":body.get("org_first_min_buy").toString());//机构首次最低金额(分)
		productDetailDto.setOrg_min_subs_amt(body.get("org_min_subs_amt") == null ? "":body.get("org_min_subs_amt").toString());//机构最低认购金额(分)
		productDetailDto.setOrg_min_bids_amt(body.get("org_min_bids_amt") == null ? "":body.get("org_min_bids_amt").toString());//机构最低申购金额(分)
		productDetailDto.setSubs_add_amt(body.get("subs_add_amt") == null ? "":body.get("subs_add_amt").toString());//个人认购追加金额(分)
		productDetailDto.setBids_add_amt(body.get("bids_add_amt") == null ? "":body.get("bids_add_amt").toString());//个人申购追加金额(分)
		productDetailDto.setSales_quota(body.get("sales_quota") == null ? "":body.get("sales_quota").toString());//总销售额度(分)
		productDetailDto.setSurplus_sales_quota(body.get("surplus_sales_quota") == null ? "":body.get("surplus_sales_quota").toString());//剩余销售额度(分)
		productDetailDto.setSign_md(body.get("sign_md") == null ? "":body.get("sign_md").toString());//签约方式
		productDetailDto.setProduct_txn_st(body.get("product_txn_st") == null ? "":body.get("product_txn_st").toString());//产品交易状态0  预售      1  认购      2  可申赎    3  封闭      4  结束      5  停止申购  6  停止赎回  -1 终止
		productDetailDto.setNet_value(body.get("net_value") == null ? "":body.get("net_value").toString());//产品净值(元)
		productDetailDto.setIss_product_risk_level(body.get("iss_product_risk_level") == null ? "":body.get("iss_product_risk_level").toString());//发行方产品评级
		productDetailDto.setIss_product_risk_level_nm(body.get("iss_product_risk_level_nm") == null ? "":body.get("iss_product_risk_level_nm").toString());//发行方产品评级名称
		productDetailDto.setSubs_base(body.get("subs_base") == null ? "":body.get("subs_base").toString());//认购基数(分)
		productDetailDto.setBids_base(body.get("bids_base") == null ? "":body.get("bids_base").toString());//申购基数(分)
		productDetailDto.setMax_subs_amt(body.get("max_subs_amt") == null ? "":body.get("max_subs_amt").toString());//最大认购金额(分)
		productDetailDto.setMin_red_qty(body.get("min_red_qty") == null ? "":body.get("min_red_qty").toString());//个人赎回起点
		productDetailDto.setRed_base(body.get("red_base") == null ? "":body.get("red_base").toString());//个人赎回基数
		productDetailDto.setRed_bfyexx(body.get("red_bfyexx") == null ? "":body.get("red_bfyexx").toString());//部分赎回余额下限
		productDetailDto.setBig_red_rate(body.get("big_red_rate") == null ? "":body.get("big_red_rate").toString());//巨额赎回比例
		productDetailDto.setFund_managers(body.get("fund_managers") == null ? "":body.get("fund_managers").toString());//基金经理
		productDetailDto.setDvid_md(body.get("dvid_md") == null ? "":body.get("dvid_md").toString());//分红方式1 红利转投	2 现金分红
		productDetailDto.setProductContracts(body.get("productContracts") == null ? "":body.get("productContracts").toString());//产品合同链接
		productDetailDto.setProductProspectus(body.get("productProspectus") == null ? "":body.get("productProspectus").toString());//产品募集说明书链接
		productDetailDto.setRiskWarningBook(body.get("riskWarningBook") == null ? "":body.get("riskWarningBook").toString());//风险提示书链接
		productDetailDto.setInvestmentConfirmation(body.get("investmentConfirmation") == null ? "":body.get("investmentConfirmation").toString());//投资确认书链接
		productDetailDto.setProductIntroduction(body.get("productIntroduction") == null ? "":body.get("productIntroduction").toString());//产品介绍链接
		productDetailDto.setProductSignBook(body.get("productSignBook") == null ? "":body.get("productSignBook").toString());//电子签名约定书链接
		productDetailDto.setEnterprise_legal_person_name(body.get("enterprise_legal_person_name") == null ? "":body.get("enterprise_legal_person_name").toString());//股权类产品企业法人姓名
		productDetailDto.setEnterprise_org_code(body.get("enterprise_org_code") == null ? "":body.get("enterprise_org_code").toString());//股权类产品企业组织机构代码
/*		productDetailDto.setProductStatus(body.get("enterprise_org_code") == null ? "":body.get("").toString());//产品募集状态（成功，失败）
		productDetailDto.setEquityRatio(body.get("enterprise_org_code") == null ? "":body.get("").toString());//股份百分比
*/		return productDetailDto;
	}
}
