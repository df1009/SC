package com.zhongchou.common.dto;

public class InvestorDto {

	private static final long serialVersionUID = 1L;
	/**头像路径 */
	private String imgPath;
	/**跟投人姓名*/
	private String investorName;
	/**跟投日期 */
	private String investTime;
	/**跟投金额 */
	private String investMoney;
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getInvestorName() {
		return investorName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public String getInvestTime() {
		return investTime;
	}
	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}
	public String getInvestMoney() {
		return investMoney;
	}
	public void setInvestMoney(String investMoney) {
		this.investMoney = investMoney;
	}


}
