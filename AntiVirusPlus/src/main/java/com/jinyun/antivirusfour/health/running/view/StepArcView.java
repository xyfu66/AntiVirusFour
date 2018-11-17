package com.jinyun.antivirusfour.health.running.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jinyun.antivirusfour.R;

/**
 *
 * 显示步数的圆弧
 */
public class StepArcView extends View {

    /**
     * 圆弧的宽度
     */
    private float borderWidth = dipToPx(14);
    /**
     * 画步数的数值的字体大小
     */
    private float numberTextSize = 0;
    /**
     * 步数
     */
    private String stepNumber = "0";
    /**
     * 开始绘制圆弧的角度
     */
    private float startAngle = 135;
    /**
     * 终点对应的角度和起始点对应的角度的夹角
     */
    private float angleLength = 270;
    /**
     * 所要绘制的当前步数的红色圆弧终点到起点的夹角
     */
    private float currentAngleLength = 0;
    /**
     * 动画时长
     */
    private int animationLength = 3000;

    private Bitmap mWatchBitmap;
    private float stepStringY = 0;//中间文字的Y值
    private int transparentWhite;
    private float textNumberY = 0;//圆环中心的步数的Y值
    private String mKmNum;
    private String mCalNum;
    private double km;
    private double calories;

    public StepArcView(Context context) {
        super(context);
    }

    public StepArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**中心点的x坐标*/
        float centerX = (getWidth()) / 2;
        float centerY = (getHeight()) / 2;
        /**指定圆弧的外轮廓矩形区域*/
        RectF rectF = new RectF(0 + borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);

        mWatchBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_watch);
        transparentWhite = Color.parseColor("#00ffffff");

        /**【第一步】绘制整体的黄色圆弧*/
        drawArcYellow(canvas, rectF,centerX,centerY);
        /**【第二步】绘制当前进度的红色圆弧*/
        drawArcRed(canvas, rectF);
        /**【第三步】绘制当前进度的红色数字*/
        drawTextNumber(canvas, centerX);

    }

    /**
     * 1.绘制总步数的圆弧
     *
     * @param canvas 画笔
     * @param rectF  参考的矩形
     */
    private void drawArcYellow(Canvas canvas, RectF rectF,float centerX,float centerY) {
        Paint paint = new Paint();
        /** 默认画笔颜色 */
//        paint.setColor(getResources().getColor(R.color.yellow));
        int outRadius = (int) ((0.65 + 0.65 * getMeasuredWidth() / 2));
        paint.setStrokeWidth(15);//当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.color_four));
        canvas.drawOval(rectF,paint);
//        canvas.drawCircle(centerX, centerY, outRadius, paint);

        int innerRadius = (int) (0.75 * getMeasuredWidth() / 2);
        int startRunAngle = -90, runedAngle = 270, startUnRunAngle = startRunAngle + runedAngle, unRunedAngle = 360 - runedAngle;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        RectF innerCircle = new RectF(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius);
        canvas.drawArc(innerCircle,-90,270,false,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[] {5, 5}, 0));
        canvas.drawArc(innerCircle,180,90,false,paint);

        /** 结合处为圆弧*/
        paint.setStrokeJoin(Paint.Join.ROUND);
        /** 设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形*/
        paint.setStrokeCap(Paint.Cap.ROUND);
        /** 设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE  ：填充内部和描边;  Paint.Style.STROKE  ：仅描边*/
//        paint.setStyle(Paint.Style.STROKE);
        /**抗锯齿功能*/
        paint.setAntiAlias(true);
        /**设置画笔宽度*/
