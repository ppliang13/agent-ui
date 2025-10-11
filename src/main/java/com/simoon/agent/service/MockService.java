package com.simoon.agent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: simoon
 * @CreateTime: 2025-10-03
 * @Description: 步骤执行器
 * @Version: 1.0
 */
public class MockService {


    /**
     * 造数据10条 java8
     * @param dev 环境
     * @param sql sql
     * @return 数据
     */
    public static List<Map<String, Object>> select(String dev, String sql) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", i);
            row.put("name", "simoon" + i);
            row.put("env", dev);
            row.put("sql", sql);
            result.add(row);
        }
        return result;
    }
}

