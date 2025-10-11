package com.simoon.agent.entity;

import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyCallable;
import groovy.lang.GroovyShell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 业务流程类
 */
public class BusinessProcess {
    /**
     * 流程名称
     */
    private final String processName;
    /**
     * 流程描述
     */
    private final String processDesc;

    /**
     * 执行脚本 groovy
     */
    private final String script;

    /**
     * 流程步骤列表
     */
    private final List<AbstractProcessStep> steps = new ArrayList<>();

    /**
     * 流程上下文，用于存储步骤之间传递的数据
     */
    private final Map<String, Object> context = new HashMap<>();

    public BusinessProcess(String processName, String processDesc, String script) {
        this.processName = processName;
        this.processDesc = processDesc;
        this.script = script;
    }

    /**
     * 添加步骤
     *
     * @param step 步骤实例
     */
    public BusinessProcess addStep(AbstractProcessStep step) {
        steps.add(step);
        return this;
    }

    /**
     * 删除步骤
     *
     * @param processName 步骤名称
     * @return 是否删除成功
     */
    public boolean deleteStep(String processName) {
        return steps.removeIf(step -> step.getName().equals(processName));
    }


    public AbstractProcessStep getStep(String processName) {
        for (AbstractProcessStep step : steps) {
            if (step.getName().equals(processName)) {
                return step;
            }
        }
        return null;
    }

    /**
     * 执行 script 脚本
     */
    public void execute() {
        if (StrUtil.isNotBlank(script)) {
            Binding binding = new Binding();
            binding.setVariable("run", new groovy.lang.Closure<Void>(this) {
                public Void doCall(Object arg) {
                    BusinessProcess.this.run((String) arg);
                    return null;
                }
            });

            binding.setVariable("check", new groovy.lang.Closure<Void>(this) {
                public boolean doCall(Object arg) {
                   return BusinessProcess.this.check((String) arg);
                }
            });

            GroovyShell shell = new GroovyShell(binding);


            shell.evaluate(script);
        }
    }




    public Map<String, Object> getContext() {
        return context;
    }

    public void run(String processName) {
        for (AbstractProcessStep step : steps) {
            if (step.getName().equals(processName)) {
                autoFull(step.input);
                step.execute(context);
                context.put(step.getName(), step.getOutput());
                break;
            }
        }
    }

    private void autoFull(Map<String, String> input) {
        if (input == null) return;

        for (Map.Entry<String, String> entry : input.entrySet()) {
            String value = entry.getValue();
            if (value == null) continue;

            // 查找所有 ${xxx#yyy} 占位符
            List<String> parts = analyze(value);
            for (String part : parts) {
                String realValue = getRealValue(part);
                if (realValue != null) {
                    value = value.replace(part, realValue);
                }
            }
            entry.setValue(value); // 更新 Map 中的值
        }
    }

    // 提取 ${xxx#yyy} 占位符
    private List<String> analyze(String value) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{[^}]+}");
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    // 获取真实值
    private String getRealValue(String part) {
        // part: ${input#cstid}
        String content = part.substring(2, part.length() - 1); // 去掉 ${ }
        String[] split = content.split("#");
        if (split.length != 2) return null;

        String source = split[0]; // 获取step对象
        String key = split[1];    // 取 key 的值
        AbstractProcessStep step = getStep(source);
        if(step!=null){
            Object value = step.getValue(key);
            return value != null ? value.toString() : null;
        }
        return null;
    }

    public boolean check(String processName) {
        run(processName);
        if (context.get(processName) instanceof Boolean) {
            return (Boolean) context.get(processName);
        }
        return false;
    }
}