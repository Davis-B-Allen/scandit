package com.example.postservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@Profile("pcf")
public class DataSourceConfig {

    @Value("${vcap.services}")
    private String vcapServices;

    @Bean
    public DataSource getDataSource() {
        HashMap<String, String> dbInfo = parseDbUrlAndCreds(vcapServices);
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dbInfo.get("correctUrl"));
        dataSourceBuilder.username(dbInfo.get("username"));
        dataSourceBuilder.password(dbInfo.get("password"));
        return dataSourceBuilder.build();
    }

    public HashMap<String, String> parseDbUrlAndCreds(String vcapServices) {
        HashMap<String, String> dbInfo = new HashMap();

        System.out.println(vcapServices);
        String[] strs = vcapServices.split("postgres");
        System.out.println("!!!!!!");
        System.out.println(strs[2]);
        String[] strs2 = strs[2].split("\"");
        System.out.println("!!!!!!");
        System.out.println(strs2[0]);
        String elephantSqlUrl = strs2[0];
        System.out.println(elephantSqlUrl);
        String[] bits = elephantSqlUrl.split("@");
        String credString = bits[0].substring(3);
        String creds[] = credString.split(":");
        String username = creds[0];
        String password = creds[1];

        String correctUrl = "jdbc:postgresql://" + bits[1];
        System.out.println(correctUrl);
        System.out.println(username);
        System.out.println(password);

        dbInfo.put("correctUrl", correctUrl);
        dbInfo.put("username", username);
        dbInfo.put("password", password);

        return dbInfo;
    }

}
