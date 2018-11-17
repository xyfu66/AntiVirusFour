package com.jinyun.antivirusfour.antitheft;

import android.Manifest;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;


public class SetUp2Activity extends BaseSetUpActivity implements
		OnClickListener {

	private TelephonyManager mTelephonyManager;
	private Button mBindSIMBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		initView();
	}

	private void initView() {
		// 设置第二个小圆点的颜色
		((RadioButton) findViewById(R.id.rb_second)).setChecked(true);
		mBindSIMBtn = (Button) findViewById(R.id.btn_bind_sim);
		mBindSIMBtn.setOnClickListener(this);
		if (isBind()) {
			mBindSIMBtn.setEnabled(false);
		} else {
			mBindSIMBtn.setEnabled(true);
		}
	}

	private boolean isBind() {
		String simString = sp.getString("sim", null);
		if (TextUtils.isEmpty(simString)) {
			return false;
		}
		return true;
	}

	@Override
	public void showNext() {
		if (!isBind()) {
			Toast.makeText(this, "您还没有帮定SIM卡！", Toast.LENGTH_SHORT).show();
			return;
		}
		startActivityAndFinishSelf(SetUp3Activity.class);
	}

	@Override
	public void showPre() {
		startActivityAndFinishSelf(SetUp1Activity.class);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_bind_sim:
				// 绑定SIM卡
				bindSIM();
				break;
		}
	}

	/**
	 * 绑定sim卡
	 */
	private void bindSIM() {
		if (!isBind()) {
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			String simSerialNumber = mTelephonyManager.getSimSerialNumber();
			Editor edit = sp.edit();
			edit.putString("sim", simSerialNumber);
			edit.commit();
			Toast.makeText(this, "SIM卡绑定成功！", Toast.LENGTH_SHORT).show();
			mBindSIMBtn.setEnabled(false);
		} else {
			// 已经绑定，提醒用户
			Toast.makeText(this, "SIM卡已经绑定！", Toast.LENGTH_SHORT).show();
			mBindSIMBtn.setEnabled(false);
		}
	}
}
