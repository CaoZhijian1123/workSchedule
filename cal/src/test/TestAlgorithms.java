package test;

import java.awt.*;

public class TestAlgorithms {
    public int[][] demandMatrix;
    private final int resCount = 3;
    private final int workCount = 4;
    private final int workersCount = 3;
    public int[][][] willingMatrices;

    public static void main(String[] args) {
        TestAlgorithms testAlgorithms = new TestAlgorithms();
        testAlgorithms.print(testAlgorithms.demandMatrix);
        testAlgorithms.print(testAlgorithms.willingMatrices);
        testAlgorithms.procMain();
        System.out.println("分配完成");
        testAlgorithms.print(testAlgorithms.willingMatrices);

    }


    public TestAlgorithms() {
        init();
    }

    /**
     * generate demand matrix and workers' willing matrix
     */
    private void init() {
        demandMatrix = new int[resCount][workCount];
        willingMatrices = new int[workersCount][resCount][workCount];

        for (int i = 0; i < resCount; i++) {
            for (int j = 0; j < workCount; j++) {
                demandMatrix[i][j] = (int) Math.round(Math.random() * 2);
            }
        }

        for (int i = 0; i < workersCount; i++) {
            for (int j = 0; j < resCount; j++) {
                for (int k = 0; k < workCount; k++) {
                    willingMatrices[i][j][k] = (int) Math.round(Math.random());
                }
            }
        }
    }


    private void procMain() {
        while (true) {
            proc();
            Tag tag = check();
            if (tag == null) {
                break;
            }
            modifyData(tag);
        }
    }

    /**
     * 将供给小于需求的班次优先满足，已安排人员的同一班次的其他供给能力设置为0
     */
    private void proc() {
        for (int i = 0; i < resCount; i++) {
            for (int j = 0; j < workCount; j++) {
                int temp = 0;
                for (int k = 0; k < workersCount; k++) {
                    temp += willingMatrices[k][i][j];
                }
                if (temp <= demandMatrix[i][j]) {
                    for (int k = 0; k < workersCount; k++) {
                        if (willingMatrices[k][i][j] == 1) {
                            for (int l = 0; l < resCount; l++) {
                                if (l == i) continue;
                                willingMatrices[k][l][j] = 0;
                            }
                        }
                    }

                }
            }
        }

    }

    /**
     * 代码核心部分，性能提高关键点
     * 方案1：将多余供给置为0，原则：顺序修改，
     *
     * @param tag
     */
    private void modifyData(Tag tag) {
        int reduce = tag.total - tag.demand;
        for (int i = 0; i < workersCount && reduce > 0; i++) {
            if (willingMatrices[i][tag.x][tag.y] == 1) {
                willingMatrices[i][tag.x][tag.y] = 0;
                reduce--;
            }
        }

    }

    /**
     * 检查分配是否完成，
     *
     * @return 标记不满足分配要求的位置tag, 若满足则返回null
     */
    private Tag check() {
        for (int i = 0; i < resCount; i++) {
            for (int j = 0; j < workCount; j++) {
                int temp = 0;
                for (int k = 0; k < workersCount; k++) {
                    temp += willingMatrices[k][i][j];
                }
                if (temp > demandMatrix[i][j]) {
                    return new Tag(i, j, demandMatrix[i][j], temp);
                }
            }
        }
        return null;
    }


    /**
     * @param matrix
     */
    public void print(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            System.out.println("Demand Matrix Error");
            return;
        }
        System.out.println("Demand Matrix________________________________________");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("___________________________________________________");
    }


    /**
     * @param willingMatrices
     */
    private void print(int[][][] willingMatrices) {
        if (willingMatrices.length == 0 || willingMatrices[0].length == 0 || willingMatrices[0][0].length == 0) {
            System.out.println("Willing Matrixs Error");
            return;
        }
        for (int i = 0; i < workersCount; i++) {

            System.out.println("worker " + (i + 1) + "_________________________________________");
            for (int j = 0; j < resCount; j++) {
                for (int k = 0; k < workCount; k++) {
                    System.out.print(willingMatrices[i][j][k] + "\t");
                }
                System.out.println();
            }
            System.out.println("___________________________________________________");
        }

    }

    private class Tag {
        public int x;
        public int y;
        public int demand;
        public int total;

        public Tag(int X, int Y, int Demand, int Total) {
            this.x = X;
            this.y = Y;
            this.demand = Demand;
            this.total = Total;
        }

        public Tag() {

        }

    }
}


