package com.jinyun.antivirusfour.health.entity;

import java.io.Serializable;

/**
 * 用户健康画像实体类
 */

public class HumanInfo implements Serializable{
    private String humanHeight;//身高
    private String humanWeight;//体重
    private boolean humanSex;//性别
    private String targetWeight;//目标体重
    private String planToLossWeight;//计划每周减重

    public HumanInfo() {
        super();
    }

    public HumanInfo(String humanHeight, String humanWeight, boolean humanSex,String targetWeight,String planToLossWeight) {
        super();
        this.humanHeight = humanHeight;
        this.humanWeight = humanWeight;
        this.humanSex = humanSex;
        this.targetWeight = targetWeight;
        this.planToLossWeight= planToLossWeight;
    }


    public String getHumanHeight() {
        return humanHeight;
    }

    public void setHumanHeight(String humanHeight) {
        this.humanHeight = humanHeight;
    }

    public String getHumanWeight() {
        return humanWeight;
    }

    public void setHumanWeight(String humanWeight) {
        this.humanWeight = humanWeight;
    }

    public boolean isHumanSex() {
        return humanSex;
    }

    public void setHumanSex(boolean humanSex) {
        this.humanSex = humanSex;
    }

    public String getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(String targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getPlanToLossWeight() {
        return planToLossWeight;
    }

    public void setPlanToLossWeight(String planToLossWeight) {
        this.planToLossWeight = planToLossWeight;
    }

}
