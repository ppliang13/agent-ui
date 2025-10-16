package com.simoon.agent.config.ds;

/**
 * @Title: DataSourceContextHolder
 * @Author: simoon
 * @Package com.simoon.agent.config.ds
 * @Date: 2025/10/16 19:32
 * @Description: 数据源切换工具
 */
public class DataSourceContextHolder {
    /**
     * 数据源名称
     */
    private static final ThreadLocal<String> DS_KEY = new ThreadLocal<>();

    /**
     * 环境名称
     */
    private static final ThreadLocal<String> ENV = new ThreadLocal<>();


    public  static void set(String dsKey) {
        DS_KEY.set(dsKey);
    }
    public static void setEnv(String env) {
        ENV.set(env);
    }

    public static String get() {
        return DS_KEY.get();
    }

    public static String getEnv() {
        return ENV.get();
    }

    public static void clear() {
        DS_KEY.remove();
        ENV.remove();
    }
}
