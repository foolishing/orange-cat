package com.zzd.wzqjx;


import android.content.Context;
import android.graphics.Point;
import java.util.ArrayList;

public class AI {
    private Context context;

    public AI()
    {
        this.context = context;
    }
    public static final int CHESSBOARD_SIZE = 15;
    private int[][] weights = new int[CHESSBOARD_SIZE][CHESSBOARD_SIZE];
    private ArrayList<Point> whitePoints;
    private ArrayList<Point> blackPoints;
    private Point point;
    private int whiteNum = 0;//五元组中的黑棋数量
    private int blackNum = 0;//五元组中的白棋数量
    private int weight = 0;//五元组得分临时变量

    public Point AI(ArrayList<Point> whitePoints,ArrayList<Point>blackPoints) {
        this.whitePoints = whitePoints;
        this.blackPoints = blackPoints;
        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++) {
                weights[i][j] = 0;
            }


        //每次机器找寻落子位置，评分都重新算一遍（虽然算了很多多余的，因为上次落子时候算的大多都没变）
        //先定义一些变量
        int whiteNum = 0;//五元组中的黑棋数量
        int blackNum = 0;//五元组中的白棋数量
        int weight = 0;//五元组得分临时变量


        //1.扫描横向的15个行
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                int k = j;
                while (k < j + 5) {
                    //chessboard[0][0]  chessboard[0][1]  chessboard[0][2]
                    //chessboard[0][1]  chessboard[0][2]  chessboard[0][3]
                    if (whitePoints.contains(new Point(i, k)))
                        whiteNum++;
                    if (blackPoints.contains(new Point(i, k)))
                        blackNum++;

                    k++;
                }
                weight = weight(whiteNum, blackNum);
                //为该五元组的每个位置添加分数
                for (k = j; k < j + 5; k++) {
                    weights[i][k] += weight;
                }
                //置零
                whiteNum = 0;//五元组中的黑棋数量
                blackNum = 0;//五元组中的白棋数量
                weight = 0;//五元组得分临时变量
            }
        }

        //2.扫描纵向15行
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 11; j++) {
                int k = j;
                while (k < j + 5) {
                    if (blackPoints.contains(new Point(k, i))) blackNum++;
                    if (whitePoints.contains(new Point(k, i))) whiteNum++;

                    k++;
                }
                weight = weight(whiteNum, blackNum);
                //为该五元组的每个位置添加分数
                for (k = j; k < j + 5; k++) {
                    weights[k][i] += weight;
                }
                //置零
                whiteNum = 0;//五元组中的黑棋数量
                blackNum = 0;//五元组中的白棋数量
                weight = 0;//五元组得分临时变量
            }
        }

        //3.扫描右上角到左下角上侧部分
        for (int i = 14; i >= 4; i--) {
            for (int k = i, j = 0; j < 15 && k >= 0; j++, k--) {
                int m = k; //x 14 13
                int n = j; //y 0  1
                while (m > k - 5 && k - 5 >= -1) {//m = 4  k=4  4,0  3,1, 2,2  1,3  0,4
                    if (blackPoints.contains(new Point(m, n))) blackNum++;
                    if (whitePoints.contains(new Point(m, n))) whiteNum++;
                    m--;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                //System.out.println(m+"==>"+(k-5));
                if (m == k - 5) {
                    weight = weight(whiteNum, blackNum);
                    //为该五元组的每个位置添加分数
                    for (m = k, n = j; m > k - 5; m--, n++) {
                        weights[m][n] += weight;
                    }
                }

                //置零
                whiteNum = 0;//五元组中的黑棋数量
                blackNum = 0;//五元组中的白棋数量
                weight = 0;//五元组得分临时变量

            }
        }

        //4.扫描右上角到左下角下侧部分
        for (int i = 1; i < 15; i++) {
            for (int k = i, j = 14; j >= 0 && k < 15; j--, k++) {
                int m = k;//y 1
                int n = j;//x 14
                while (m < k + 5 && k + 5 <= 15) {
                    if (blackPoints.contains(new Point(n, m))) blackNum++;
                    if (whitePoints.contains(new Point(n, m))) whiteNum++;

                    m++;
                    n--;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if (m == k + 5) {
                    weight = weight(whiteNum, blackNum);
                    //为该五元组的每个位置添加分数
                    for (m = k, n = j; m < k + 5; m++, n--) {
                        weights[n][m] += weight;
                    }
                }
                //置零
                whiteNum = 0;//五元组中的黑棋数量
                blackNum = 0;//五元组中的白棋数量
                weight = 0;//五元组得分临时变量

            }
        }

        //5.扫描左上角到右下角上侧部分
        for (int i = 0; i < 11; i++) {
            for (int k = i, j = 0; j < 15 && k < 15; j++, k++) {
                int m = k;
                int n = j;
                while (m < k + 5 && k + 5 <= 15) {
                    if (blackPoints.contains(new Point(m, n))) blackNum++;
                    if (whitePoints.contains(new Point(m, n))) whiteNum++;

                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if (m == k + 5) {
                    weight = weight(whiteNum, blackNum);
                    //为该五元组的每个位置添加分数
                    for (m = k, n = j; m < k + 5; m++, n++) {
                        weights[m][n] += weight;
                    }
                }

                //置零
                whiteNum = 0;//五元组中的黑棋数量
                blackNum = 0;//五元组中的白棋数量
                weight = 0;//五元组得分临时变量

            }
        }

        //6.扫描左上角到右下角下侧部分
        for (int i = 1; i < 11; i++) {
            for (int k = i, j = 0; j < 15 && k < 15; j++, k++) {
                int m = k;
                int n = j;
                while (m < k + 5 && k + 5 <= 15) {
                    if (blackPoints.contains(new Point(n, m))) blackNum++;
                    if (whitePoints.contains(new Point(n, m))) whiteNum++;

                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if (m == k + 5) {
                    weight = weight(whiteNum, blackNum);
                    //为该五元组的每个位置添加分数
                    for (m = k, n = j; m < k + 5; m++, n++) {
                        weights[n][m] += weight;
                    }
                }

                //置零
                whiteNum = 0;//五元组中的黑棋数量
                blackNum = 0;//五元组中的白棋数量
                weight = 0;//五元组得分临时变量

            }
        }

        //从空位置中找到得分最大的位置
        int a = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (!whitePoints.contains(new Point(i, j))
                        && !blackPoints.contains(new Point(i, j))
                        && a < weights[i][j]) {
                    a = weights[i][j];
                    point = new Point(i, j);
                }
            }

        }
        return point;
    }



        //各种五元组情况评分表
        public int weight ( int whiteNum, int blackNum){

            //1.既有人类落子，又有机器落子，判分为0
            if (whiteNum > 0 && blackNum > 0) {
                return 0;
            }
            //2.全部为空，没有落子，判分为7
            if (whiteNum == 0 && blackNum == 0) {
                return 7;
            }
            //3.机器落1子，判分为35
            if (blackNum == 1) {
                return 35;
            }
            //4.机器落2子，判分为800
            if (blackNum == 2) {
                return 800;
            }
            //5.机器落3子，判分为15000
            if (blackNum == 3) {
                return 15000;
            }
            //6.机器落4子，判分为800000
            if (blackNum == 4) {

                return 800000;
            }
            //7.人类落1子，判分为15
            if (whiteNum == 1) {
                return 15;
            }
            //8.人类落2子，判分为400
            if (whiteNum == 2) {
                return 400;
            }
            //9.人类落3子，判分为1800
            if (whiteNum == 3) {
                return 1800;
            }
            //10.人类落4子，判分为100000
            if (whiteNum == 4) {

                return 100000;
            }
            return weight;//若是其他结果肯定出错了。这行代码根本不可能执行
        }

    }


