package com.simoon.agent.entity;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.HashMap;
import java.util.Map;

public class CheckStep extends AbstractProcessStep {


    public CheckStep(String name,String desc) {
        super(name,desc);
    }


    public CheckStep(String name){
        super(name);
    }

    @Override
    public Object execute(Map<String, Object> context) {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        for (Map.Entry<String, String> entry : input.entrySet()) {
            String script = entry.getValue();
            Object evaluate = shell.evaluate(script);
            if(evaluate instanceof Boolean){
                if(!(Boolean)evaluate){
                    this.output= false;
                    return false;
                }
            }
        }
        this.output= true;
        return true;
    }

    @Override
    public Object getValue(String path) {
        return null;
    }

    public CheckStep addInput(String key, String value) {
        if(this.input == null) {
            this.input=new HashMap<String, String>();
        }
        this.input.put(key, value);
        return this;
    }
}