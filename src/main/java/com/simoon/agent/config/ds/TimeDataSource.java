package com.simoon.agent.config.ds;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class TimeDataSource {

    public final HikariDataSource dataSource;

    private volatile long lastUsed;

    public TimeDataSource(HikariDataSource dataSource) {
        this.dataSource = dataSource;
        this.lastUsed = System.currentTimeMillis();
    }

    public DataSource getDataSource() {
        refresh();
        return dataSource;
    }

    private void refresh() {
        this.lastUsed = System.currentTimeMillis();
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void close() {
        this.dataSource.close();
    }


}
