package com.jinyun.antivirusfour.antitheft;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jinyun.antivirusfour.R;


public class LostFindActivity extends Activity implements OnClickListener{

	private TextView mSafePhoneTV;
	private RelativeLayout mInterSetupRL;
	private SharedPreferences msharedPreferences;
	private ToggleButton mToggleButton;
	private TextView mProtectStatusTV;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lostfind);
		msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		if(!isSetUp()){
			//如果没有进入过设置向导，则进入
			startSetUp1Activity();
		}
		initView();
	}

	private boolean isSetUp() {
		return msharedPreferences.getBoolean("isSetUp", false);
	}

	/**初始化控件*/
	private void initView() {
		TextView mTitleTV = (TextView) findViewById(R.id.tv_title);
		mTitleTV.setText("手机防盗");
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.returns_arrow02);
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.purple));
		mSafePhoneTV = (TextView) findViewById(R.id.tv_safephone);
		mSafePhoneTV.setText(msharedPreferences.getString("safephone", ""));
		mToggleButton = (ToggleButton) findViewById(R.id.togglebtn_lostfind);
		mInterSetupRL = (RelativeLayout) findViewById(R.id.rl_inter_setup_wizard);
		mInterSetupRL.setOnClickListener(this);
		mProtectStatusTV = (TextView) findViewById(R.id.tv_lostfind_protectstauts);
		//查询手机防盗是否开启，默认为开启
		boolean protecting = msharedPreferences.getBoolean("protecting", true);
		if(protecting){
			mProtectStatusTV.setText("防盗保护已经开启");
			mToggleButton.setChecked(true);
		}else{
			mProtectStatusTV.setText("防盗保护没有开启");
			mToggleButton.setChecked(false);
		}
		mToggleButton.setOnCheckedChangeListener( new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					mProtectStatusTV.setText("防盗保护已经开启");
				}else{
					mProtectStatusTV.setText("防盗保护没有开启");
				}
				Editor editor = msharedPreferences.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_inter_setup_wizard:
				//重新进入设置向导
				startSetUp1Activity();
				break;
			case R.id.imgv_leftbtn:
				//重新进入设置向导
				finish();
				break;
		}
	}

	private void startSetUp1Activity() {
		Intent intent =new Intent(LostFindActivity.this,SetUp1Activity.class);
		startActivity(intent);
		finish();
	}
}
