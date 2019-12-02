package com.example.authservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("pcf")
public class DataSourceConfig {

    @Value("${vcap.services}")
    private String vcapServices;

    Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Bean
    public DataSource getDataSource() {
        String[] strs = vcapServices.split("postgres");
        String[] strs2 = strs[2].split("\"");
        String elephantSqlUrl = strs2[0];
        String[] bits = elephantSqlUrl.split("@");
        String credString = bits[0].substring(3);
        String creds[] = credString.split(":");
        String username = creds[0];
        String password = creds[1];

        String correctUrl = "jdbc:postgresql://" + bits[1];
        logger.info("After parsing vcap services, database info is:\n" +
                "correctUrl: " + correctUrl + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(correctUrl);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

}
