package com.lz.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@RestController
public class MainController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello,Counsumer";
    }

    /**
     * spring cloud 自带的
     */
    @Autowired
    DiscoveryClient discoveryClient;

    /**
     * 获取所有注册服务名
     * @return
     */
    @RequestMapping("/requestClient1")
    public Object requestClient1() {
        return discoveryClient.getServices();//["eurekaprovider2","eurekaserver","eurekaconsumer","eurekaprovider1"]
    }

    /**
     * 根据serviceId 调用服务
     * @return
     */

    @RequestMapping("/requestClient2")
    public String requestClient2() {
        //获取所有服务名称
        List<String> services = discoveryClient.getServices();
        String response = null;
        for (String service : services) {
            //调用provider
            if (service.contains("provider")) {
                //获取所有服务实例
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                for (ServiceInstance serviceInstance : instances) {
                    //拼接url，调用服务
                    String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/hello";
                    //使用RestTemplate调用provider接口
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
                    System.out.println(entity.getBody());
                    response = entity.getBody();
                }
            }
        }
        return response;
    }

    @Autowired
    EurekaClient eurekaClient;

    /**
     * 用eurekaclient调用服务
     * @return
     */
    @RequestMapping("/requestClient3")
    public Object requestClient3() {
        //如果启动两个服务（高可用），这里会获取两个InstanceInfo
        List<InstanceInfo> instanceInfos = eurekaClient.getInstancesByVipAddress("eurekaprovider2", false);
        System.out.println("==> instanceInfos.size(): " + instanceInfos.size());
        String response = null;
        if (instanceInfos.size() > 0) {
            InstanceInfo instanceInfo = instanceInfos.get(0);
            if(instanceInfo.getStatus() == InstanceInfo.InstanceStatus.UP){
                //拼接url，调用服务
                String url = "http://" + instanceInfo.getHostName() + ":" + instanceInfo.getPort() + "/hello";
                //使用RestTemplate调用provider接口
                RestTemplate restTemplate = new RestTemplate();
                response = restTemplate.getForObject(url, String.class);
                System.out.println(response);
            }
        }
        return response;
    }

    @Autowired
    LoadBalancerClient loadBalancerClient;

    /**
     * 调用springcloud带的接口，负载均衡调用服务
     * @return
     */
    @RequestMapping("/requestClient4")
    public Object requestClient4() {
        ServiceInstance instance = loadBalancerClient.choose("eurekaprovider2");
        //拼接url，调用服务
        String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/hello";
        //使用RestTemplate调用provider接口
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }
}
