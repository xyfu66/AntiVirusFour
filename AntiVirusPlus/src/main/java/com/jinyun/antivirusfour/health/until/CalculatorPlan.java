package com.jinyun.antivirusfour.health.until;

/**
 * 用于计算到目标体重要多少周
 */

public class CalculatorPlan {
    public static String runPlan(String weight, String target_weight, String plan_to_loss_weight){

        double myWeight = Double.parseDouble(weight)-Double.parseDouble(target_weight);
        double planToLoss = Double.parseDouble(plan_to_loss_weight);

        double result = myWeight/planToLoss;

        return  Math.round(result) +"";
    }
}
