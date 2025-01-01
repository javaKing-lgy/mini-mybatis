package com.douyu.live.id.generate.provider;

import com.douyu.live.id.generate.provider.service.IdBuilderService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liuguanyi
 * * @date 2025/1/1
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
public class IdGenerateApplication implements CommandLineRunner {
    @Resource
    private IdBuilderService idBuilderService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IdGenerateApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(idBuilderService.getSeqId(1));
//            if(i == 49){
//                Thread.sleep(200);
//            }
        }
    }
}
