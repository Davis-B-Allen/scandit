package com.example.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("pcf")
public class DataSourceConfig {

    @Value("${vcap.services.elephantsql[0].credentials.uri}")
    private String elephantSqlUrl;

    @Bean
    public DataSource getDataSource() {
        String[] bits = elephantSqlUrl.split("@");
        String credString = bits[0].substring(11);
        String creds[] = credString.split(":");
        String username = creds[0];
        String password = creds[1];

        String correctUrl = "postgresql://" + bits[1];
        System.out.println(correctUrl);
        System.out.println(username);
        System.out.println(password);

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/pgdevposts");
        dataSourceBuilder.username("davisallen");
        return dataSourceBuilder.build();
    }

}
