package com.zhongchou.common.dto;

import com.zhongchou.common.model.SearchConditionBase;

/**
 * 战略合作
 *
 */
public class CooperateDto extends SearchConditionBase {

	private static final long serialVersionUID = 1L;

	/** 战略合作名 */
	private String cooperateName;

	/** 战略合作链接 */
	private String cooperateLink;

	/** 图片链接 */
	private String imgUrl;


	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCooperateName() {
		return cooperateName;
	}

	public void setCooperateName(String cooperateName) {
		this.cooperateName = cooperateName;
	}

	public String getCooperateLink() {
		return cooperateLink;
	}

	public void setCooperateLink(String cooperateLink) {
		this.cooperateLink = cooperateLink;
	}



}
