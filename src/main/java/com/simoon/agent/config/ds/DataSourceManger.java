package com.simoon.agent.config.ds;

import com.simoon.agent.config.EnvConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class DataSourceManger {

    /**
     * 存放多数据源
     */
    public static final Map<String, TimeDataSource> dataSourceMap = new HashMap<>();

    private static final Long Timeout = 30 * 60 * 1000L;


    public static DataSource getDataSource(String env, String dsKey) {
        String key = env + "_" + dsKey;
        TimeDataSource tds = dataSourceMap.get(key);
        if (tds != null) {
            return tds.getDataSource();
        }


        synchronized (DataSourceManger.class) {
            tds = dataSourceMap.get(key);

            if (tds == null) {
                HikariConfig hikariConfig = new HikariConfig();
                DataSourceInfo dsConfig=EnvConfig.getDS(env,key);
                hikariConfig.setJdbcUrl(dsConfig.getUrl());
                hikariConfig.setUsername(dsConfig.getUsername());
                hikariConfig.setPassword(dsConfig.getPassword());
                hikariConfig.setDriverClassName(dsConfig.getDriver());
                hikariConfig.setMaximumPoolSize(5);
                hikariConfig.setMinimumIdle(1);
                hikariConfig.setConnectionTimeout(30000);
                hikariConfig.setIdleTimeout(600000);
                HikariDataSource ds = new HikariDataSource(hikariConfig);
                tds = new TimeDataSource(ds);
                dataSourceMap.put(key, tds);
            }
        }
        return tds.getDataSource();
    }

    /**
     * 清理超时未使用的数据源
     */
    public static void clean() {
        long now = System.currentTimeMillis();
        dataSourceMap.entrySet().removeIf(entry -> {
            TimeDataSource tds = entry.getValue();
            if (now - tds.getLastUsed() > Timeout) {
                tds.close();
                return true;
            }
            return false;
        });
    }
}
