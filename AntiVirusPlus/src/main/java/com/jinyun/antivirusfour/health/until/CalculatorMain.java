package com.jinyun.antivirusfour.health.until;

/**
 * 各项指标计算
 */


public class CalculatorMain {
    public static String run(float weight, float height, float age, boolean humanSex){


        float bodyMassIndex = weight / (height * height) ;//BMI
        float basalMetabolicRate = 0;
                
        //male
        if (!humanSex) {
            basalMetabolicRate = (float) (66 + (6.23 * weight) + (12.7 * height) - (6.8 * age));
            System.out.println("male selected");

        //female
        } else {
            basalMetabolicRate = (float) (655 + (4.35 * weight) + (4.7 * height) - (4.7 * age));
            System.out.println("female selected");

        }

        double toLoseWeight = .75 * basalMetabolicRate;

        double howManyDays = 3500 / (basalMetabolicRate - toLoseWeight);

        return "BMI = " + Math.round(bodyMassIndex) + "%." + "  BMR = " + Math.round(basalMetabolicRate) + "." + "\n"
                + "75% of your BMR is: " + Math.round(toLoseWeight) + "." + "\n"
                + "If you intake " + Math.round(toLoseWeight) + " calories each day, it will take you " + Math.round(howManyDays) + " days to lose a pound.";

    }
}
