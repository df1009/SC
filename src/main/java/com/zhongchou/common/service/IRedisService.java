package com.zhongchou.common.service;

public interface IRedisService {/*
	*//**
	 * 向redis设置值
	 * @param key
	 * @param value
	 * @return
	 *//*
	public String set(String key,String value);
	*//**
	 * 向redis设置值并设置存活时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 *//*
	public String set(String key,String value,Integer seconds);
	*//**
	 * 从redis获取数据
	 * @param key
	 * @return
	 *//*
	public String get(String key);
	*//**
	 * 根据key删除redis中数据
	 * @param key
	 * @return
	 *//*
	public Long del(String key);
	*//**
	 * 判断redis中是否存在对应的key
	 * @param key
	 * @return
	 *//*
	public Boolean exists(String key);
	*//**
	 * 重新设置存活时间
	 * @param key
	 * @param seconds
	 * @return
	 *//*
	public Long expire(final String key,final Integer seconds);
*/}
