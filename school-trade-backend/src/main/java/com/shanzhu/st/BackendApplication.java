package com.shanzhu.st;

import com.shanzhu.st.utils.OrderTaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot 启动类
 * 项目启动入口（点击右键 选择 "run BackendApplication" 启动项目）
 * <p>
 * 什么是SpringBoot？（https://www.php.cn/faq/498384.html）
 *
 * @author: ShanZhu
 * @date: 2024-01-05
 */
@Slf4j
@SpringBootApplication()
public class BackendApplication {

    public static void main(String[] args) {
        //SpringBoot 执行启动
        SpringApplication.run(BackendApplication.class, args);

        OrderTaskHandler.run();

        log.info("=====================项目后端启动成功============================");

    }

}
