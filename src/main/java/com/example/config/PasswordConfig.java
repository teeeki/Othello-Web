package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * パスワードエンコーダの設定クラス
 * パスワードのハッシュ化を行うためのエンコーダをBeanとして提供します
 */
@Configuration // Springの設定クラスであることを示すアノテーション
public class PasswordConfig {

    /**
     * パスワードエンコーダのBeanを提供します
     * BCryptアルゴリズムを使用して安全にパスワードをハッシュ化します
     * 
     * @return BCryptPasswordEncoderのインスタンス
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCryptは強力なハッシュアルゴリズムでソルトも自動的に生成されます
        return new BCryptPasswordEncoder();
    }
}