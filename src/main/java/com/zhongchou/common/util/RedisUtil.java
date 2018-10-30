package com.zhongchou.common.util;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.zhongchou.common.base.RedisFunction;

public class RedisUtil {

	static ShardedJedisPool shardedJedisPool;
    static{
        JedisPoolConfig config =new JedisPoolConfig();//Jedis池配置
        config.setMaxIdle(500);//最大活动的对象个数
/*        config.setMaxIdle(1000 * 60);//对象最大空闲时间
        config.setMaxWaitMillis(1000 * 10);//获取对象时最大等待时间
        config.setTestOnBorrow(true);*/
        String hostA = "127.0.0.1";
        int portA = 6379;
        List<JedisShardInfo> jdsInfoList =new ArrayList<JedisShardInfo>(1);
        JedisShardInfo infoA = new JedisShardInfo(hostA, portA);
        jdsInfoList.add(infoA);
        shardedJedisPool =new ShardedJedisPool(config, jdsInfoList);
     }

	private static <T> T execute(RedisFunction<ShardedJedis, T> fun){
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
	/**
	 * 向redis设置值
	 * @param key
	 * @param value
	 * @return
	 */
	public static String set(final String key,final String value){
		return execute(new RedisFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis shardedJedis) {
				return shardedJedis.set(key, value);
			}
		});
	}
	/**
	 * 向redis设置值并设置存活时间
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public static String set(final String key,final String value,final Integer seconds){
		return execute(new RedisFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis shardedJedis) {
				String set = shardedJedis.set(key, value);
				shardedJedis.expire(key, seconds);
				return set;
			}
		});
	}
	/**
	 * 从redis获取数据
	 * @param key
	 * @return
	 */
	public static String get(final String key){
		return execute(new RedisFunction<ShardedJedis, String>() {
			@Override
			public String callback(ShardedJedis shardedJedis) {
				return shardedJedis.get(key);
			}
		});
	}
	/**
	 * 根据key删除redis中数据
	 * @param key
	 * @return
	 */
	public static Long del(final String key){
		return execute(new RedisFunction<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis shardedJedis) {
				return shardedJedis.del(key);
			}
		});
	}
	/**
	 * 判断redis中是否存在对应的key
	 * @param key
	 * @return
	 */
	public static Boolean exists(final String key){
		return execute(new RedisFunction<ShardedJedis, Boolean>() {
			@Override
			public Boolean callback(ShardedJedis shardedJedis) {
				return shardedJedis.exists(key);
			}
		});
	}
	/**
	 * 重新设置存活时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static Long expire(final String key,final Integer seconds){
		return execute(new RedisFunction<ShardedJedis, Long>() {
			@Override
			public Long callback(ShardedJedis shardedJedis) {
				return shardedJedis.expire(key,seconds);
			}
		});
	}
}
