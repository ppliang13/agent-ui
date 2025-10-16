package com.simoon.agent.config.ds;

import com.simoon.agent.config.EnvConfig;
import com.simoon.agent.config.YamlProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class MybatisConfig {

    @Resource
    private YamlProp yamlProp;

    @Bean
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        HashMap<Object, Object> targetDs = new HashMap<>();
        dynamicRoutingDataSource.setTargetDataSources(targetDs);
        DataSource dataSource = DataSourceManger.getDataSource(yamlProp.getEnv(), yamlProp.getDefaultDs());
        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSource);


        return dynamicRoutingDataSource;
    }


}
