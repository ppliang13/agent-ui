package com.simoon.agent.entity;

import com.simoon.agent.service.ApplicationSequenceUtil;
import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: simoon
 * @CreateTime: 2025-10-09
 * @Description: 获取序列
 * @Version: 1.0
 */
public class SequenceCommonUtils {


    private static final Logger logger = LogManager.getLogger(SequenceCommonUtils.class);
    public static final HashMap<String, Long> sequenceMap = new HashMap<>();

    public static final HashMap<String, Long> sequenceMax = new HashMap<>();


    /**
     * 序列 获取
     * 缓存大小 SEQUENCEPROPERTIES 文件中的配置
     *
     * @param key 序列名称
     * @return 当前自增序列
     */
    public static long getSequence(String key) {
        long startTime = System.currentTimeMillis();
        ReentrantLock lock = new ReentrantLock();
        long value = 0L;
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("序列长度为空");
        }
        lock.lock();
        try {
            if (sequenceMap.containsKey(key)) {
                long current_value = sequenceMap.get(key);
                long max_value = sequenceMax.get(key);
                if (current_value > max_value) {
                    List<Long> list = ApplicationSequenceUtil.next(key, getSequenceCount(key));
                    sequenceMax.put(key, list.get(list.size() - 1));
                    sequenceMap.put(key, list.get(0) + 1);
                    value = list.get(0);
                } else {
                    value = current_value;
                    sequenceMap.put(key, current_value + 1);
                }
            } else {
                List<Long> list = ApplicationSequenceUtil.next(key, getSequenceCount(key));
                sequenceMax.put(key, list.get(list.size() - 1));
                sequenceMap.put(key, list.get(0) + 1);
                value = list.get(0);
            }
        } finally {
            lock.unlock();
        }
        long endTime = System.currentTimeMillis();

        return value;
    }

    private static int getSequenceCount(String key) {
        return 5;
    }


    public static void main(String[] args) {
        ApplicationSequenceUtil.setCurrentValue("ORDER_SEQ", 986000L);
        for (int i = 0; i < 10000; i++) {
            long orderSeq = getSequence("ORDER_SEQ");
            System.out.println(orderSeq);
        }
    }
}
