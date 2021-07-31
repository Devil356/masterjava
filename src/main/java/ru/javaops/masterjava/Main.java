package ru.javaops.masterjava;

import ru.javaops.masterjava.matrix.MatrixUtil;

/**
 * User: gkislin
 * Date: 05.08.2015
 *
 * @link http://caloriesmng.herokuapp.com/
 * @link https://github.com/JavaOPs/topjava
 */
public class Main {
    public static void main(String[] args) {
        System.out.format("Hello MasterJava!");
        System.out.println();
        int matrix1[][] = new int[][] {
                {1, 2, 8, 5},
                {3, 6, 9, 4}
        };
        int matrix2[][] = new int[][]{
                {8, 4},
                {3, 1},
                {4, 9},
                {2, 5}
        };

        int matrixResult[][] = MatrixUtil.singleThreadMultiply(matrix1, matrix2);

        for (int i = 0; i < matrixResult.length; i++) {
            for (int j = 0; j < matrixResult.length; j++) {
                System.out.print(" " + matrixResult[i][j]);
            }
            System.out.println();
        }
    }
}
