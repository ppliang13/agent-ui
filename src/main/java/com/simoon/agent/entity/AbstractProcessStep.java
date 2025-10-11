package com.simoon.agent.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: simoon
 * @CreateTime: 2025-10-03
 * @Description:
 * @Version: 1.0
 */
public abstract class AbstractProcessStep  {
    protected String name;
    protected String desc;
    protected Map<String,String> input;
    protected Object output;

    public AbstractProcessStep(String name,String desc) {
        this.name = name;
        this.desc = desc;
    }

    public AbstractProcessStep(String name) {
        this.name = name;
        this.desc = "";
    }


    public String getName() { return name; }


    public Object getOutput() { return output; }

    public Map<String, String> getInput() {
        return input;
    }

    public void setInput(Map<String, String> input) {
        this.input = input;
    }



    public abstract Object execute(Map<String, Object> context);
    public abstract Object getValue(String path);
}
