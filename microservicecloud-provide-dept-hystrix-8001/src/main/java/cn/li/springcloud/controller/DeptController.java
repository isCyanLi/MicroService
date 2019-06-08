package cn.li.springcloud.controller;

import cn.li.entities.Dept;
import cn.li.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {

    @Autowired
    private DeptService service;
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/dept/add",method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept)
    {
        return service.add(dept);
    }

    @RequestMapping(value = "/dept/get/{id}",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    public Dept get(@PathVariable("id") Long id)
    {
        Dept dept = service.get(id);
        if (dept == null) {
            throw new RuntimeException("该ID：" + id + "没有对应的信息");
        }
        return dept;
    }

    public Dept processHystrix_Get(@PathVariable("id") Long id) {
        return new Dept().setDeptno(id).setDname("该ID：" + id + "没有对应的信息，null -- @HystrixCommand").setDb_source("no source in this database");
    }

    @RequestMapping(value = "/dept/list",method = RequestMethod.GET)
    public List<Dept> list()
    {
        return service.list();
    }

    @RequestMapping(value = "/dept/discovery",method = RequestMethod.GET)
    public Object disvovery() {
        List<String> list = discoveryClient.getServices();
        System.out.println("***********" + list);

        List<ServiceInstance> srvList = discoveryClient.getInstances("MICROSERVICECLOUD-DEPT");
        for (ServiceInstance element: srvList)
        {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
        }
        return this.discoveryClient;

    }



}
