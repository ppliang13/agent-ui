package com.simoon.agent.entity;

import com.simoon.agent.service.MockService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlStep extends AbstractProcessStep {

    private List<Map<String, Object>> resList;

    public SqlStep(String name) {
        super(name);
    }

    @Override
    public Object execute(Map<String, Object> context) {
        String source = input.get("source");
        String sql = input.get("sql");
        System.out.println("source = " + source + " " + "sql =" + sql);
        List<Map<String, Object>> list = MockService.select(source, sql);
        output = list;
        resList = list;
        return list;
    }

    /**
     * resList
     * [0].name
     *
     * @param path
     * @return
     */
    @Override
    public Object getValue(String path) {
        if (path == null || !path.startsWith("[") || !path.contains("].")) {
            return null;
        }

        // 解析下标
        int idxStart = path.indexOf("[") + 1;
        int idxEnd = path.indexOf("]");
        int index = Integer.parseInt(path.substring(idxStart, idxEnd));

        // 解析 key
        String key = path.substring(idxEnd + 2); // 去掉 "]."

        if (resList != null && index >= 0 && index < resList.size()) {
            return resList.get(index).get(key);
        }
        return null;
    }

    public SqlStep addInput(String key, String value) {
        if(this.input == null) {
            this.input=new HashMap<String, String>();
        }
        this.input.put(key, value);
        return this;
    }
}