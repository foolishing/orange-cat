package com.zzd.wzqjx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class ChessView extends View {

    private Context context;
    private Point choose;
    private Paint paint;
    private Bitmap bq_bm;
    private Bitmap wq_bm;
    private ArrayList<Point> whitePoints;
    private ArrayList<Point> blackPoints;
    private int geShu = 15;
    private int chessPlaneWidth;
    private float chessWidth;
    private float chessScale = 1.0f * 3 / 4;
    private Win isWin;
    private boolean isGameOver;
    private boolean isWhiteWinFlag = true;
    private boolean isWhiteRound = false;
    private boolean isChess =false;
    private boolean mode = false;
    AI ai = new AI();


    public ChessView(Context context) {
        super(context);
        this.context = context;
        init();//初始化操作

    }

    public ChessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ChessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init()
    {
        paint = new Paint();//画笔的实例化
        paint.setColor(Color.BLACK);//画笔的颜色
        paint.setAntiAlias(true);//画笔的抗锯齿
        paint.setDither(true);//画笔的防抖动
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(255);//画笔的透明度
        paint.setStrokeWidth(3);//画笔的线宽


        bq_bm = BitmapFactory.decodeResource(getResources(),R.drawable.bq);
        wq_bm = BitmapFactory.decodeResource(getResources(),R.drawable.wq);


        blackPoints = new ArrayList<>();
        whitePoints = new ArrayList<>();//保存棋子的位置的集合

    }
    //获得画棋盘的区域
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);//获取屏幕的宽度
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int len = width > height ? height : width;

        setMeasuredDimension(len , len);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        chessPlaneWidth = w;//棋盘的宽度
        chessWidth = w * 1.0f / geShu;//棋子的宽度
        int len = (int) ((w * 1.0f / geShu) * chessScale);// w * 1.0f / geShu，获取一个棋盘格的边长。 ((w * 1.0f / geShu) * chessScale)，棋子的缩放尺寸
        wq_bm = Bitmap.createScaledBitmap(wq_bm , len ,len ,true);
        bq_bm = Bitmap.createScaledBitmap(bq_bm , len ,len ,true);

    }
    //绘制棋盘 绘制棋子 判断游戏是否结束
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawChessPlane(canvas);//画棋盘
        drawChess(canvas);//画棋子

        isWin = new Win(context);
        isGameOver = isWin.isGameOver(whitePoints, blackPoints);
        if(isGameOver)
        {
            showDialog();
        }
        if(!isWhiteRound && mode)
        {
             Point point = ai.AI(whitePoints,blackPoints);
             if (whitePoints.isEmpty())
             {
                 point = new Point(geShu/2,geShu/2);
             }
             blackPoints.add(point);

             isWhiteRound = !isWhiteRound;
             invalidate();
        }


    }

    private void drawChessPlane(Canvas canvas) {
        for(int i = 0; i < geShu ; i++)
        {
            int x0 = (int) (chessWidth / 2);//起点
            int x1 = (int) (chessPlaneWidth - chessWidth / 2);//终点
            int y = (int) ((0.5 + i ) * chessWidth);
            canvas.drawLine(x0,y,x1,y,paint);//画横线
            canvas.drawLine(y,x0,y,x1,paint);//画竖线

        }
    }
    private void drawChess(Canvas canvas)
    {
        if(choose!=null)
        {
            isChess = true;
        }

        for (int i =0;i < whitePoints.size(); i++)
        {
            Point point = whitePoints.get(i);
            canvas.drawBitmap(wq_bm,
                    (point.x + 1.0f / 2 - chessScale / 2) * chessWidth,
                    (point.y + 1.0f / 2 - chessScale / 2) * chessWidth,
                    null);
        }

        for (int i = 0; i < blackPoints.size(); i++)
        {
            Point point = blackPoints.get(i);//获取棋子对象
            canvas.drawBitmap(bq_bm,
                    (point.x + 1.0f / 2 - chessScale / 2) * chessWidth,
                    (point.y + 1.0f / 2 - chessScale / 2) * chessWidth,
                    null);
        }


    }
    //重写点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isGameOver = isWin.isGameOver(whitePoints, blackPoints);
        if(isGameOver)
        {
            showDialog();
            return false;
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP)
        {
            int x =(int) event.getX();
            int y =(int) event.getY();

            Point point = new Point((int)(x / chessWidth),(int)(y / chessWidth) );
            //判断落子下有无棋子
            if(!isChess ||!choose.equals(point)){
                choose =point;
                invalidate();
                return false;
            }

            if (whitePoints.contains(point) || blackPoints.contains(point))
            {
                return  false;
            }


            //判断黑白棋回合
            if (isWhiteRound && isChess)
            {
                whitePoints.add(point);
            }
            else
            {
                blackPoints.add(point);
            }
            isChess = false;
            isWhiteRound = !isWhiteRound;
            invalidate();
            return false;

        }
        return true;
    }

    private void showDialog() {
        String str = isWin.isWqWinFlag() ? "白棋获胜" : "黑棋获胜";
        new AlertDialog.Builder(context).setMessage("恭喜" + str + "，是否再来一局？" )
                .setCancelable(false)//是否能撤销
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restart();//清空数据

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    public void restart()
    {
        whitePoints.clear();
        blackPoints.clear();
        choose =new Point(-1,-1);
        isGameOver = false; //游戏没有结束
        isWhiteRound = true; //游戏重新开始

        invalidate();//进行更新界面
    }

    public void restart(boolean b) {

        whitePoints.clear();
        blackPoints.clear();
        choose =new Point(-1,-1);
        isGameOver = false; //游戏没有结束
        isWhiteRound = b; //游戏重新开始

        invalidate();

    }
    public void setMode(boolean mode) {
        this.mode=mode;
    }

}


