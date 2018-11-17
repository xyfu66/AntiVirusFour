
package com.jinyun.antivirusfour.trafficApps.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;


public class Adapterforimage extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, Object>> item;

	public Adapterforimage(Context context,
			ArrayList<HashMap<String, Object>> item) {
		this.context = context;
		this.item = item;
	}

	@Override
	public int getCount() {
		return item.size();
	}

	@Override
	public Object getItem(int position) {
		return item.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Datalist data = new Datalist();
		convertView = LayoutInflater.from(context).inflate(R.layout.showitems,
				null);
		data.mimage = (ImageView) convertView.findViewById(R.id.showitum_image);
		data.mnametextv = (TextView) convertView
				.findViewById(R.id.showitem_appname);
		data.mrxtextv = (TextView) convertView
				.findViewById(R.id.showitem_rxdata);
		data.mtxtextv = (TextView) convertView
				.findViewById(R.id.showitem_txdata);
		data.mtotaltextv = (TextView) convertView
				.findViewById(R.id.showitem_totaldata);
		data.mimage.setImageDrawable((Drawable)item.get(position).get("appsimage"));
		data.mnametextv.setText(item.get(position).get("appsname").toString());
		data.mrxtextv.setText(item.get(position).get("rxdata").toString());
		data.mtxtextv.setText(item.get(position).get("txdata").toString());
		data.mtotaltextv.setText(item.get(position).get("alldata").toString());
		return convertView;

	}

	private class Datalist {
		public ImageView mimage;
		public TextView mnametextv, mrxtextv, mtxtextv, mtotaltextv;

	}
}
