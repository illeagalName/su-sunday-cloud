package com.haier.system.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 轮盘赌
 * @author Ami
 */
public class RouletteWheelVerification {
    public static void main(String[] args) {
        Map<String, Integer> statistics = new HashMap<>();
        statistics.put("A", 0);
        statistics.put("B", 0);
        statistics.put("C", 0);
        statistics.put("D", 0);

        // A的概率
        double probabilityA = 0.4;
        // B的概率
        double probabilityB = 0.3;
        // C的概率
        double probabilityC = 0.2;
        // D的概率
        double probabilityD = 0.1;

        Integer iterations = 100000000;

        for (int i = 0; i < iterations; i++) {
            double random = Math.random();
            if (random <= probabilityA) {
                statistics.put("A", statistics.get("A") + 1);
            } else if (random <= (probabilityA + probabilityB)) {
                statistics.put("B", statistics.get("B") + 1);
            } else if (random <= (probabilityA + probabilityB + probabilityC)) {
                statistics.put("C", statistics.get("C") + 1);
            } else {
                statistics.put("D", statistics.get("D") + 1);
            }
        }

        System.out.println("A 的次数 ： " + statistics.get("A") + "，比例：" + (statistics.get("A") / (double) iterations));
        System.out.println("B 的次数 ： " + statistics.get("B") + "，比例：" + (statistics.get("B") / (double) iterations));
        System.out.println("C 的次数 ： " + statistics.get("C") + "，比例：" + (statistics.get("C") / (double) iterations));
        System.out.println("D 的次数 ： " + statistics.get("D") + "，比例：" + (statistics.get("D") / (double) iterations));
    }
}
