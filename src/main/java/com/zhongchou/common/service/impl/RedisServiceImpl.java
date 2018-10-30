package com.zhongchou.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.zhongchou.common.base.RedisFunction;
import com.zhongchou.common.service.IRedisService;
@Service
public class RedisServiceImpl implements IRedisService{/*
	@Autowired
	private ShardedJedisPool shardedJedisPool;

	private <T> T execute(RedisFunction<ShardedJedis, T> fun){
		ShardedJedis shardedJedis = null;
		try {
			shardedJedis = shardedJedisPool.getResource();

			return fun.callback(shardedJedis);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(null!=shardedJedis)
				shardedJedis.close();
		}
		return null;
	}
	*//**
	 * 向redis设置值
	 * @param key
	 * @param value
	 * @return
	 *//*
	public String set(final String key,final String value){
		return this.execute(new RedisFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis shardedJedis) {
				return shardedJedis.set(key, value);
			}
		});
	}
	*//**
	 * 向redis设置值并设置存活时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 *//*
	public String set(final String key,final String value,final Integer seconds){
		return this.execute(new RedisFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis shardedJedis) {
				String set = shardedJedis.set(key, value);
				shardedJedis.expire(key, seconds);
				return set;
			}
		});
	}
	*//**
	 * 从redis获取数据
	 * @param key
	 * @return
	 *//*
	public String get(final String key){
		return this.execute(new RedisFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis shardedJedis) {
				return shardedJedis.get(key);
			}
		});
	}
	*//**
	 * 根据key删除redis中数据
	 * @param key
	 * @return
	 *//*
	public Long del(final String key){
		return this.execute(new RedisFunction<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis shardedJedis) {
				return shardedJedis.del(key);
			}
		});
	}
	*//**
	 * 判断redis中是否存在对应的key
	 * @param key
	 * @return
	 *//*
	public Boolean exists(final String key){
		return this.execute(new RedisFunction<ShardedJedis, Boolean>() {
			@Override
			public Boolean callback(ShardedJedis shardedJedis) {
				return shardedJedis.exists(key);
			}
		});
	}
	*//**
	 * 重新设置存活时间
	 * @param key
	 * @param seconds
	 * @return
	 *//*
	public Long expire(final String key,final Integer seconds){
		return this.execute(new RedisFunction<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis shardedJedis) {
				return shardedJedis.expire(key,seconds);
			}
		});
	}

*/}
