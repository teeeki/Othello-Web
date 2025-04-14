package com.example; // または他のパッケージ名

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.repository") // 追加！
@EntityScan(basePackages = "com.example.model") // 追加！
public class OthelloWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(OthelloWebApplication.class, args);
    }
}