package com.jinyun.antivirusfour.health.filpboard;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jinyun.antivirusfour.R;


public class FlipboardTwoLayout extends RelativeLayout {
    FlipboardTwoView view_two;
    Button animateBt_two;

    public FlipboardTwoLayout(Context context) {
        super(context);
    }

    public FlipboardTwoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipboardTwoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view_two = (FlipboardTwoView) findViewById(R.id.objectAnimatorView_two);
        animateBt_two = (Button) findViewById(R.id.animateBt_two);

        animateBt_two.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view_two.clearCanvas();

                ObjectAnimator animator1 = ObjectAnimator.ofInt(view_two, "turnoverDegreeFirst", 0, 30);
                animator1.setDuration(1000);
                animator1.setInterpolator(new LinearInterpolator());

                ObjectAnimator animator2 = ObjectAnimator.ofInt(view_two, "degree", 0, 270);
                animator2.setDuration(2000);
                animator2.setInterpolator(new AccelerateDecelerateInterpolator());

                ObjectAnimator animator3 = ObjectAnimator.ofInt(view_two, "turnoverDegreeLast", 0, 30);
                animator3.setDuration(1000);
                animator3.setInterpolator(new LinearInterpolator());

                AnimatorSet animatorSet = new AnimatorSet();
                // 两个动画依次执行
                animatorSet.playSequentially(animator1, animator2, animator3);
                animatorSet.start();
            }
        });
    }
}
