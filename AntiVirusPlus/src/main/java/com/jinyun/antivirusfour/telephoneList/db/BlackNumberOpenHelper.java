package com.jinyun.antivirusfour.telephoneList.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 用于创建黑名单数据库
 * blackNumber.db
 * id为自增主键
 * number为电话号码
 * name为联系人姓名
 * mode为拦截模式
 *
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper {
    public BlackNumberOpenHelper(Context context) {
        super(context, "blackNumber.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table blacknumber (id integer primary key autoincrement, number varchar(20),name varchar(255),mode integer)");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
    }
}