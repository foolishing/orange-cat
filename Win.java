package com.zzd.wzqjx;

import android.content.Context;
import android.graphics.Point;

import java.util.List;

public class Win {

    private boolean isGameOver = false;//游戏是否结束
    private int maxNumber = 5;
    private int currentNumber = 0;//当前棋子连在一起的数量
    private Context context;
    private boolean isWqWinFlag;//判断标识 判断白棋是否获胜

    public Win(Context context)
    {
        this.context = context;
    }

    private Boolean isHorizontalFive(int x , int y, List<Point> points)//水平方向
    {
        for (int i = 0;i <maxNumber; i++)
        {
            if (points.contains(new Point(x + i,y)))
            {
                currentNumber++;
            }
            else
            {
                break;
            }
        }
        if (currentNumber == maxNumber)
        {
            return true;
        }
        currentNumber = 0;

        return false;

    }

    private Boolean isVerticalFive(int x , int y, List<Point> points)//竖直方向
    {
        for (int i = 0;i <maxNumber; i++)
        {
            if (points.contains(new Point(x ,y + i)))
            {
                currentNumber++;
            }
            else
            {
                break;
            }
        }
        if (currentNumber == maxNumber)
        {
            return true;
        }
        currentNumber = 0;

        return false;

    }

    private Boolean isSkewFive(int x , int y, List<Point> points)//斜的方向
    {
        for (int i = 0;i <maxNumber; i++)
        {
            if (points.contains(new Point(x + i ,y + i)))
            {
                currentNumber++;
            }
            else
            {
                break;
            }
        }
        if (currentNumber == maxNumber)
        {
            return true;
        }
        currentNumber = 0;

        for (int i = 0;i <maxNumber; i++)
        {
            if (points.contains(new Point(x + i ,y - i)))
            {
                currentNumber++;
            }
            else
            {
                break;
            }
        }
        if (currentNumber == maxNumber)
        {
            return true;
        }
        currentNumber = 0;

        return false;
    }

    private boolean isFiveConnect(List<Point> points)
    {
        for (Point point : points)
        {
            int x = point.x;
            int y = point.y;

            if (isHorizontalFive(x,y,points)
            || isVerticalFive(x,y,points)
            || isSkewFive(x,y,points))
            {
                return true;
            }
        }

        return  false;
    }
    public boolean isWqWin(List<Point> points)
    {
        if (isFiveConnect(points))
        {
            return  true;
        }

        return false;
    }
    public boolean isBqWin(List<Point> points)
    {
        if (isFiveConnect(points))
        {
            return true;
        }

        return false;

    }

    public  boolean isGameOver(List<Point> whitePoints,List<Point> blackPoints)
    {
        boolean whiteWin = isWqWin(whitePoints);
        boolean blackWin = isBqWin(blackPoints);

        if(whiteWin || blackWin)
        {
            isGameOver = true;
            isWqWinFlag = whiteWin;
        }

        return isGameOver;
    }

    public boolean isWqWinFlag()
    {
        return isWqWinFlag;
    }


}
