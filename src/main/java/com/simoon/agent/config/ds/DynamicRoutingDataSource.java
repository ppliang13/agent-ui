package com.simoon.agent.config.ds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.get();
    }


    @Override
    protected DataSource determineTargetDataSource() {
        String dsKey = DataSourceContextHolder.get();
        String env = DataSourceContextHolder.getEnv();
        return DataSourceManger.getDataSource(env, dsKey);
    }
}
