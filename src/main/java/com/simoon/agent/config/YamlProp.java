package com.simoon.agent.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * yaml配置类
 */
@Component
@PropertySource("classpath:application.yml")
public class YamlProp {

    @Value("${env.name}")
    private String env;

    @Value("${env.default-ds}")
    private String defaultDs;

    @Value("${env.path-catch}")
    private String pathCatch;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getDefaultDs() {
        return defaultDs;
    }

    public void setDefaultDs(String defaultDs) {
        this.defaultDs = defaultDs;
    }

    public String getPathCatch() {
        return pathCatch;
    }

    public void setPathCatch(String pathCatch) {
        this.pathCatch = pathCatch;
    }
}
