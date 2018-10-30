package com.zhongchou.common.dto;

public class PcGoodProductListDto {

	private String productId;//产品id
	private String projectMainTitle;//项目主标题
	private String smallImgPath;//列表缩略图
	private String  projectAdds;//项目所在地：上海
	private String projectcompanyName;//企业名称
	private String[] projectCharacter;//项目特点（标签）


	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProjectMainTitle() {
		return projectMainTitle;
	}
	public void setProjectMainTitle(String projectMainTitle) {
		this.projectMainTitle = projectMainTitle;
	}
	public String getSmallImgPath() {
		return smallImgPath;
	}
	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
	}
	public String getProjectAdds() {
		return projectAdds;
	}
	public void setProjectAdds(String projectAdds) {
		this.projectAdds = projectAdds;
	}
	public String getProjectcompanyName() {
		return projectcompanyName;
	}
	public void setProjectcompanyName(String projectcompanyName) {
		this.projectcompanyName = projectcompanyName;
	}
	public String[] getProjectCharacter() {
		return projectCharacter;
	}
	public void setProjectCharacter(String[] projectCharacter) {
		this.projectCharacter = projectCharacter;
	}


}
