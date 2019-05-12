package cn.itcast.redistest;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 单机版redis测试：连接linux上的redis
 * @author 刘政
 *
 */
public class RedisTest {
	/**
	 * 方式一：Jedis操作redis
	 */
	@Test
	public void test1(){
		Jedis jedis = new Jedis("192.168.12.128", 6379);
		String set = jedis.set("age", "20");
		System.out.println(set);
//		String name = jedis.get("name");
//		System.out.println(name);
		jedis.close();
	}
	/**
	 * 方式二：使用JedisPool操作redis
	 */
	@Test
	public void test2(){
		/**创建JedisPoolConfig连接池配置对象*/
		JedisPoolConfig jpconfig = new JedisPoolConfig();
		/**设置最大连接数*/
		jpconfig.setMaxTotal(20);
		/**创建JedisPool连接池对象*/
		JedisPool jedisPool = new JedisPool(jpconfig,"192.168.12.128", 6379);
		/**获取Jedis对象*/
		Jedis jedis = jedisPool.getResource();
		System.out.println(jedis.get("age"));
		jedis.close();
		jedisPool.close();
	}
}




