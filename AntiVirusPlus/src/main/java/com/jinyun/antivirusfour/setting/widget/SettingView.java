package com.jinyun.antivirusfour.setting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jinyun.antivirusfour.R;


public class SettingView extends RelativeLayout {

	private String setTitle ="";
	private String status_on ="";
	private String status_off ="";
	private TextView mSettingTitleTV;
	private TextView mSettingStatusTV;
	private ToggleButton mToggleBtn;
	private boolean isChecked;
	private OnCheckedStatusIsChanged onCheckedStatusIsChanged;

	public SettingView(Context context) {
		super(context);
		init(context);
	}

	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//拿到属性对象的值
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.SettingView);
		setTitle = mTypedArray.getString(R.styleable.SettingView_settitle);
		status_on = mTypedArray.getString(R.styleable.SettingView_status_on);
		status_off = mTypedArray.getString(R.styleable.SettingView_status_off);
		isChecked = mTypedArray.getBoolean(R.styleable.SettingView_status_ischecked,false);
		mTypedArray.recycle();
		init(context);
		setStatus(status_on, status_off, isChecked);
	}

	/***
	 * 初始化控件
	 * @param context
	 */
	private void init(Context context) {
		View view  = View.inflate(context, R.layout.ui_settings_home_view, null);
		this.addView(view);
		mSettingTitleTV = (TextView) findViewById(R.id.tv_setting_title);
		mSettingStatusTV = (TextView) findViewById(R.id.tv_setting_status);
		mToggleBtn = (ToggleButton) findViewById(R.id.toggle_setting_status);
		mSettingTitleTV.setText(setTitle);
		mToggleBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setChecked(isChecked);
				onCheckedStatusIsChanged.onCheckedChanged(SettingView.this, isChecked);
			}
		});
	}

	/***
	 * 返回组合控件是否选中
	 * @return
	 */
	public boolean isChecked() {
		return mToggleBtn.isChecked();
	}

	/**
	 * 设置组合控件的选中方式
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		mToggleBtn.setChecked(checked);
		isChecked = checked;
		if (checked) {
			if (!TextUtils.isEmpty(status_on)) {
				mSettingStatusTV.setText(status_on);
			}
		} else {
			if (!TextUtils.isEmpty(status_off)) {
				mSettingStatusTV.setText(status_off);
			}
		}
	}

	/**
	 * 设置自定义控件的描述
	 */
	public void setStatus(String status_on, String status_off, boolean checked) {
		if (checked) {
			mSettingStatusTV.setText(status_on);
		} else {
			mSettingStatusTV.setText(status_off);
		}
		mToggleBtn.setChecked(checked);
	}

	/**
	 * 设置状态变化监听
	 */
	public void setOnCheckedStatusIsChanged(OnCheckedStatusIsChanged onCheckedStatusIsChanged){
		this.onCheckedStatusIsChanged = onCheckedStatusIsChanged;
	}

	/***
	 * 回调接口
	 * @author admin
	 */
	public interface OnCheckedStatusIsChanged{
		/***
		 * 当前View的check状态发生变化时会调用此方法
		 * @param view
		 * @param isChecked
		 */
		void onCheckedChanged(View view,boolean isChecked);
	}
}
