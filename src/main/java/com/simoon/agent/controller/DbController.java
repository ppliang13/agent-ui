package com.simoon.agent.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: simoon
 * @CreateTime: 2025-09-28
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/db")
public class DbController {

    private List<Map<String,Object>> fetchData(Map<String,String> param) {
        // 模拟数据
        List<Map<String,Object>> data = new ArrayList<>();
        for (int i = 1; i <= 120; i++) {
            Map<String,Object> record = new HashMap<>();
            record.put("id", i);
            record.put("name", "Name " + i);
            record.put("value", "Value " + i);
            record.put("value1", "Value " + i);
            record.put("value2", "Value " + i);
            record.put("value3", "Value " + i);
            record.put("value4", "Value " + i);
            record.put("value5", "Value " + i);
            record.put("value6", "Value " + i);
            record.put("value7", "Value " + i);
            record.put("value8", "Value " + i);
            record.put("value9", "Value " + i);
            record.put("value10", "Value " + i);
            record.put("value11", "Value " + i);
            record.put("value12", "Value " + i);
            record.put("value13", "Value " + i);
            record.put("value14", "Value " + i);
            data.add(record);
        }
        return data;
    }

    @PostMapping("/select")
    public Page<Map<String, Object>> select(@RequestBody Map<String,String> param,
                                            @RequestParam(defaultValue="0") int page,
                                            @RequestParam(defaultValue="200") int size) {
        List<Map<String,Object>> allData = fetchData(param); // 自己构造数据或查询数据库
        int start = page * size;
        int end = Math.min(start + size, allData.size());
        List<Map<String,Object>> pageData = allData.subList(start,end);

        return new PageImpl<>(pageData, PageRequest.of(page, size), allData.size());
    }


    // 模拟内存数据源
    private final List<Map<String,String>> dataSourceList = new ArrayList<>();

    {
        // 初始化模拟数据
        for (int i = 0; i < 15; i++) {
            Map<String,String> ds = new HashMap<>();
            ds.put("id", String.valueOf(i+1));
            ds.put("name", "数据源" + (i+1));
            ds.put("env", "pl4");
            ds.put("type", i % 2 == 0 ? "mysql" : "oracle");
            ds.put("url", "127.0.0.1");
            ds.put("driver", i % 2 == 0 ? "com.mysql.cj.jdbc.Driver" : "oracle.jdbc.OracleDriver");
            ds.put("username", "root");
            ds.put("password", "******");
            ds.put("createTime", "2025-09-28 22:30:00");
            dataSourceList.add(ds);
        }
    }


    // 查询列表
    @GetMapping("/dsList")
    public List<Map<String,String>> dsList(@RequestParam(defaultValue="pl4") String env) {
        return dataSourceList;
    }

    // 新增数据源
    @PostMapping("/add")
    public boolean addDatasource(@RequestBody Map<String,String> param) {
        // 生成新的ID
        int newId = dataSourceList.stream().mapToInt(ds -> Integer.parseInt(ds.get("id"))).max().orElse(0) + 1;
        param.put("id", String.valueOf(newId));
        param.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dataSourceList.add(param);
        System.out.println("Added datasource: " + param);
        return true;
    }

    // 修改数据源
    @PostMapping("/update")
    public boolean updateConnect(@RequestBody Map<String,String> param){
        String id = param.get("id");
        for (Map<String,String> ds : dataSourceList) {
            if (ds.get("id").equals(id)) {
                ds.putAll(param); // 覆盖字段
                System.out.println("Updated datasource: " + ds);
                return true;
            }
        }
        return false;
    }

    // 删除数据源
    @PostMapping("/delete")
    public boolean deleteDatasource(@RequestBody Map<String,String> param) {
        String id = param.get("id");
        boolean removed = dataSourceList.removeIf(ds -> ds.get("id").equals(id));
        System.out.println("Deleted datasource id=" + id + ", success=" + removed);
        return removed;
    }

    // 检查连接
    @PostMapping("/checkConn")
    public boolean checkConnection(@RequestBody Map<String,String> param){
        String url = param.get("url");
        String username = param.get("username");
        String password = param.get("password");
        String driver = param.get("driver");
        String type = param.get("type");
        String name = param.get("name");

        System.out.println("Testing connection to DB:" + url + " with user: " + username);
        // 这里可以加实际JDBC连接测试
        if (Math.random()<0.6) {
            return false; // 模拟80%失败
        }
        return true; // 模拟总是成功
    }

    @GetMapping("get")
    public Map<String,String> get(@RequestParam String id, @RequestParam(defaultValue="pl4") String env){
        for (Map<String,String> ds : dataSourceList) {
            if (ds.get("id").equals(id)) {
                return ds;
            }
        }
        return Collections.emptyMap();
    }





    @GetMapping("/getApps")
    public List<String> getApps(@RequestParam(defaultValue="pl4") String env){
        ArrayList<String> list = new ArrayList<>();
        list.add("ydh");
        list.add("ydb");
        list.add("ydb2");
        list.add("ydb3");
        return list;
    }

}
