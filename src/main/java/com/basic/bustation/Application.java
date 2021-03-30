package com.basic.bustation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;



/**
 * Created by hp-pc on 2021/1/31.
 */

@SpringBootApplication
@ImportResource("applicationContext.xml")
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) {
        log.debug("debug级别日志");
        log.info("info级别日志");
        SpringApplication.run(Application.class,args);
    }
}
