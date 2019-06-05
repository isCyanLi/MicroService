package cn.li.Rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义负载均衡算法
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule() {
//        return new RandomRule();//Ribbon默认是轮询，自定义为随机
        return new RandomRule_L(); // 我定义为每台机器5次
    }

}
