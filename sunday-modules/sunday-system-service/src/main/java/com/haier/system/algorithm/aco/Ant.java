package com.haier.system.algorithm.aco;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Ant {

    /**
     * 信息启发因子
     */
    private double alpha;

    /**
     * 期望启发因子
     */
    private double beta;

    /**
     * 城市数量
     */
    private int cityNum;

    /**
     * 信息素变化矩阵
     */
    private double[][] delta;

    /**
     * 禁忌表，表示访问过的城市
     */
    private List<Integer> tabu;

    /**
     * 还未访问过的城市
     */
    private List<Integer> allow;

    /**
     * 距离矩阵
     */
    private int[][] distance;

    /**
     * 刚刚访问的城市节点
     */
    private Integer nextCity;

    /**
     * 第一个访问的城市
     */
    private Integer firstCity;

    public Ant(double alpha, double beta, int cityNum, int[][] distance) {
        this.alpha = alpha;
        this.beta = beta;
        this.cityNum = cityNum;
        this.distance = distance;
        this.delta = new double[cityNum][cityNum];
        tabu = new ArrayList<>();
        // 初始化允许城市
        allow = IntStream.range(0, cityNum).boxed().collect(Collectors.toList());
        // 初始化信息素变化矩阵 全部为0
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                delta[i][j] = 0;
            }
        }

        // 随机选一个城市,这里是第一个访问的城市
//        firstCity = ThreadLocalRandom.current().nextInt(cityNum);
        // 固定第一个城市为0
        firstCity = 0;
        // 记录起始城市
        nextCity = firstCity;
        allow.remove(nextCity);
        tabu.add(nextCity);
    }

    /**
     * 概率公式
     *
     * @param pheromone 信息素矩阵
     */
    public void selectNextCity(double[][] pheromone) {
        // 去往下一个城市的概率
        double[] probability = new double[cityNum];
//        for (int i = 0; i < cityNum; i++) {
//            // 所有概率初始化为0
//            probability[i] = 0;
//        }
        // 分母 Σ和
        double sum = 0;
        for (Integer city : allow) {
            sum += Math.pow(pheromone[nextCity][city], alpha) * Math.pow(1.0 / distance[nextCity][city], beta);
        }
        for (Integer city : allow) {
            // 计算到达下一个城市的概率
            probability[city] = (Math.pow(pheromone[nextCity][city], alpha) * Math.pow(1.0 / distance[nextCity][city], beta)) / sum;
        }

        // 采用轮盘赌选择下一个城市
        double p = ThreadLocalRandom.current().nextDouble();
        double pN = 0;

        for (Integer city : allow) {
            pN += probability[city];
            if (pN >= p) {
                nextCity = city;
                break;
            }
        }
        allow.remove(nextCity);
        tabu.add(nextCity);
    }

    /**
     * 计算当前这只蚂蚁走过的路径长度
     *
     * @return length
     */
    public int calculateTourLength() {
        int length = 0;
        for (int i = 0; i < cityNum; i++) {
            length += distance[tabu.get(i)][tabu.get(i + 1)];
        }
        return length;
    }
}
