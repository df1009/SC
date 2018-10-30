package com.zhongchou.common.dto;

public class ProductDetailDto {
	public String getProjectMainTitle() {
		return projectMainTitle;
	}
	public void setProjectMainTitle(String projectMainTitle) {
		this.projectMainTitle = projectMainTitle;
	}
	private Long total_results;//总记录数
	private String product_cd;//产品代码
	private String product_short_nm;//产品简称
	private String product_full_nm;//产品全称
	private String product_inst_tp;//产品大类(一级分类)  1  基金产品类  2  私募基金类 3  私募股权类 4  信托产品类5  资管产品类6  银行产品类7  保险产品类8  债务融资工具类9 衍生品类10 内部产品11 资产支持证券类12 收益凭证类13 服务产品99 其他类型
	private String product_inst_cls;//产品类型(二级分类) 1 小集合    2 证券投资类3 另类投资  4 大集合    5 定向计划  6 专项计划
	private String iss_st;//发行状态  0 募集前状态1 募集期2 开放期3 封闭期4 清盘
	private String iss_st_name;//发行状态名字  0 募集前状态1 募集期2 开放期3 封闭期4 清盘
	private String risk_lvl;//风险级别 1 低2 中低3 中4 中高5 高
	private String risk_lvl_nm;//风险级别名字 1 低2 中低3 中4 中高5 高
	private String currency;//币种0 人民币1 美元2 港币
	private String product_emp_code;//产品负责人
	private String profit_tp;//收益类型1 非保本浮动收益2 保本浮动收益3 保本保底收益4 保本固定收益5 非保本固定收益6 其他
	private String est_yield;//预计收益率(百分比%)
	private String iss_bgn_dt;//发行开始日期
	private String iss_end_dt;//发行结束日期
	private String est_dt;//成立日期
	private String end_dt;//终止日期
	private String reg_dt;//登记日期
	private String payment_dt;//兑付日期
	private String ta_code;//登记机构
	private String mgr_code;//管理人
	private String mgr_short_nm;//管理人简称
	private String trust_code;//托管人
	private String trust_short_nm;//托管人简称
	private String pro_size;//产品规模(分)
	private String iss_size;//发行规模(分)
	private String face_amt;//发行面值(分)
	private String is_zlxs;//是否主力销售0 否1 是
	private String rxbz_flag;//热销标志0 否1 是
	private String new_pdt_flag;//新产品标志0 否1 是
	private String underwr_relship;//承销关系1 直销2 代销3 居间
	private String impt_degree;//重要程度0 无1 普通2 重要
	private String sell_mrk;//销售市场1 内部2 沪市3 深市4 银行5 协会6 中登
	private String first_min_buy;//个人首次最低金额(分)
	private String min_subs_amt;//个人最低认购金额(分)
	private String min_bids_amt;//个人最低申购金额(分)
	private String org_first_min_buy;//机构首次最低金额(分)
	private String org_min_subs_amt;//机构最低认购金额(分)
	private String org_min_bids_amt;//机构最低申购金额(分)
	private String subs_add_amt;//个人认购追加金额(分)
	private String bids_add_amt;//个人申购追加金额(分)
	private String sales_quota;//总销售额度(分)
	private String surplus_sales_quota;//剩余销售额度(分)
	private String sign_md;//签约方式
	private String product_txn_st;//产品交易状态0  预售      1  认购      2  可申赎    3  封闭      4  结束      5  停止申购  6  停止赎回  -1 终止
	private String net_value;//产品净值(元)
	private String iss_product_risk_level;//发行方产品评级
	private String iss_product_risk_level_nm;//发行方产品评级名称
	private String subs_base;//认购基数(分)
	private String bids_base;//申购基数(分)
	private String max_subs_amt;//最大认购金额(分)
	private String min_red_qty;//个人赎回起点
	private String red_base;//个人赎回基数
	private String red_bfyexx;//部分赎回余额下限
	private String big_red_rate;//巨额赎回比例
	private String fund_managers;//基金经理
	private String dvid_md;//分红方式1 红利转投	2 现金分红
	private String productContracts;//产品合同链接
	private String productProspectus ;//产品募集说明书链接
	private String riskWarningBook ;//风险提示书链接
	private String investmentConfirmation ;//投资确认书链接
	private String productIntroduction ;//产品介绍链接
	private String productSignBook ;//电子签名约定书链接
	private String enterprise_legal_person_name;//股权类产品企业法人姓名
	private String enterprise_org_code;//股权类产品企业组织机构代码
	private String productStatus;//产品募集状态（成功，失败）
	private String equityRatio;//股份百分比
	/** 项目主标题 */
	private String projectMainTitle;
	public Long getTotal_results() {
		return total_results;
	}
	public void setTotal_results(Long total_results) {
		this.total_results = total_results;
	}
	public String getProduct_cd() {
		return product_cd;
	}
	public void setProduct_cd(String product_cd) {
		this.product_cd = product_cd;
	}
	public String getProduct_short_nm() {
		return product_short_nm;
	}
	public void setProduct_short_nm(String product_short_nm) {
		this.product_short_nm = product_short_nm;
	}
	public String getProduct_full_nm() {
		return product_full_nm;
	}
	public void setProduct_full_nm(String product_full_nm) {
		this.product_full_nm = product_full_nm;
	}
	public String getProduct_inst_tp() {
		return product_inst_tp;
	}
	public void setProduct_inst_tp(String product_inst_tp) {
		this.product_inst_tp = product_inst_tp;
	}
	public String getProduct_inst_cls() {
		return product_inst_cls;
	}
	public void setProduct_inst_cls(String product_inst_cls) {
		this.product_inst_cls = product_inst_cls;
	}
	public String getIss_st() {
		return iss_st;
	}
	public void setIss_st(String iss_st) {
		this.iss_st = iss_st;
	}
	public String getRisk_lvl() {
		return risk_lvl;
	}
	public void setRisk_lvl(String risk_lvl) {
		this.risk_lvl = risk_lvl;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getProduct_emp_code() {
		return product_emp_code;
	}
	public void setProduct_emp_code(String product_emp_code) {
		this.product_emp_code = product_emp_code;
	}
	public String getProfit_tp() {
		return profit_tp;
	}
	public void setProfit_tp(String profit_tp) {
		this.profit_tp = profit_tp;
	}
	public String getEst_yield() {
		return est_yield;
	}
	public void setEst_yield(String est_yield) {
		this.est_yield = est_yield;
	}
	public String getIss_bgn_dt() {
		return iss_bgn_dt;
	}
	public void setIss_bgn_dt(String iss_bgn_dt) {
		this.iss_bgn_dt = iss_bgn_dt;
	}
	public String getIss_end_dt() {
		return iss_end_dt;
	}
	public void setIss_end_dt(String iss_end_dt) {
		this.iss_end_dt = iss_end_dt;
	}
	public String getEst_dt() {
		return est_dt;
	}
	public void setEst_dt(String est_dt) {
		this.est_dt = est_dt;
	}
	public String getEnd_dt() {
		return end_dt;
	}
	public void setEnd_dt(String end_dt) {
		this.end_dt = end_dt;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getPayment_dt() {
		return payment_dt;
	}
	public void setPayment_dt(String payment_dt) {
		this.payment_dt = payment_dt;
	}
	public String getTa_code() {
		return ta_code;
	}
	public void setTa_code(String ta_code) {
		this.ta_code = ta_code;
	}
	public String getMgr_code() {
		return mgr_code;
	}
	public void setMgr_code(String mgr_code) {
		this.mgr_code = mgr_code;
	}
	public String getMgr_short_nm() {
		return mgr_short_nm;
	}
	public void setMgr_short_nm(String mgr_short_nm) {
		this.mgr_short_nm = mgr_short_nm;
	}
	public String getTrust_code() {
		return trust_code;
	}
	public void setTrust_code(String trust_code) {
		this.trust_code = trust_code;
	}
	public String getTrust_short_nm() {
		return trust_short_nm;
	}
	public void setTrust_short_nm(String trust_short_nm) {
		this.trust_short_nm = trust_short_nm;
	}
	public String getPro_size() {
		return pro_size;
	}
	public void setPro_size(String pro_size) {
		this.pro_size = pro_size;
	}
	public String getIss_size() {
		return iss_size;
	}
	public void setIss_size(String iss_size) {
		this.iss_size = iss_size;
	}
	public String getFace_amt() {
		return face_amt;
	}
	public void setFace_amt(String face_amt) {
		this.face_amt = face_amt;
	}
	public String getIs_zlxs() {
		return is_zlxs;
	}
	public void setIs_zlxs(String is_zlxs) {
		this.is_zlxs = is_zlxs;
	}
	public String getRxbz_flag() {
		return rxbz_flag;
	}
	public void setRxbz_flag(String rxbz_flag) {
		this.rxbz_flag = rxbz_flag;
	}
	public String getNew_pdt_flag() {
		return new_pdt_flag;
	}
	public void setNew_pdt_flag(String new_pdt_flag) {
		this.new_pdt_flag = new_pdt_flag;
	}
	public String getUnderwr_relship() {
		return underwr_relship;
	}
	public void setUnderwr_relship(String underwr_relship) {
		this.underwr_relship = underwr_relship;
	}
	public String getImpt_degree() {
		return impt_degree;
	}
	public void setImpt_degree(String impt_degree) {
		this.impt_degree = impt_degree;
	}
	public String getSell_mrk() {
		return sell_mrk;
	}
	public void setSell_mrk(String sell_mrk) {
		this.sell_mrk = sell_mrk;
	}
	public String getFirst_min_buy() {
		return first_min_buy;
	}
	public void setFirst_min_buy(String first_min_buy) {
		this.first_min_buy = first_min_buy;
	}
	public String getMin_subs_amt() {
		return min_subs_amt;
	}
	public void setMin_subs_amt(String min_subs_amt) {
		this.min_subs_amt = min_subs_amt;
	}
	public String getMin_bids_amt() {
		return min_bids_amt;
	}
	public void setMin_bids_amt(String min_bids_amt) {
		this.min_bids_amt = min_bids_amt;
	}
	public String getOrg_first_min_buy() {
		return org_first_min_buy;
	}
	public void setOrg_first_min_buy(String org_first_min_buy) {
		this.org_first_min_buy = org_first_min_buy;
	}
	public String getOrg_min_subs_amt() {
		return org_min_subs_amt;
	}
	public void setOrg_min_subs_amt(String org_min_subs_amt) {
		this.org_min_subs_amt = org_min_subs_amt;
	}
	public String getOrg_min_bids_amt() {
		return org_min_bids_amt;
	}
	public void setOrg_min_bids_amt(String org_min_bids_amt) {
		this.org_min_bids_amt = org_min_bids_amt;
	}
	public String getSubs_add_amt() {
		return subs_add_amt;
	}
	public void setSubs_add_amt(String subs_add_amt) {
		this.subs_add_amt = subs_add_amt;
	}
	public String getBids_add_amt() {
		return bids_add_amt;
	}
	public void setBids_add_amt(String bids_add_amt) {
		this.bids_add_amt = bids_add_amt;
	}
	public String getSales_quota() {
		return sales_quota;
	}
	public void setSales_quota(String sales_quota) {
		this.sales_quota = sales_quota;
	}
	public String getSurplus_sales_quota() {
		return surplus_sales_quota;
	}
	public void setSurplus_sales_quota(String surplus_sales_quota) {
		this.surplus_sales_quota = surplus_sales_quota;
	}
	public String getSign_md() {
		return sign_md;
	}
	public void setSign_md(String sign_md) {
		this.sign_md = sign_md;
	}
	public String getProduct_txn_st() {
		return product_txn_st;
	}
	public void setProduct_txn_st(String product_txn_st) {
		this.product_txn_st = product_txn_st;
	}
	public String getNet_value() {
		return net_value;
	}
	public void setNet_value(String net_value) {
		this.net_value = net_value;
	}
	public String getIss_product_risk_level() {
		return iss_product_risk_level;
	}
	public void setIss_product_risk_level(String iss_product_risk_level) {
		this.iss_product_risk_level = iss_product_risk_level;
	}
	public String getIss_product_risk_level_nm() {
		return iss_product_risk_level_nm;
	}
	public void setIss_product_risk_level_nm(String iss_product_risk_level_nm) {
		this.iss_product_risk_level_nm = iss_product_risk_level_nm;
	}
	public String getSubs_base() {
		return subs_base;
	}
	public void setSubs_base(String subs_base) {
		this.subs_base = subs_base;
	}
	public String getBids_base() {
		return bids_base;
	}
	public void setBids_base(String bids_base) {
		this.bids_base = bids_base;
	}
	public String getMax_subs_amt() {
		return max_subs_amt;
	}
	public void setMax_subs_amt(String max_subs_amt) {
		this.max_subs_amt = max_subs_amt;
	}
	public String getMin_red_qty() {
		return min_red_qty;
	}
	public void setMin_red_qty(String min_red_qty) {
		this.min_red_qty = min_red_qty;
	}
	public String getRed_base() {
		return red_base;
	}
	public void setRed_base(String red_base) {
		this.red_base = red_base;
	}
	public String getRed_bfyexx() {
		return red_bfyexx;
	}
	public void setRed_bfyexx(String red_bfyexx) {
		this.red_bfyexx = red_bfyexx;
	}
	public String getBig_red_rate() {
		return big_red_rate;
	}
	public void setBig_red_rate(String big_red_rate) {
		this.big_red_rate = big_red_rate;
	}
	public String getFund_managers() {
		return fund_managers;
	}
	public void setFund_managers(String fund_managers) {
		this.fund_managers = fund_managers;
	}
	public String getDvid_md() {
		return dvid_md;
	}
	public void setDvid_md(String dvid_md) {
		this.dvid_md = dvid_md;
	}
	public String getProductContracts() {
		return productContracts;
	}
	public void setProductContracts(String productContracts) {
		this.productContracts = productContracts;
	}
	public String getProductProspectus() {
		return productProspectus;
	}
	public void setProductProspectus(String productProspectus) {
		this.productProspectus = productProspectus;
	}
	public String getRiskWarningBook() {
		return riskWarningBook;
	}
	public void setRiskWarningBook(String riskWarningBook) {
		this.riskWarningBook = riskWarningBook;
	}
	public String getInvestmentConfirmation() {
		return investmentConfirmation;
	}
	public void setInvestmentConfirmation(String investmentConfirmation) {
		this.investmentConfirmation = investmentConfirmation;
	}
	public String getProductIntroduction() {
		return productIntroduction;
	}
	public void setProductIntroduction(String productIntroduction) {
		this.productIntroduction = productIntroduction;
	}
	public String getProductSignBook() {
		return productSignBook;
	}
	public void setProductSignBook(String productSignBook) {
		this.productSignBook = productSignBook;
	}
	public String getRisk_lvl_nm() {
		return risk_lvl_nm;
	}
	public void setRisk_lvl_nm(String risk_lvl_nm) {
		this.risk_lvl_nm = risk_lvl_nm;
	}
	public String getIss_st_name() {
		return iss_st_name;
	}
	public void setIss_st_name(String iss_st_name) {
		this.iss_st_name = iss_st_name;
	}
	public String getEnterprise_legal_person_name() {
		return enterprise_legal_person_name;
	}
	public void setEnterprise_legal_person_name(String enterprise_legal_person_name) {
		this.enterprise_legal_person_name = enterprise_legal_person_name;
	}
	public String getEnterprise_org_code() {
		return enterprise_org_code;
	}
	public void setEnterprise_org_code(String enterprise_org_code) {
		this.enterprise_org_code = enterprise_org_code;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public String getEquityRatio() {
		return equityRatio;
	}
	public void setEquityRatio(String equityRatio) {
		this.equityRatio = equityRatio;
	}
}
