
package com.jinyun.antivirusfour.trafficApps.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.trafficApps.database.Adapterforimage;


public class Appsdata extends Activity implements View.OnClickListener{
	private ListView showListview;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appsdatamain);

        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.lightBlue));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("APP流量监控");

		showListview = (ListView) findViewById(R.id.apps_data_listview);
		show_data_onlistviw();
	}

	private void show_data_onlistviw() {
		PackageManager pckMan = getPackageManager();
		List<PackageInfo> packs = pckMan.getInstalledPackages(0);
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
		for (PackageInfo p:packs) {
			if((p.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0&&  
       (p.applicationInfo.flags&ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)==0){ 		
			int appid = p.applicationInfo.uid;
			long rxdata = TrafficStats.getUidRxBytes(appid);
			rxdata = rxdata / 1024 ;   
			long txdata = TrafficStats.getUidTxBytes(appid);
			txdata = txdata / 1024 ;
			long data_total = rxdata + txdata;
			HashMap<String, Object> items = new HashMap<String, Object>();
			Drawable drawable=p.applicationInfo.loadIcon(getPackageManager());
			Log.i("TAG", ""+drawable);
			items.put("appsimage",p.applicationInfo.loadIcon(getPackageManager()));
			items.put("appsname", p.applicationInfo.loadLabel(getPackageManager()).toString());
			items.put("rxdata", rxdata + "");
			items.put("txdata", txdata + "");
			items.put("alldata", data_total + "");
			item.add(items);
			}
		}
		Adapterforimage adapter=new Adapterforimage(this, item);
		
		showListview.setAdapter(adapter);
	}

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
