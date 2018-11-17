package com.jinyun.antivirusfour.traffic;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.traffic.utils.AppTrafficModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 各个APP的详细流量
 */

public class AppTrafficActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = AppTrafficActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter = null;

    private List<AppTrafficModel> listApps = new ArrayList<AppTrafficModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_app);

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainAdapter = new MainAdapter();
        recyclerView.setAdapter(mainAdapter);

        trafficMonitor();
        mainAdapter.notifyDataSetChanged();


        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);
        ((TextView) findViewById(R.id.tv_title)).setText("APP流量监控");

    }

    /**
     * 遍历有联网权限的应用程序的流量记录
     */
    private void trafficMonitor() {
        int uid;
        long rx,tx;

        PackageManager pm = this.getPackageManager();
        List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        // System.out.println(info.packageName+"访问网络");
                        uid = info.applicationInfo.uid;
                        rx = TrafficStats.getUidRxBytes(uid);
                        tx = TrafficStats.getUidTxBytes(uid);

                        AppTrafficModel appTrafficModel = new AppTrafficModel();
                        appTrafficModel.setAppInfo(info.applicationInfo);
                        appTrafficModel.setDownload(rx);
                        appTrafficModel.setUpload(tx);
                        listApps.add(appTrafficModel);

//                        /** 获取手机通过 2G/3G 接收的字节流量总数 */
//                        TrafficStats.getMobileRxBytes();
//                        /** 获取手机通过 2G/3G 接收的数据包总数 */
//                        TrafficStats.getMobileRxPackets();
//                        /** 获取手机通过 2G/3G 发出的字节流量总数 */
//                        TrafficStats.getMobileTxBytes();
//                        /** 获取手机通过 2G/3G 发出的数据包总数 */
//                        TrafficStats.getMobileTxPackets();
//                        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
//                        TrafficStats.getTotalRxBytes();
//                        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
//                        TrafficStats.getTotalRxPackets();
//                        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
//                        TrafficStats.getTotalTxBytes();
//                        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
//                        TrafficStats.getTotalTxPackets();
//                        /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
//                        TrafficStats.getUidRxBytes(uid);
//                        /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
//                        TrafficStats.getUidTxBytes(uid);

                    }
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }

    class MainAdapter extends RecyclerView.Adapter{
        PackageManager pm = AppTrafficActivity.this.getPackageManager();
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AppTrafficActivity.this).inflate(R.layout.item_traffic_app, parent, false);
            return new MainHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            AppTrafficModel trafficModel = listApps.get(position);
            MainHolder mainHolder = (MainHolder) holder;
            mainHolder.ivLauncher.setImageDrawable(trafficModel.getAppInfo().loadIcon(pm));
            mainHolder.tvName.setText((String) pm.getApplicationLabel(trafficModel.getAppInfo()));
            mainHolder.tvDownload.setText("下行：" + Formatter.formatFileSize(AppTrafficActivity.this, trafficModel.getDownload()));
            mainHolder.tvUpload.setText("上行：" + Formatter.formatFileSize(AppTrafficActivity.this, trafficModel.getUpload()));
        }

        @Override
        public int getItemCount() {
            return listApps.size();
        }

        class MainHolder extends RecyclerView.ViewHolder{

            ImageView ivLauncher;
            TextView tvName;
            TextView tvDownload;
            TextView tvUpload;

            public MainHolder(View itemView) {
                super(itemView);
                ivLauncher = (ImageView) itemView.findViewById(R.id.iv_main_item_laucher);
                tvName = (TextView) itemView.findViewById(R.id.tv_main_item_name);
                tvDownload = (TextView) itemView.findViewById(R.id.tv_main_item_download);
                tvUpload = (TextView) itemView.findViewById(R.id.tv_main_item_upload);
            }
        }
    }
}
