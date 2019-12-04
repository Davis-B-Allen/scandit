package com.example.postservice.config;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSourceConfigTest {

    private String vcapServices;

    public DataSourceConfigTest() {
        vcapServices = "{\"elephantsql\":[{ \"label\": \"elephantsql\", \"provider\": null, \"plan\": \"turtle\", \"name\": \"dba-micro-scandit-api-db-posts\", \"tags\": [ \"Data Stores\", \"Web-based\", \"Open Source\", \"User Provisioning\", \"PaaS\", \"Midsize Business\", \"Windows\", \"New Product\", \"3rd Party Developer\", \"Restaurant and Hospitality\", \"Developer Tools\", \"Retail\", \"postgresql\", \"Buyable\", \"Importable\", \"Single Sign-On\", \"Manufacuring\", \"Mac\", \"Android\", \"Professional Services\", \"Data Store\", \"Government and Education\", \"Healthcare\", \"relational\", \"Small Business\", \"IT Management\" ], \"instance_name\": \"dba-micro-scandit-api-db-posts\", \"binding_name\": null, \"credentials\": { \"uri\": \"postgres://etbhubrn:trGw450JR4saZAMGG0SyCb3mifsBlG4W@isilo.db.elephantsql.com:5432/etbhubrn\", \"max_conns\": \"5\" }, \"syslog_drain_url\": null, \"volume_mounts\": [ ] }]}";
        MockitoAnnotations.initMocks(this);
    }

    @InjectMocks
    DataSourceConfig dataSourceConfig;

    @Test
    public void parseDbUrlAndCreds_VcapServices_Success() {
        HashMap<String, String> dbInfo = dataSourceConfig.parseDbUrlAndCreds(vcapServices);
        String correctUrl = dbInfo.get("correctUrl");
        String username = dbInfo.get("username");
        String password = dbInfo.get("password");
        assertThat(correctUrl).isEqualTo("jdbc:postgresql://isilo.db.elephantsql.com:5432/etbhubrn");
        assertThat(username).isEqualTo("etbhubrn");
        assertThat(password).isEqualTo("trGw450JR4saZAMGG0SyCb3mifsBlG4W");
    }
}
