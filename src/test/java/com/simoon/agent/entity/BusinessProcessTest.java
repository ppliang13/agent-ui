package com.simoon.agent.entity;


import org.junit.Test;


public class BusinessProcessTest {


    @Test
    public void test() {
        InputStep inputStep = new InputStep("input1");

        inputStep.addInput("name","psl");


        SqlStep sqlStep = new SqlStep("sql1")
                .addInput("dev", "dev")
                .addInput("sql", "select * from user where name='${input1#name}'");


        SqlStep sqlStep2 = new SqlStep("sql2")
                .addInput("dev", "dev")
                .addInput("sql", "select * from user where name='xxx'");

        CheckStep checkStep = new CheckStep("check1", "检查sql结果是否正确");
        checkStep.addInput("check1", "\"${sql1#[10].name}\".equals(\"simoon11\");");

        BusinessProcess businessProcess = new BusinessProcess("测试模板", "测试案例",
                "run(\"input1\"); run(\"sql1\"); if(check(\"check1\")){run(\"sql2\"); } ");
        businessProcess.addStep(inputStep).addStep(sqlStep).addStep(checkStep).addStep(sqlStep2);
        businessProcess.execute();
        System.out.println("businessProcess = " + businessProcess);



    }
}