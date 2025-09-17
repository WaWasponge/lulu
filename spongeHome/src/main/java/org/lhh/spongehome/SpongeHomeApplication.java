package org.lhh.spongehome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("org.lhh.spongehome.mapper")
@SpringBootApplication
@EnableCaching
public class SpongeHomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpongeHomeApplication.class, args);
    }

}
