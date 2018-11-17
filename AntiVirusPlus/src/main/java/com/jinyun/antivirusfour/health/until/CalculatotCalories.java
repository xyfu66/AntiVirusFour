package com.jinyun.antivirusfour.health.until;

/**
 * 计算每天需要的热量
 */

public class CalculatotCalories {
    public static String CaloriesRun(float weight, float height, float age, boolean humanSex){

        float basalMetabolicRate = 0;

        //male
        if (!humanSex) {
            basalMetabolicRate = (float) (66 + (13.7 * weight) + (5 * height) - (6.8 * age));
            System.out.println("male selected");
            //female
        } else {
            basalMetabolicRate = (float) (655 + (9.6 * weight) + (1.7 * height) - (4.7 * age));
            System.out.println("female selected");

        }

        return  Math.round(basalMetabolicRate) + "" ;

    }
}
