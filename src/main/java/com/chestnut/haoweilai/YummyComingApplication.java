package com.chestnut.haoweilai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan  // 扫描filters
@EnableCaching  // 开启缓存
@EnableTransactionManagement  // 开启事务管理（springboot无需手动注入transactionManager）
public class YummyComingApplication {
    public static void main(String[] args) {
        SpringApplication.run(YummyComingApplication.class, args);
        log.info("项目启动成功...");
    }
}
