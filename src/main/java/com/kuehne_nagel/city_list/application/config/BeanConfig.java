package com.kuehne_nagel.city_list.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;

@Configuration
public class BeanConfig {

    @Value("${password.encoder.rounds}")
    private Integer passwordEncoderRounds;

    @Value("${time.zone}")
    private String timeZone;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(passwordEncoderRounds);
    }

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of(timeZone);
    }

}

