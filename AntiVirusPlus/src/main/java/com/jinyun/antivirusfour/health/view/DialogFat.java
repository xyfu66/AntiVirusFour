package com.jinyun.antivirusfour.health.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.jinyun.antivirusfour.R;

/**
 * 点击右边的按钮弹出的帮助
 */

public class DialogFat extends Dialog {

    private Button btn_dialog_fat_yes;
    /**
     * 通过主题样式来控制标题栏
     * @param context
     * @param theme
     */
    public DialogFat(Context context, int theme) {
        super(context, theme);
        //加载布局文件
        this.setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_fat, null));
        btn_dialog_fat_yes = (Button) findViewById(R.id.btn_dialog_fat_yes);
        //设置按钮被点击后，结束
        btn_dialog_fat_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
