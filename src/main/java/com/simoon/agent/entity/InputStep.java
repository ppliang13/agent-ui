package com.simoon.agent.entity;

import java.util.HashMap;
import java.util.Map;


public class InputStep extends AbstractProcessStep {

    public InputStep(String name) {
        super(name);
    }

    public InputStep(String name, String desc) {
        super(name, desc);
    }

    @Override
    public Object execute(Map<String, Object> context) {
        this.output=this.input;
        return input;
    }

    @Override
    public Object getValue(String path) {
        return input.get(path);
    }

    @Override
    public Object getOutput() {
        return this.input;
    }

    public InputStep addInput(String key, String value) {
        if(this.input == null) {
            this.input=new HashMap<String, String>();
        }
        this.input.put(key, value);
        return this;
    }
}