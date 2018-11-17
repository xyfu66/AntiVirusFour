package com.jinyun.antivirusfour.advanced.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.software.utils.DensityUtil;


/**
 * 自定义控件 带进度的按钮
 * */
public class MyCircleProgress extends android.support.v7.widget.AppCompatButton {
    private Paint paint;
    /** 进度 */
    private int progress;
    private int max;
    /** 进度为零时，颜色 */
    private int mCircleColor;
    private int mProgressColor;
    private float roundWidth;
    private int mProgressTextSize;
    private Context context;
    private float mDistanceOFbg;
    public MyCircleProgress(Context context){
        this(context,null);
    }
    public MyCircleProgress(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }
    public MyCircleProgress(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init(context,attrs);
    }
    /**
     * 初始化控件
     * @param context
     */
    private void init(Context context,AttributeSet attrs){
        paint=new Paint();
        this.context=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.MyCircleProgress);
        progress=typedArray.getInteger(R.styleable.MyCircleProgress_progress,0);
        max=typedArray.getInteger(R.styleable.MyCircleProgress_max,100);
        mCircleColor=typedArray.getColor(R.styleable.MyCircleProgress_circleColor,Color.RED);
        mProgressColor=typedArray.getColor(R.styleable.MyCircleProgress_progressColor,Color.WHITE);
        roundWidth= DensityUtil.dip2px(context,5);
        mDistanceOFbg=DensityUtil.dip2px(context,5);
        mProgressTextSize=DensityUtil.dip2px(context,16);
        typedArray.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //步骤1：计算出圆心位置
        int centerX=getWidth()/2;
        int centerY=getHeight()/2;
        int radius=(int) (centerX-mDistanceOFbg);
        //步骤2：画出外层圆圈
        paint.setColor(mCircleColor);                   //设置颜色
        paint.setAntiAlias(true);                       //给Paint加上锯齿
        paint.setStyle(Paint.Style.STROKE);             //设置填充样式，仅描边
        paint.setStrokeWidth(roundWidth);               //设置画笔宽度
        canvas.drawCircle(centerX,centerY,radius,paint);//画圈
        //步骤3：画出外层进度条
        paint.setColor(mProgressColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        //用于定义的圆弧的形状和大小的界限
        RectF oval=new RectF(centerX-radius,centerY-radius,centerX+radius,centerY+radius);
        paint.setStyle(Paint.Style.STROKE);
        //旋转后，图片的抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        //参数1：圆弧起始角度，单位为度；参数2：圆弧扫过的角度，顺时针方向，单位为度；
        //参数3：如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形；参数4：画笔
        canvas.drawArc(oval,0,360*progress/max,false,paint);
        //步骤4：展示进度文字
        paint.setStrokeWidth(0);
        paint.setColor(mProgressColor);
        paint.setTextSize(mProgressTextSize);
        //设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        int percent=(int)(((float)progress/(float)max)*100);
        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth=paint.measureText(percent+"%");
        if(percent>0){
            //画出进度百分比
            canvas.drawText(percent+"%",centerX-textWidth/2,(float)(centerY + radius-mDistanceOFbg*6),paint);
        }
    }
    /** 设置进度，此为线程安全控件，由于考虑多线程的问题，需要线程同步 */
    public synchronized void setProcess(int process){
        if(process<0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(process>max){
            process=max;
        }
        if(process<=max){
            this.progress=process;
            //重绘图片
            postInvalidate();
        }
    }
    public synchronized void setMax(int max){
        if(max<0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max=max;
    }
}
