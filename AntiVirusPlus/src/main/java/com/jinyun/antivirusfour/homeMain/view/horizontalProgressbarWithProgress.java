package com.jinyun.antivirusfour.homeMain.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.jinyun.antivirusfour.R;


public class horizontalProgressbarWithProgress extends ProgressBar {

    private static final int DEFAULT_TEXT_SIZE = 10;//dp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_COLOR_UNREACH = 0XFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;//dp
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp

    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnReachColor = DEFAULT_COLOR_UNREACH;
    private int mUnReachHeight  = dp2px(DEFAULT_HEIGHT_UNREACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();//画笔

    private  int mRealWidth;

    public horizontalProgressbarWithProgress(Context context) {
        this(context,null);
    }

    public horizontalProgressbarWithProgress(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public horizontalProgressbarWithProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        obtainStyledAttrs(attrs);

    }

    //获取自定义属性
    private void obtainStyledAttrs(AttributeSet attrs) {
        //创建TypedArray获取旗下资源
        TypedArray ta = getContext().obtainStyledAttributes(attrs,
                R.styleable.horizontalProgressbarWithProgress);

        mTextSize=(int)ta
                .getDimension(
                        R.styleable.horizontalProgressbarWithProgress_progress_text_size,mTextSize);
        mTextColor=ta
                .getColor(
                        R.styleable.horizontalProgressbarWithProgress_progress_text_color,mTextColor);
        mUnReachColor=ta
                .getColor(
                        R.styleable.horizontalProgressbarWithProgress_progress_unreach_color,mUnReachColor);
        mUnReachHeight=(int)ta
                .getDimension(
                        R.styleable.horizontalProgressbarWithProgress_progress_unreach_height,mUnReachHeight);
        mReachColor=ta
                .getColor(
                        R.styleable.horizontalProgressbarWithProgress_progress_reach_color,mReachColor);
        mReachHeight=(int)ta
                .getDimension(
                        R.styleable.horizontalProgressbarWithProgress_progress_reach_height,mReachHeight);
        mTextOffset=(int)ta
                .getDimension(
                        R.styleable.horizontalProgressbarWithProgress_progress_text_offset,mTextOffset);


        ta.recycle();

        mPaint.setTextSize(mTextSize); //设置字体的一个size

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   //自定义View的外观形象
        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);//模式 (水平进度条不要宽度)
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);//得到值

        int height = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthVal,height);//调用

        //实际上绘制区域的一个宽度
        mRealWidth = getMeasuredWidth()- getPaddingLeft() - getPaddingRight();

    }

    private int measureHeight(int heightMeasureSpec) {

        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if(mode == MeasureSpec.EXACTLY){  //exactly精确值
            result = size;

        }else {  //自己测量值
            int textHeight = (int) (mPaint.descent()-mPaint.ascent());//得到文字的高度
            //获取上下边距，在三者中取最大值
            result = getPaddingTop()
                    +getPaddingBottom()
                    + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));
            //不能超过size
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }

        }

        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() ,getHeight()/2);//放在画布中间

        boolean noNeedUnReach = false;

        //draw reach bar
        int j =getProgress()*100;
        int k = j/getMax();//算法，得到百分比
        String text = String.valueOf(k)+"%";

        int textWidth = (int) mPaint.measureText(text);

        float radio = getProgress()*1.0f/getMax();//转换为浮点
        float progressX = radio*mRealWidth; //最终显示的进度条
        if(progressX+textWidth>mRealWidth){
            progressX = mRealWidth-textWidth;
            noNeedUnReach = true;
        }

        float endX = progressX - mTextOffset/2;//进度条减去文本框真正的进度
        if(endX > 0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);//边宽
            canvas.drawLine(0,0,endX,0,mPaint);

        }

        //draw text
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent())/2);//获取高度
        canvas.drawText(text,progressX,y,mPaint);

        //draw unreach bar
        if(!noNeedUnReach){
            float start = progressX + mTextOffset/2 + textWidth;//得到高度
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);

        }

        canvas.restore();

    }

    //两个辅助方法
    private int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,
                getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spVal,
                getResources().getDisplayMetrics());
    }

}
