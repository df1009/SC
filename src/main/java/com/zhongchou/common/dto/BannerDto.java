package com.zhongchou.common.dto;

import java.util.List;
import java.util.Map;

import com.yanshang.util.Row;
import com.zhongchou.common.model.SearchConditionBase;

/**
 * banner
 *
 */
public class BannerDto extends SearchConditionBase {

	public String getBannerAppUrl() {
		return bannerAppUrl;
	}

	public void setBannerAppUrl(String bannerAppUrl) {
		this.bannerAppUrl = bannerAppUrl;
	}

	public String getBannerAppLink() {
		return bannerAppLink;
	}

	public void setBannerAppLink(String bannerAppLink) {
		this.bannerAppLink = bannerAppLink;
	}

	private static final long serialVersionUID = 1L;

	/** banner路径 */
	private String bannerUrl;

	/** banner链接 */
	private String bannerLink;

	/** APPbanner路径 */
	private String bannerAppUrl;

	/** APPbanner链接 */
	private String bannerAppLink;

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getBannerLink() {
		return bannerLink;
	}

	public void setBannerLink(String bannerLink) {
		this.bannerLink = bannerLink;
	}



}
