package com.simoon.agent.service;

import org.apache.groovy.parser.antlr4.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟数据库序列生成工具
 * 支持：最小值、最大值、间隔、自增、当前值
 */
public class ApplicationSequenceUtil {

    // 模拟数据库表：存储当前序列值
    private static final Map<String, Long> dbSequenceTable = new ConcurrentHashMap<>();

    // 存储每个序列的配置（最小值、最大值、间隔）
    private static final Map<String, SequenceConfig> sequenceConfigMap = new ConcurrentHashMap<>();

    static {
        // 初始化不同序列 key 的初始配置
        sequenceConfigMap.put("ORDER_SEQ", new SequenceConfig(0L, 99_9999L, 2000));
        sequenceConfigMap.put("USER_SEQ", new SequenceConfig(0L, 7_000_000L, 2000));

        // 初始化当前数据库值（可以等于 minValue）
        dbSequenceTable.put("ORDER_SEQ", 1_000_000L);
        dbSequenceTable.put("USER_SEQ", 5_000_000L);
    }

    /**
     * 获取单个序列
     */
    public static long next(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("序列key为空");
        }

        SequenceConfig cfg = sequenceConfigMap.get(key);
        if (cfg == null) {
            throw new RuntimeException("未配置的序列key: " + key);
        }

        synchronized (ApplicationSequenceUtil.class) {
            long current = dbSequenceTable.getOrDefault(key, cfg.getMinValue());
            long nextVal = current + cfg.getInterval();

            if (nextVal > cfg.getMaxValue()) {
                System.out.println("[警告] key=" + key + " 到达最大值，重置为最小值");
                nextVal = cfg.getMinValue();
            }

            dbSequenceTable.put(key, nextVal);

            System.out.printf("[DB模拟] key=%s, 当前=%d, 下一个=%d, 区间[%d~%d]%n",
                    key, current, nextVal, cfg.getMinValue(), cfg.getMaxValue());

            return current;
        }
    }

    /**
     * 获取多个序列（连续调用 count 次）
     */
    public static List<Long> next(String key, int count) {
        List<Long> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(next(key));
        }
        return list;
    }

    /**
     * 修改序列配置（可动态设置最大值、最小值、间隔）
     */
    public static void setConfig(String key, long min, long max, int interval) {
        sequenceConfigMap.put(key, new SequenceConfig(min, max, interval));
        dbSequenceTable.putIfAbsent(key, min);
    }

    /**
     * 获取当前序列的值（测试用）
     */
    public static long getCurrentValue(String key) {
        return dbSequenceTable.getOrDefault(key, 0L);
    }

    /**
     * 手动设置当前序列值（测试边界用）
     */
    public static void setCurrentValue(String key, long value) {
        if (!sequenceConfigMap.containsKey(key)) {
            throw new RuntimeException("未配置的序列key: " + key);
        }
        dbSequenceTable.put(key, value);
        System.out.printf("[手动设置] key=%s 当前值设为 %d%n", key, value);
    }

    // 配置类
    private static class SequenceConfig {
        private final long minValue;
        private final long maxValue;
        private final int interval;

        public SequenceConfig(long minValue, long maxValue, int interval) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.interval = interval;
        }

        public long getMinValue() {
            return minValue;
        }

        public long getMaxValue() {
            return maxValue;
        }

        public int getInterval() {
            return interval;
        }
    }

    // 测试
    public static void main(String[] args) {
        System.out.println(ApplicationSequenceUtil.next("ORDER_SEQ", 5));

        System.out.println("**********************");
        // 模拟边界测试：手动把当前值设置为最大值附近
        setCurrentValue("ORDER_SEQ", 986000L);
        System.out.println("当前值: " + getCurrentValue("ORDER_SEQ"));
        System.out.println(ApplicationSequenceUtil.next("ORDER_SEQ", 10));

        // 测试自定义序列
        setConfig("TEST_SEQ", 10_000L, 10_500L, 200);
        System.out.println(ApplicationSequenceUtil.next("TEST_SEQ", 5));

        // 手动设置 TEST_SEQ 为接近上限
        setCurrentValue("TEST_SEQ", 10_400L);
        System.out.println(ApplicationSequenceUtil.next("TEST_SEQ", 3));
    }
}