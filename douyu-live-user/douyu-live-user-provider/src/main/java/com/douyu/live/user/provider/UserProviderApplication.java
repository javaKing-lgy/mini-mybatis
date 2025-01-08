package com.douyu.live.user.provider;

import com.douyu.live.user.provider.service.IUserTagService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 用户服务中台启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class UserProviderApplication implements CommandLineRunner {

    @Autowired
    private IUserTagService iUserTagService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserProviderApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        
    }
}