//        paint.setStrokeWidth(borderWidth);

        /**绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        //canvas.drawArc(rectF, startAngle, angleLength, false, paint);

    }

    /**
     * 2.绘制当前步数的红色圆弧
     */
    private void drawArcRed(Canvas canvas, RectF rectF) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStrokeJoin(Paint.Join.ROUND);
        paintCurrent.setStrokeCap(Paint.Cap.ROUND);//圆角弧度
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(borderWidth);//设置画笔宽度
        paintCurrent.setColor(getResources().getColor(R.color.red));//设置画笔颜色
        canvas.drawArc(rectF, startAngle, currentAngleLength, false, paintCurrent);
    }

    /**
     * 3.圆环中心的步数
     */
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);//抗锯齿功能
        mPaint.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        mPaint.setTypeface(font);//字体风格
        mPaint.setColor(getResources().getColor(R.color.white));
        Rect bounds_Number = new Rect();
        mPaint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        textNumberY = getHeight() / 2 + bounds_Number.height() / 2;
        canvas.drawText(stepNumber, centerX, textNumberY, mPaint);

        /**更新所走公里数和消耗的卡路里*/
        SharedPreferences sp = getContext().getSharedPreferences("LHA", Context.MODE_PRIVATE);
        String weight = sp.getString("weight", null);
        float weight_to_float = Float.parseFloat(weight);

        km = Math.round(Integer.valueOf(stepNumber)*10 / 1333)/10.0;
        calories = Math.round(0.8214 * weight_to_float * km*10)/10.0;

        Log.i("weight_to_float",weight_to_float+"");
        Log.i("km",km+"");
        Log.i("calories",calories+"");
        Log.i("stepNumber",stepNumber+"");

        mKmNum = km + "公里";
        mCalNum = calories +"千卡";

        //绘制公里文字
        mPaint.setTextSize(28);
        mPaint.setColor(Color.parseColor("#b1d6f8"));
        Rect kmNumRect = new Rect();
        mPaint.getTextBounds(mKmNum, 0, mKmNum.length() - 1, kmNumRect);
        float kmNumBaseX = centerX - 20 - (kmNumRect.left + kmNumRect.right) / 2;
        float kmNumBaseY = textNumberY + 30 + kmNumRect.height();
        canvas.drawText(mKmNum, kmNumBaseX, kmNumBaseY, mPaint);
        //绘制卡路里
        Rect calNumRect = new Rect();
        mPaint.getTextBounds(mCalNum, 0, mCalNum.length() - 1, calNumRect);
        float calNumBaseX = centerX + 20 + (calNumRect.left + calNumRect.right) / 2;
        float calNumBaseY = textNumberY + 30 + kmNumRect.height();
        canvas.drawText(mCalNum, calNumBaseX, calNumBaseY, mPaint);
        //绘制中间线
        mPaint.setStrokeWidth(2);
        float centerLineTop = kmNumBaseY - kmNumRect.height();
        float centerLineBottom = centerLineTop + kmNumRect.height();
        canvas.drawLine(centerX, centerLineTop, centerX, centerLineBottom, mPaint);
        //绘制最底部手表
        float watchX = centerX - mWatchBitmap.getWidth() / 2;
        float watchY = centerLineBottom + 40;
        canvas.drawBitmap(mWatchBitmap, watchX, watchY, mPaint);

    }




    /**
     * 获取当前步数的数字的高度
     *
     * @param fontSize 字体大小
     * @return 字体高度
     */
    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 所走的步数进度
     *
     * @param totalStepNum  设置的步数
     * @param currentCounts 所走步数
     */
    public void setCurrentCount(int totalStepNum, int currentCounts) {
        /**如果当前走的步数超过总步数则圆弧还是270度，不能成为园*/
        if (currentCounts > totalStepNum) {
            currentCounts = totalStepNum;
        }

        /**上次所走步数占用总共步数的百分比*/
        float scalePrevious = (float) Integer.valueOf(stepNumber) / totalStepNum;
        /**换算成弧度最后要到达的角度的长度-->弧长*/
        float previousAngleLength = scalePrevious * angleLength;

        /**所走步数占用总共步数的百分比*/
        float scale = (float) currentCounts / totalStepNum;
        /**换算成弧度最后要到达的角度的长度-->弧长*/
        float currentAngleLength = scale * angleLength;
        /**开始执行动画*/
        setAnimation(previousAngleLength, currentAngleLength, animationLength);
        stepNumber = String.valueOf(currentCounts);//步数


        setTextSize(currentCounts);
    }

    /**
     * 为进度设置动画
     * ValueAnimator是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现的，
     * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
     * 它的内部使用一种时间循环的机制来计算值与值之间的动画过渡，
     * 我们只需要将初始值和结束值提供给ValueAnimator，并且告诉它动画所需运行的时长，
     * 那么ValueAnimator就会自动帮我们完成从初始值平滑地过渡到结束值这样的效果。
     *
     * @param start   初始值
     * @param current 结束值
     * @param length  动画时长
     */
    private void setAnimation(float start, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(start, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**每次在初始值和结束值之间产生的一个平滑过渡的值，逐步去更新进度*/
                currentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * 设置文本大小,防止步数特别大之后放不下，将字体大小动态设置
     *
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dipToPx(50);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dipToPx(40);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dipToPx(30);
        } else if (length > 8) {
            numberTextSize = dipToPx(25);
        }
    }

}

