package com.jinyun.antivirusfour.advanced.utils;

/**
 * Created by ENG on 2018/3/18.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *  短信的工具类，提供短信备份API
 *  @author Administrator
 */
public class SmsBackUpUtils {
    /**
     *  定义的一个接口，用作回调
     *  @author Administrator
     */
    public interface BackupStatusCallback{
        /**
         *  在短信备份之前调用的方法
         *  @param size 总的短信的个数
         */
        public void beforeSmsBackup(int size);
        /**
         *  当sms短信备份过程中调用的方法
         *  @param process 当前的进度
         */
        public void onSmsBackup(int process);
    }
    private boolean flag=true;
    public void setFlag(boolean flag){
        this.flag=flag;
    }
    /**
     *  备份短信
     *  @param context 上下文
     *  @param callback 实现BackupStatusCallback接口的对象
     *  @return
     *  @throws FileNotFoundException
     */
    public boolean backUpSms(Context context,BackupStatusCallback callback)
        throws FileNotFoundException,IllegalStateException,IOException {
        XmlSerializer serializer= Xml.newSerializer();
        File sdDir= Environment.getExternalStorageDirectory();
        long freesize=sdDir.getFreeSpace();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && freesize>10241*10241){
            File file=new File(Environment.getExternalStorageDirectory(),"backup.xml");
            FileOutputStream os=new FileOutputStream(file);
            //初始化xml文件的序列化器
            serializer.setOutput(os,"utf-8");
            //写xml文件头
            serializer.startDocument("utf-8",true);
            //写根节点
            ContentResolver resolver=context.getContentResolver();
            Uri uri=Uri.parse("content://sms/");
            Cursor cursor=resolver.query(uri,new String[] {"address","body","type","date"},null,null,null);
            //得到总的条目个数
            int size=cursor.getCount();
            //设置进度的总大小
            callback.beforeSmsBackup(size);
            serializer.startTag(null,"smss");
            serializer.attribute(null,"size",String.valueOf(size));
            int process=0;
            while(cursor.moveToNext() & flag) {
                serializer.startTag(null,"sms");
                serializer.startTag(null,"body");
                //可能会有乱码问题需要处理，如果出现乱码会导致备份失败
                try{
                    String bodyencpyt=Crypto.encrypt("123",cursor.getString(1));
                    serializer.text(bodyencpyt);
                } catch (Exception el){
                    el.printStackTrace();
                    serializer.text("短信读取失败");
                }
                serializer.endTag(null,"body");
                serializer.startTag(null,"address");
                serializer.text(cursor.getString(0));

                serializer.endTag(null,"address");
                serializer.startTag(null,"type");
                serializer.text(cursor.getString(2));

                serializer.endTag(null,"type");
                serializer.startTag(null,"date");
                serializer.text(cursor.getString(3));

                serializer.endTag(null,"date");
                serializer.endTag(null,"sms");
                try{
                    Thread.sleep(600);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                //设置进度调对话框的进度
                process++;
                callback.onSmsBackup(process);
            }
            cursor.close();
            serializer.endTag(null,"smss");
            serializer.endDocument();
            os.flush();
            os.close();
            return flag;
        }else{
            throw new IllegalStateException("sd 卡不存在或者空间不足");
        }
    }
}
