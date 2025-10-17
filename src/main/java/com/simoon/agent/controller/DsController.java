package com.simoon.agent.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: DsController
 * @Author: simoon
 * @Package com.simoon.agent.controller
 * @Date: 2025/10/17 16:58
 * @Description: 数据源列表
 */
@RestController
@RequestMapping("/ds")
public class DsController {

    @GetMapping("/env")
    public List<String> envs() {
        return Arrays.asList("dev", "run", "test");
    }

    @GetMapping("/list")
    public List<String> list(@RequestParam String env) {
        if (env.equals("dev")) {
            return Arrays.asList("dev_ds1", "dev_ds2", "dev_ds3");
        } else if (env.equals("test")) {
            return Arrays.asList("test_ds1", "test_ds2", "test_ds3");
        } else {
            return Arrays.asList("run_ds1", "run_ds2", "run_ds3");
        }
    }

    @GetMapping("/listUrl")
    public Map<String, String> listUrl(@RequestParam String env) {
        HashMap map = new HashMap<String, String>();
        if (env.equals("dev")) {
            map.put("dev_ap1", "http://devhost1:8080/api1");
            map.put("dev_ap2", "http://devhost2:8080/api2");
            map.put("dev_ap3", "http://devhost3:8080/api3");
        } else if (env.equals("test")) {
            map.put("test_ap1", "http://testhost1:8080/api1");
            map.put("test_ap2", "http://testhost2:8080/api2");
            map.put("test_ap3", "http://testhost3:8080/api3");
        } else {
            map.put("run_ap1", "http://runhost1:8080/api1");
            map.put("run_ap2", "http://runhost2:8080/api2");
            map.put("run_ap3", "http://runhost3:8080/api3");
        }
        return map;
    }

    /**
     * 格式化请求字符串 xml 与 json
     *
     * @param str
     * @return
     */
    @PostMapping("/formatXMLOrJSON")
    public String formatXMLOrJSON(@RequestBody String str) {
        return "geshihua";
    }
}
