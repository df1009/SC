package com.zhongchou.common.model;

import java.io.Serializable;

/**
 * 查找条件的数据传输类。
 */
public class SearchConditionBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private String primaryKey;

	/** 查找关键字 */
	private String findKeywords;

	/** 查找状态值 */
	private String findStatus;

	/** 分页开始位置 */
	private int pageIndex;

	/** 每页最大显示数 */
	private int pageSize;

	/** 更新/插入者ID */
	private String updUserId;

	/** 查找开始时间 */
	private String dateFrom;

	/** 查找结束时间 */
	private String dateTo;

	/** 排序列 */
	private String sortField;

	/** 排序 */
	private String sort;

	/** 检索数据条数 */
	// private int findCnt = 0;

	/**
	 * 获取主键的方法。
	 *
	 * @return 主键
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * 设置主键的方法。
	 *
	 * @param primaryKey
	 *            主键
	 */
	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * 获取查找关键字的方法。
	 *
	 * @return 查找关键字
	 */
	public String getFindKeywords() {
		return findKeywords;
	}

	/**
	 * 设置查找关键字的方法。
	 *
	 * @param findKeywords
	 *            查找关键字
	 */
	public void setFindKeywords(String findKeywords) {
		this.findKeywords = findKeywords;
	}

	/**
	 * 获取get的方法。
	 *
	 * @return
	 */
	public String getFindStatus() {
		return findStatus;
	}

	/**
	 * 设置set的方法。
	 *
	 * @param findStatus
	 */
	public void setFindStatus(String findStatus) {
		this.findStatus = findStatus;
	}

	/**
	 * 获取分页开始位置的方法。
	 *
	 * @return 分页开始位置
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * 设置分页开始位置的方法。
	 *
	 * @param pageIndex
	 *            分页开始位置
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * 获取每页最大显示数的方法。
	 *
	 * @return 每页最大显示数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页最大显示数的方法。
	 *
	 * @param pageSize
	 *            每页最大显示数
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取更新/插入者ID的方法。
	 *
	 * @return 更新/插入者ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}

	/**
	 * 设置更新/插入者ID的方法。
	 *
	 * @param updUserId
	 *            更新/插入者ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}

	/**
	 * 获取查找开始时间的方法。
	 *
	 * @return 查找开始时间
	 */
	public String getDateFrom() {
		return dateFrom;
	}

	/**
	 * 设置查找开始时间的方法。
	 *
	 * @param dateFrom
	 *            查找开始时间
	 */
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * 获取查找结束时间的方法。
	 *
	 * @return 查找结束时间
	 */
	public String getDateTo() {
		return dateTo;
	}

	/**
	 * 设置查找结束时间的方法。
	 *
	 * @param dateTo
	 *            查找结束时间
	 */
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * 获取排序列的方法。
	 *
	 * @return 排序列
	 */
	public String getSortField() {
		return sortField;
	}

	/**
	 * 设置排序列的方法。
	 *
	 * @param sortField
	 *            排序列
	 */
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	/**
	 * 获取排序的方法。
	 *
	 * @return 排序
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * 设置排序的方法。
	 *
	 * @param sort
	 *            排序
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * 取得get的方法。
	 *
	 * @return
	 */
//	public int getFindCnt() {
//		return findCnt;
//	}

	/**
	 * 设置set的方法。
	 *
	 * @param searchCnt
	 */
	// public void setFindCnt(int findCnt) {
	// this.findCnt = findCnt;
	// }

}
