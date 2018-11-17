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


public class FlipboardThreeLayout extends RelativeLayout {
    FlipboardThreeView view_three;
    Button animateBt_three;

    public FlipboardThreeLayout(Context context) {
        super(context);
    }

    public FlipboardThreeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlipboardThreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view_three = (FlipboardThreeView) findViewById(R.id.objectAnimatorView_three);
        animateBt_three = (Button) findViewById(R.id.animateBt_three);

        animateBt_three.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view_three.clearCanvas();

                ObjectAnimator animator1 = ObjectAnimator.ofInt(view_three, "turnoverDegreeFirst", 0, 30);
                animator1.setDuration(1000);
                animator1.setInterpolator(new LinearInterpolator());

                ObjectAnimator animator2 = ObjectAnimator.ofInt(view_three, "degree", 0, 270);
                animator2.setDuration(2000);
                animator2.setInterpolator(new AccelerateDecelerateInterpolator());

                ObjectAnimator animator3 = ObjectAnimator.ofInt(view_three, "turnoverDegreeLast", 0, 30);
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
