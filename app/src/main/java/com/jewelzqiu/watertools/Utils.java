package com.jewelzqiu.watertools;

/**
 * Created by Jewelz on 2015-6-28.
 */
public class Utils {

    public static double calculateDischarge(double velocity, double diameter) {
        return Math.PI * diameter * diameter / 4 * velocity;
    }

    public static double calculateVelocity(double discharge, double diameter) {
        return discharge / (Math.PI * diameter * diameter / 4);
    }

    public static double calculateDiameter(double discharge, double velocity) {
        return Math.sqrt(discharge / velocity / Math.PI * 4);
    }

    public static double calculateHeadLoss(double length, double gradient) {
        return length * gradient;
    }

    public static double calculateGradient(double velocity, double diameter) {
        if (velocity >= 1.2) {
            return 0.00107 * Math.pow(velocity, 2) * Math.pow(diameter, -1.3);
        } else {
            return 0.000912 * Math.pow(velocity, 2) * Math.pow(diameter, -1.3)
                    * Math.pow((1 + 0.867 / velocity), 0.3);
        }
    }

    public static double calculateVelocityFullPipeFlow(double roughness, double diameter,
            double gradient) {
        return 1 / roughness * Math.pow(diameter / 4, 2.0 / 3) * Math.pow(gradient, 0.5);
    }

}
