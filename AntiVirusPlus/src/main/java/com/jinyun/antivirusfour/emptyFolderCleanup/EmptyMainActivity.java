package com.jinyun.antivirusfour.emptyFolderCleanup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmptyMainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 99;
    private static final int CODE_UPDE_PROGRESS = 307;
    private static final int CODE_SEARCH_OVER = 116;
    /**
     * 所有的空文件夹
     */
    private List<DataBean> emptyFiles;
    /**
     * 已选择的空文件夹
     */
    private List<File> selectFiles;
    private String path = Environment.getExternalStorageDirectory()+ File.separator;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    /**
     * 总文件夹和已选文件夹
     */
    private MenuItem itemSelect;

    /**
     * 手机内存情况
     */
    private TextView textView;

    private ProgressDialog progressDialog;

    private long mExitTime;

    /**
     * 对话框
     */
    private android.support.v7.app.AlertDialog.Builder builder;

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case CODE_UPDE_PROGRESS:

                    break;
                case CODE_SEARCH_OVER:
                    if (progressDialog.isShowing())
                    {
                        progressDialog.cancel();
                    }
                    break;
            }
            recyclerViewAdapter.notifyDataSetChanged();
            if(itemSelect!=null)
            {
                itemSelect.setTitle(selectFiles.size()+"/"+emptyFiles.size());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_main);
        emptyFiles = new ArrayList<>();
        selectFiles = new ArrayList<>();

        textView = (TextView) findViewById(R.id.activity_textview);

        try {
            textView.setText("总空间："+getTotalInternalMemorySize()/(1024*1024) + "M  剩余空间："+getAvailableInternalMemorySize()/(1024*1024)+"M");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            textView.setText("获取空间失败");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new RecyclerViewAdapter(this,emptyFiles);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnItemclickListener(new RecyclerViewAdapter.OnItemclickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                DataBean dataBean = emptyFiles.get(position);
                dataBean.setSelected(!dataBean.isSelected());
                if(dataBean.isSelected())
                {
                    selectFiles.add(dataBean.getFile());
                }
                else
                {
                    selectFiles.remove(dataBean.getFile());
                }
                recyclerViewAdapter.notifyItemChanged(position);
                itemSelect.setTitle(selectFiles.size()+"/"+emptyFiles.size());
            }
        });

        //判断app读写权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.app_name)+"需要存储权限来帮助您在线更新，请您在下一步授权。");
            builder.setCancelable(false);
            builder.setTitle("温馨提示");
            builder.setNegativeButton("下一步", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(EmptyMainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            });
            builder.create().show();
        }
        else{
            initProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getFiles(emptyFiles,path);
                    handler.sendEmptyMessage(CODE_SEARCH_OVER);
                }
            }).start();
        }

    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在搜索，请稍后……");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        itemSelect=menu.add(0,0,0,selectFiles.size()+"/"+emptyFiles.size());
        /**
         * setShowAsAction参数说明   MenuItem接口的一些常量
         * SHOW_AS_ACTION_ALWAYS   总是显示这个项目作为一个操作栏按钮。
         * SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW   此产品的动作视图折叠成一个正常的菜单项。
         * SHOW_AS_ACTION_IF_ROOM   显示此项目作为一个操作栏的按钮,如果系统有空间。
         * SHOW_AS_ACTION_NEVER     从不显示该项目作为一个操作栏按钮。
         * SHOW_AS_ACTION_WITH_TEXT  当这个项目是在操作栏中,始终以一个文本标签显示它,即使它也有指定一个图标。
         */
        itemSelect.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);//主要是这句话

//        item.setOnMenuItemClickListener(listener);//添加监听事件
//        item.setIcon(R.drawable.ic_menu_confirm);//设置图标
        //找到ActionBar上所添加的UI组件的方法:
        return true;
    }


    /**
     * 申请权限的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getFiles(emptyFiles,path);
                            handler.sendEmptyMessage(CODE_SEARCH_OVER);
                        }
                    }).start();

                } else {
                    //退出应用
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.menu_research:
                initProgressDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        emptyFiles.clear();
                        selectFiles.clear();
                        getFiles(emptyFiles,path);
                        handler.sendEmptyMessage(CODE_SEARCH_OVER);
                    }
                }).start();

                break;
            case R.id.menu_selectall:
                selectFiles.clear();
                for (DataBean dataBean:emptyFiles) {
                    dataBean.setSelected(true);
                    selectFiles.add(dataBean.getFile());
                }
                recyclerViewAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_disselect:
                for (DataBean dataBean:emptyFiles) {
                    dataBean.setSelected(false);
                    selectFiles.clear();
                }
                recyclerViewAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_delete:
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("正在删除，请稍后……");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(selectFiles.size());
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <emptyFiles.size() ; i++) {
                            DataBean dataBean = emptyFiles.get(i);
                            if (dataBean.isSelected())
                            {
                                dataBean.setSelected(false);
                                emptyFiles.remove(dataBean);
                            }
                        }
                        for (File file:selectFiles) {
                            file.delete();
                            progressDialog.setProgress(selectFiles.size());
                        }
                        selectFiles.clear();
                        handler.sendEmptyMessage(CODE_SEARCH_OVER);
                    }
                }).start();
                break;
            case R.id.menu_about:
                Intent intent = new Intent(EmptyMainActivity.this,EmptyAboutActivity.class);
                startActivity(intent);
                break;
        }
        itemSelect.setTitle(selectFiles.size()+"/"+emptyFiles.size());
        return super.onOptionsItemSelected(item);
    }

    /**
     * 遍历文件夹（查找空文件夹）
     * @param fileList
     * @param path
     */
    private void getFiles(List<DataBean> fileList, String path) {
        File[] allFiles = new File(path).listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if(file.isDirectory())
            {
                if (file.listFiles().length==0) {
                    DataBean dataBean = new DataBean();
                    dataBean.setFile(file);
                    fileList.add(dataBean);
                } else if (!file.getAbsolutePath().contains(".thumnail")) {
                    getFiles(fileList, file.getAbsolutePath());
                    handler.sendEmptyMessage(CODE_UPDE_PROGRESS);
                }
            }
        }
    }

    /**
     * 获取手机内部剩余存储空间
     *
     * @return
     */
    public long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 按两次返回键退出程序
     * @param keyCode
     * @param event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime)<2000){
                System.exit(0);
            }else{
                Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
