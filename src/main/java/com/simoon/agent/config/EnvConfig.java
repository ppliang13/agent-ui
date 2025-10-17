package com.simoon.agent.config;

import com.simoon.agent.config.ds.DataSourceInfo;

public class EnvConfig {


    public static DataSourceInfo getDS(String env, String key) {
        if(key.equals("mysql-01")){
            return  datasource1();
        }
        if(key.equals("mysql-02")){
            return  datasource2();
        }
        return null;
    }


    private static DataSourceInfo datasource1(){
        DataSourceInfo ds = new DataSourceInfo();
        ds.setDriver("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test");
        ds.setUsername("test");
        ds.setPassword("123456");
        return ds;
    }

    private static DataSourceInfo datasource2(){
        DataSourceInfo ds = new DataSourceInfo();
        ds.setDriver("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3307/test");
        ds.setUsername("test");
        ds.setPassword("123456");
        return ds;
    }
}
