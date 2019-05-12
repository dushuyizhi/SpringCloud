package cn.itcast.redistest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * redisCluster:redis集群
 * @author 刘政
 *
 */
public class RedisCluster {
	@Test
	public void test1() throws Exception{
		/**创建set集合封装HostAndPort对象*/
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.12.128",7001));
		nodes.add(new HostAndPort("192.168.12.128",7002));
		nodes.add(new HostAndPort("192.168.12.128",7003));
		nodes.add(new HostAndPort("192.168.12.128",7004));
		nodes.add(new HostAndPort("192.168.12.128",7005));
		nodes.add(new HostAndPort("192.168.12.128",7006));
		
		/**创建JedisCluster集群对象*/
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		/**设置数据*/
		jedisCluster.set("sex", "男");
		System.out.println(jedisCluster.get("sex"));
		/**关闭JedisCluster*/
		jedisCluster.close();
	}
}
