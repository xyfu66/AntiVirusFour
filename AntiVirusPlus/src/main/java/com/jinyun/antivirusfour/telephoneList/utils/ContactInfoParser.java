package com.jinyun.antivirusfour.telephoneList.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.jinyun.antivirusfour.telephoneList.entity.ContactInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人信息的解析器，用于从系统通讯录读取 联系人信息
 */

public class ContactInfoParser{
    public static List<ContactInfo> getSystemContact(Context context){
        ContentResolver resolver=context.getContentResolver();
        //1. 查询raw_contacts表，把联系人的id取出来
        Uri uri= Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos=new ArrayList<ContactInfo>();
        Cursor cursor=resolver.query(uri,new String[] {"contact_id"},null,null,null);
        while(cursor.moveToNext()){
            String id=cursor.getString(0);
            if(id!=null){
                System.out.println("联系人id: "+id);
                ContactInfo info=new ContactInfo();
                info.setId(id);
                //2. 根据联系人的id，查询data表，把这个id的数据取出来
                //系统API查询data表的时候，不是真正的查询data表，而是查询data表的视图
                Cursor dataCursor=resolver.query(datauri,new String[]{"data1","mimetype"},"raw_contact_id=?",new String[] {id},null);
                while(dataCursor.moveToNext()){
                    String data1=dataCursor.getString(0);
                    String mimetype=dataCursor.getString(1);
                    if("vnd.android.cursor.item/name".equals(mimetype)){
                        System.out.println("姓名="+data1);
                        info.setName(data1);
                    }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        System.out.println("电话="+data1);
                        info.setNumber(data1);
                    }
                }
                dataCursor.close();

                // 过滤掉脏数据 infos.add(info);
                if (!TextUtils.isEmpty(info.getName()) && !TextUtils.isEmpty(info.getNumber())) {
                    infos.add(info);
                }

            }
        }
        cursor.close();
        return infos;
    }
}
