package com.jinyun.antivirusfour.health.lesson.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.health.lesson.fragment.FoundFragment;
import com.jinyun.antivirusfour.health.lesson.utils.MyDialogHandler;
import com.jinyun.antivirusfour.health.lesson.view.base.BaseActivity;
import com.sangcomz.fishbun.define.Define;


import java.io.File;
import java.util.ArrayList;

/**
 * 添加课程笔记页面
 */


public class ReleaseNewsActivity extends BaseActivity implements View.OnClickListener {

    private String TITLE_NAME = "我的课程笔记";
    private View title_back;
    private TextView titleText;

    private Context mContext;

    private EditText title;
    private EditText content;
    private ImageView photo;
    private Button release;

    private ArrayList<Uri> path;

    private File imageFile;
    private static final int REQUEST_CODE_IMAGE_OP = 111;

    private String titleStr;
    private String contentStr;
    private String img_path;

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_add_news);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        this.title_back = $(R.id.title_back);
        this.titleText = $(R.id.titleText);
        this.title = $(R.id.add_news_et_share_title);
        this.content = $(R.id.add_news_et_share_content);
        this.photo = $(R.id.add_news_iv_photo);
        this.release = $(R.id.add_news_btn_release_ok);
    }

    @Override
    protected void initView() {
        mContext = this;
        this.titleText.setText(TITLE_NAME);

        this.title_back.setOnClickListener(this);
        this.photo.setOnClickListener(this);
        this.release.setOnClickListener(this);
        uiFlusHandler = new MyDialogHandler(mContext, "来吧...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back: {
                this.finish();
            }
            break;
            case R.id.add_news_iv_photo:
                getPhoto();
                break;
            case R.id.add_news_btn_release_ok://确定
                checkInfo();
                break;
        }
    }

    /**
     * 确定按钮点击事件
     */
    private void checkInfo() {
        titleStr = title.getText().toString();
        contentStr = content.getText().toString();
        if (TextUtils.isEmpty(titleStr)) {
            DisplayToast("请输入一个标题");
            title.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contentStr)) {
            DisplayToast("请输入笔记内容");
            content.requestFocus();
            return;
        }
        releaseNews();
    }

    private void releaseNews() {

        //消除对话框
        uiFlusHandler.sendEmptyMessage(DISMISS_LOADING_DIALOG);
        String url;

        if (imageFile != null && imageFile.exists()) {//如果有图片
            myDismissToMiss("success",1);

        } else {//没有图片
            myDismissToMiss("false",1);
        }
    }

    private void getPhoto() {
        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
        getImageByalbum.setType("image/*");
        getImageByalbum.setAction(Intent.ACTION_PICK);//重点
        startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);

    }

    /**
     * 对返回的数值进行处理
     * @param requestCode
     * @param resultCode
     * @param imageData
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Uri uri = path.get(0);
                    photo.setImageURI(uri);
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    actualimagecursor.moveToFirst();
                    img_path = actualimagecursor.getString(actual_image_column_index);
                    imageFile = new File(img_path);

                    //You can get image path(ArrayList<Uri>) Version 0.6.2 or later
                    break;
                }
                break;
            case REQUEST_CODE_IMAGE_OP :
                if(resultCode==RESULT_OK){
                    path = imageData.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Cursor actualimagecursor =null;
                    Uri uri = imageData.getData();
                    photo.setImageURI(uri);
                    try {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        if (uri != null) {
                            actualimagecursor = mContext.getContentResolver().query(uri, proj, null, null, null);
                            int actual_image_column_index = 0;
                            if (actualimagecursor != null) {
                                actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                                actualimagecursor.moveToFirst();// 将光标移至开头 ，这个很重要，不小心很容易引起越界
                                img_path = actualimagecursor.getString(actual_image_column_index);
                                imageFile = new File(img_path);// 路径 /storage/emulated/0/Pictures/Screenshots/Screenshot_20180618-232734.jpg
                            }
                        }

                    }finally {
                        if(actualimagecursor!=null){
                            actualimagecursor.close();
                        }
                    }
                }

                break;

        }
    }

    private void myDismissToMiss(String response, int id){
        uiFlusHandler.sendEmptyMessage(DISMISS_LOADING_DIALOG);

        // 动态添加fragment
        // 即将创建的fragment添加到Activity布局文件中定义的占位符中（FrameLayout）
        FragmentManager manager_ff = getFragmentManager();
        FragmentTransaction transaction_ff = manager_ff.beginTransaction();
        final  FoundFragment foundFragment = new FoundFragment();
        // 作用:存储数据，并传递到Fragment中
        Bundle bundle = new Bundle();
        // 往bundle中添加数据
        bundle.putString("titleStr", titleStr);
        bundle.putString("contentStr", contentStr);


        switch (id) {
            case 1:
                if (response.contains("success")) {//有图片
                    DisplayToast("成功");

                    bundle.putString("img_path", img_path);
                    // 把数据设置到Fragment中
                    foundFragment.setArguments(bundle);
                    transaction_ff.commit();

                    finish();
                } else {
                    DisplayToast("请设置图片");

                }
                break;
            default:
                DisplayToast("what?");
                break;
        }
    }

    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis() - mExitTime)<2000){
                Intent intent = new Intent();
                // 标记这个activity启动
                int flag = 1;
                intent.putExtra("flag", flag); //将计算的值回传回去
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(1909, intent);
                finish();
            }else{
                Toast.makeText(this,"再按一次即保存并退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }
}
