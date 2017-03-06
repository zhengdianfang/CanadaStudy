package com.canadian.study.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.canadian.study.R;

/**
 * Created by zheng on 16/9/12.
 */

public class CircleImageView extends View {

    private int count = 0;
    private Context context;
    private Paint mPaint;
    private int index = 0;
    private int a;

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initPaint();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initPaint();
    }

    private void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        a = 40;
    }

    public void setCount(int count, int index){
        this.count = count;
        this.index = index;
        invalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        int heigth = getHeight()/2;

        //绘制内圆


        if (count > 1){
            for (int i=0; i<count;i++){



                if (index == i){
                    mPaint.setColor(ContextCompat.getColor(context, R.color.project_e00303));
                }else {
                    mPaint.setColor(Color.WHITE);
                }
                canvas.drawCircle(getStartX(count) + a*i,heigth ,heigth,mPaint);

            }
        }
    }

    private int getStartX(int count ){
        return (getWidth() - (a *( count - 1) + getHeight() * count))/2;
    }
}
