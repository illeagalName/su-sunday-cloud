package com.haier.system.algorithm.aco;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class AntCore {
//    信息素矩阵
//            蚂蚁个数
//    城市个数
//            α
//    β
//            ρ
//    Q
//            最佳路径长度
//    最佳路径列表
//            蚂蚁数组
//    迭代次数
//            距离矩阵

    /**
     * 迭代次数
     */
    private int iterMax;

    /**
     * 城市个数
     */
    private int cityNum;

    /**
     * 蚂蚁个数
     */
    private int antNum;

    /**
     * 蚁群
     */
    private Ant[] ants;

    /**
     * 距离矩阵
     * 以米为单位
     */
    private int[][] distance;

    /**
     * 信息启发因子
     */
    private double alpha;

    /**
     * 期望启发因子
     */
    private double beta;

    /**
     * 信息素挥发系数
     */
    private double rho;

    /**
     * 蚂蚁循环一次所释放的信息素总量
     */
    private double Q;

    /**
     * 信息素矩阵
     */
    private double[][] pheromone;

    /**
     * 最佳路径
     */
    private List<Integer> bestPath;

    /**
     * 最佳路径长度
     */
    private int bestPathLength;

    public AntCore(int iterMax, int cityNum, int antNum, int[][] distance, double alpha, double beta, double rho, double Q) {
        this.iterMax = iterMax;
        this.cityNum = cityNum;
        this.antNum = antNum;
        this.distance = distance;
        this.alpha = alpha;
        this.beta = beta;
        this.rho = rho;
        this.Q = Q;
        this.ants = new Ant[antNum];
        // 初始化 蚁群
        initAntGroup();
    }


    private void initAntGroup() {
        // 初始化最佳路径长度为最大
        bestPathLength = Integer.MAX_VALUE;
        bestPath = new ArrayList<>();

        // 初始时刻，各个城市之间连接路径的信息素浓度相同，
        double pheromone0 = calInitPheromone();

        // 初始化信息素
        pheromone = new double[cityNum][cityNum];
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < cityNum; j++) {
                if (i == j) {
                    pheromone[i][j] = 0;
                } else {
                    pheromone[i][j] = pheromone0;
                }
            }
        }
        // 初始化每一个蚂蚁
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(alpha, beta, cityNum, distance);
        }
    }

    /**
     * 初始浓度需要优化
     * 太小，算法容易早熟
     * 太大，指导作用太低
     * <p>第一种
     * <p>C = m/L’，其中L’是最短路径的长度
     * 扩大搜索路径，避免过早的陷入局部最优
     * <p>第二种
     * <p>
     * 1 / (avg * N)，其中avg为图网中所有边边权的平均数。
     *
     * <p>第三种
     * <p>
     * 前辈经验，试错法
     *
     * @return
     */
    private double calInitPheromone() {
//        double temp = 0;
//        int cnt = 0;
//        for (int i = 0; i < cityNum; i++) {
//            for (int j = 0; j < cityNum; j++) {
//                temp += i != j ? distance[i][j] : 0;
//                cnt += i != j ? 1 : 0;
//            }
//        }
//        return (double) cnt / (temp * cityNum);
        return 0.01;
    }

    /**
     * 迭代
     */
    public Pair<Integer, List<Integer>> iteration() {
        for (int iter = 0; iter < iterMax; iter++) {
            // 每一个蚂蚁开始寻找路径
            for (int i = 0; i < antNum; i++) {
                // 初始化的时候已经选择了一个城市，这次循环次数 cityNum-1
                for (int j = 0; j < cityNum - 1; j++) {
                    // 可以多线程并行行走
                    ants[i].selectNextCity(pheromone);
                }
                // 每一只蚂蚁遍历完所有的城市 需要将 起始城市 添加上，形成首尾相映，tabu个数为cityNum+1
                List<Integer> tabu = ants[i].getTabu();
                tabu.add(ants[i].getFirstCity());
                int tourLength = ants[i].calculateTourLength();
                if (tourLength < bestPathLength) {
                    // 寻找最近长度和最佳路径
                    bestPathLength = tourLength;
                    bestPath = new ArrayList<>(tabu);
                }

                // 计算当前这只蚂蚁的信息素变化矩阵
                for (int j = 0; j < cityNum; j++) {
                    // 每只蚂蚁走过的每相邻城市之间留下的信息素
                    double v = Q / tourLength;
                    double[][] delta = ants[i].getDelta();
                    delta[tabu.get(j)][tabu.get(j + 1)] = v;
                    delta[tabu.get(j + 1)][tabu.get(j)] = v;
                }
            }

            // 更新整个信息素矩阵
            // ① 挥发
            for (int i = 0; i < cityNum; i++) {
                for (int j = 0; j < cityNum; j++) {
                    pheromone[i][j] = pheromone[i][j] * (1 - rho);
                }
            }
            // ②留下
            for (int i = 0; i < cityNum; i++) {
                for (int j = 0; j < cityNum; j++) {
                    for (int k = 0; k < antNum; k++) {
                        pheromone[i][j] += ants[k].getDelta()[i][j];
                    }
                }
            }

            // 重新初始化蚁群
            for (int i = 0; i < antNum; i++) {
                ants[i] = new Ant(alpha, beta, cityNum, distance);
            }
        }
        return Pair.of(bestPathLength, bestPath);
    }
}
