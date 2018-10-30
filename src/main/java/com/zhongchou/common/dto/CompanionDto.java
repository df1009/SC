package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;

/**
 * 合作伙伴
 *
 */
public class CompanionDto extends SearchConditionBase {

	private static final long serialVersionUID = 1L;

	/** 合作伙伴名 */
	private String companionName;

	/** 合作伙伴链接 */
	private String companionLink;

	/** 图片链接 */
	private String imgUrl;



	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCompanionName() {
		return companionName;
	}

	public void setCompanionName(String companionName) {
		this.companionName = companionName;
	}

	public String getCompanionLink() {
		return companionLink;
	}

	public void setCompanionLink(String companionLink) {
		this.companionLink = companionLink;
	}



}
