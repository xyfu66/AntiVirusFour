package com.jinyun.antivirusfour.telephoneList;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jinyun.antivirusfour.R;
import com.jinyun.antivirusfour.telephoneList.adapter.ContactAdapter;
import com.jinyun.antivirusfour.telephoneList.entity.ContactInfo;
import com.jinyun.antivirusfour.telephoneList.utils.ContactInfoParser;

import java.util.List;


/**
 * 显示系统联系人界面
 */

public class ContactSelectActivity extends Activity implements View.OnClickListener{
    private ListView mListView;
    private ContactAdapter adapter;
    private List<ContactInfo> systemContacts;
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 10:
                    if (systemContacts!=null){
                        adapter = new ContactAdapter(systemContacts,ContactSelectActivity.this);
                        mListView.setAdapter(adapter);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_select);

        //判断用户是否已经授权，未授权则向用户申请授权
        if(ContextCompat.checkSelfPermission(ContactSelectActivity.this,"Manifest.permission.READ_CONTACTS")
                != PackageManager.PERMISSION_GRANTED ) {
            // >= 0 : 启动画面是会提示是否允许该App拥有获取手机联系人信息的权限
            //注意第二个参数没有双引号
            ActivityCompat.requestPermissions(ContactSelectActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},1);
        }

        initView();
    }

    private void initView(){
        //findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.lightBlue));
        ((TextView) findViewById(R.id.tv_title)).setText("联系人列表");
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.returns_arrow02);

        mListView = (ListView) findViewById(R.id.lv_contact);

        new Thread(){
            public void run(){
                systemContacts = ContactInfoParser.getSystemContact(ContactSelectActivity.this);
                systemContacts.addAll(ContactInfoParser.getSystemContact(ContactSelectActivity.this));
                mHandler.sendEmptyMessage(10);
            }
        }.start();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ContactInfo item = (ContactInfo) adapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra("number", item.getNumber());
                intent.putExtra("name", item.getName());
                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;
        }
    }
}
