package com.canadian.study.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.canadian.study.R;

/**
 * Created by zheng on 16/9/12.
 */

public class QuickSearchBar extends ImageView {

    private char mTargetChar = '#';
    private char[] mChars; // 被放大的字符
    private int mTextColor; // QuickSearchBar上字体的颜色
    private int mTextSize; // QuickSearchBar上字体的大小
    private float mTextHeight;
    private float mTextMaxWidth;
    private float[] mYs;
    private boolean mStartMove = false; // 是否开始滑动QuickSearchBar

    private Paint mPaint;
    private Drawable mBackground;
    private OnClickListener mOnClickListener;

    public QuickSearchBar(Context context) {
        this(context, null);
    }

    public QuickSearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSearchBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.QuickSearchBar, defStyle, 0);

        String charText = array.getString(R.styleable.QuickSearchBar_android_text);
        mChars = (null == charText ? new char[0] : charText.toCharArray());
        mYs = new float[mChars.length];
        mTextColor = array.getColor(R.styleable.QuickSearchBar_android_textColor, Color.BLUE);
        mTextSize = array.getDimensionPixelSize(R.styleable.QuickSearchBar_android_textSize, 5);

        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        //  mPaint.setTypeface(Typeface.DEFAULT_BOLD); // 粗体
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG); // 消除锯齿

        // FontMetrics对象包含了一个给定字符的坐标，大小等相关信息
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        mTextHeight = metrics.bottom - metrics.top;

        int charsLength = mChars.length;
        for (int i = 0; i < charsLength; i++) {
            float width = mPaint.measureText(mChars, i, 1); // 放大字符的宽度
            if (width > mTextMaxWidth) {
                mTextMaxWidth = width;
            }
        }

        mBackground = ContextCompat.getDrawable(context, R.mipmap.quick_bar_background);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTextHeight = (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - 6)
                / ((float) mChars.length);
        mPaint.setTextSize(mTextHeight - 2);

        float y = this.getPaddingTop() + mTextHeight - mPaint.getFontMetrics().leading;
        for (int i = 0; i < mChars.length; i++) {
            if (y > (this.getHeight() - this.getPaddingBottom())) {
                break;
            }
            float width = mPaint.measureText(mChars, i, 1);
            float x = this.getPaddingLeft() + (mTextMaxWidth - width + 8) / 2;
            mYs[i] = y;

            if (mStartMove && mChars[i] == mTargetChar) {
              /*  Bitmap bitmap = ((BitmapDrawable) (getContext().getResources()
                        .getDrawable(R.drawable.quick_search_bar_track))).getBitmap();
                float trackX = x + width / 2f - bitmap.getWidth() / 2f;
                float trackY = y - mTextHeight / 2f - bitmap.getHeight() / 2f;
                canvas.drawBitmap(bitmap, trackX + 10, trackY, null);
*/
                mPaint.setColor(Color.WHITE);
                canvas.drawText(mChars, i, 1, x + 10, y, mPaint);
                mPaint.setColor(mTextColor);
            } else {
                canvas.drawText(mChars, i, 1, x + 10, y, mPaint);
            }

            y += mTextHeight;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                mStartMove = true;
                moveTo(event.getY());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mStartMove = false;
                setImageDrawable(null);
                break;
            case MotionEvent.ACTION_DOWN:
                mStartMove = true;
                if (null != mBackground){
                    setImageDrawable(mBackground);
                }
                break;
            default:
                return true;
        }

        moveTo(event.getY());
        invalidate();
        return true;
    }

    private void moveTo(float y) {
        int pos = getClickPosition(y);
        if (pos < 0 || pos >= mChars.length || null == mOnClickListener) {
            return;
        }
        mTargetChar = mChars[pos];
        mOnClickListener.onClick(mTargetChar, pos);
    }

    private int getClickPosition(float y) {
        int res = -1;
        for (int i = 0; i < mYs.length; i++) {
            float previousY = i == 0 ? (mYs[0] - mTextHeight) : mYs[i - 1];
            if (y > previousY && y <= mYs[i]) {
                res = i;
                break;
            }
        }
        return res;
    }

    public interface OnClickListener {
        public void onClick(char c, int position);
    }

    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

}