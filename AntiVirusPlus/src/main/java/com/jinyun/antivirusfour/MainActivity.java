package com.jinyun.antivirusfour;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.didikee.donate.AlipayDonate;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jinyun.antivirusfour.emptyFolderCleanup.EmptyMainActivity;
import com.jinyun.antivirusfour.health.HealthMainActivity;
import com.jinyun.antivirusfour.health.LoginHealthActivity;
import com.jinyun.antivirusfour.homeMain.HomeMainActivity;
import com.jinyun.antivirusfour.homeMain.qrCode.WebViewActivity;
import com.jinyun.antivirusfour.homeMain.titleBarView.ActionItem;
import com.jinyun.antivirusfour.homeMain.titleBarView.TitlePopup;
import com.jinyun.antivirusfour.homeMain.view.LauncherViewPagerAdapter;
import com.jinyun.antivirusfour.newsReader.NewsActivity;
import com.jinyun.antivirusfour.setting.SettingsActivity;
import com.jinyun.antivirusfour.trafficApps.activity.Showmain;

import com.google.zxing.client.android.CaptureActivity2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements OnClickListener {
    private final String TAG = this.getClass().toString();

    private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE_OP = 2;
    private static final int REQUEST_CODE_OP = 3;

    private Button button4, button5, button6, button7, button8;

    private Context mContext;
    private Handler mHandler;
    private Runnable mRunnable;
    private ViewPager mViewPager;
    private int viewPagerItemSize = 0;
    private ImageView[] dotImageViews;
    private final int INTERVAL = 1000 * 3;
    private ArrayList<Integer> mArrayList;
    private LinearLayout mDotsLinearLayout;
    private final static int SET_VIEWPAGER_ITEM = 9527;
    private LauncherViewPagerAdapter mViewPagerAdapter;

    private TitlePopup titlePopup;//右上角更多按钮，实际上是ImageView
    //扫一扫用到的两个常量
    private static final int SCAN_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION = 110;

    //FKX03157FJGZYJ91GQRFD2?t=1527433687975
    public static final String Donate_USER_INPUT = "FKX03157FJGZYJ91GQRFD2";//捐助我们付款码


    private PageChangeListenerImpl mPageChangeListenerImpl;

    /* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_test);
        View v = this.findViewById(R.id.button1);
        v.setOnClickListener(this);
        v = this.findViewById(R.id.button2);
        v.setOnClickListener(this);
        v = this.findViewById(R.id.button3);
        v.setOnClickListener(this);
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(this);
        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
        button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(this);
        button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(this);
        button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(this);

        initData();

        if (viewPagerItemSize > 0) {
            initDots();
            initViewPager();
            setAutoChangeViewPager();
        }

        initPopWindow();

        ImageView mRightImgvIndex = (ImageView) findViewById(R.id.imgv_rightbtn_index);
        mRightImgvIndex.setImageResource(R.drawable.add);
        mRightImgvIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });


    }


	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
            Uri mPath = data.getData();
            String file = getPath(mPath);
            Bitmap bmp = Application.decodeImage(file);
            if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0) {
                Log.e(TAG, "error");
            } else {
                Log.i(TAG, "bmp [" + bmp.getWidth() + "," + bmp.getHeight());
            }
            startRegister(bmp, file);
        } else if (requestCode == REQUEST_CODE_OP) {
            Log.i(TAG, "RESULT =" + resultCode);
            if (data == null) {
                return;
            }
            Bundle bundle = data.getExtras();
            String path = bundle.getString("imagePath");
            Log.i(TAG, "path=" + path);
        } else if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Uri mPath = ((Application) (MainActivity.this.getApplicationContext())).getCaptureImage();
            String file = getPath(mPath);
            Bitmap bmp = Application.decodeImage(file);
            startRegister(bmp, file);
        } else if (requestCode == SCAN_REQUEST_CODE && resultCode == RESULT_OK) {
            //扫一扫 返回的信息
            String isbn = data.getStringExtra("CaptureIsbn");
            if (!TextUtils.isEmpty(isbn)) {
                //todo something
                Toast.makeText(this, "解析到的内容为" + isbn, Toast.LENGTH_LONG).show();
                if (isbn.contains("http")) {
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.RESULT, isbn);
                    startActivity(intent);
                }
            }
        }
    }


    @Override
    public void onClick(View paramView) {
        // TODO Auto-generated method stub
        switch (paramView.getId()) {
            case R.id.button8://无笔健康
                startMyAnimation(paramView);
                whetherToOpen();//是否是第一次进入
                break;
            case R.id.button7://更多功能
                startMyAnimation(paramView);
                Toast.makeText(MainActivity.this, "别着急，正在开发中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6://流量统计
                startMyAnimation(paramView);
                startActivity(Showmain.class);
                break;
            case R.id.button5://更多功能
                startMyAnimation(paramView);
                Toast.makeText(MainActivity.this, "别着急，正在开发中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4://新闻
                startMyAnimation(paramView);
                startActivity(NewsActivity.class);
                break;
            case R.id.button3://手机安全卫士
                startMyAnimation(paramView);
                startActivity(HomeMainActivity.class);
                break;
            case R.id.button2://人脸杀毒
                startMyAnimation(paramView);
                if (((Application) getApplicationContext()).mFaceDB.mRegister.isEmpty()) {
                    Toast.makeText(this, "没有注册人脸，请先注册！", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("请选择相机")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setItems(new String[]{"后置相机", "前置相机"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startDetector(which);
                                }
                            })
                            .show();
                }
                break;
            case R.id.button1://注册人脸
                new AlertDialog.Builder(this)
                        .setTitle("请选择注册方式")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setItems(new String[]{"打开图片", "拍摄照片"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 1:
                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                        ContentValues values = new ContentValues(1);
                                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                        ((Application) (MainActivity.this.getApplicationContext())).setCaptureImage(uri);
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
                                        break;
                                    case 0:
                                        Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
                                        getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
                                        getImageByalbum.setType("image/jpeg");
                                        startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
                                        break;
                                    default:
                                        ;
                                }
                            }
                        })
                        .show();
                break;
            default:
                ;
        }
    }

    //自定义缩放动画
    private void startMyAnimation(View paramView) {
        //相对于自身的百分比
        ScaleAnimation sa = new ScaleAnimation(0.85f, 1, 0.85f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(500);
        paramView.startAnimation(sa);
    }

    /**
     * 开启新的Activity不关闭自己,cls是行Activity字节码
     */
    public void startActivity(Class<?> cls) {
        Timer time = new Timer();
        final Intent intent = new Intent(MainActivity.this, cls);
        TimerTask tk = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };
        time.schedule(tk, 420);
    }

    /**
     * @param uri
     * @return
     */
    private String getPath(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(this, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(this, contentUri, selection, selectionArgs);
                }
            }
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        String end = img_path.substring(img_path.length() - 4);
        if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
            return null;
        }
        return img_path;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param mBitmap
     */
    private void startRegister(Bitmap mBitmap, String file) {
        Intent it = new Intent(MainActivity.this, RegisterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", file);
        it.putExtras(bundle);
        startActivityForResult(it, REQUEST_CODE_OP);
    }

    private void startDetector(int camera) {
        Intent it = new Intent(MainActivity.this, DetecterActivity.class);
        it.putExtra("Camera", camera);
        startActivityForResult(it, REQUEST_CODE_OP);
    }


    //准备ViewPager将显示的数据
    private void initData() {
        mContext = this;
        mArrayList = new ArrayList<Integer>();
        mArrayList.add(R.drawable.main_banner0);
        mArrayList.add(R.drawable.main_banner1);
        mArrayList.add(R.drawable.main_banner2);
        mArrayList.add(R.drawable.main_banner3);
        viewPagerItemSize = mArrayList.size();
    }

    //初始化ViewPager
    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        mViewPagerAdapter = new LauncherViewPagerAdapter(mContext);
        mViewPagerAdapter.setAdapterData(mArrayList);
        mViewPager.setAdapter(mViewPagerAdapter);
        int currentItem = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % viewPagerItemSize;
        mViewPager.setCurrentItem(currentItem);
        setdotImageViews(currentItem % viewPagerItemSize);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mViewPager.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    //初始化底部小圆点
    private void initDots() {
        mDotsLinearLayout = (LinearLayout) findViewById(R.id.dotsLinearLayout);
        dotImageViews = new ImageView[viewPagerItemSize];
        for (int i = 0; i < dotImageViews.length; i++) {
            LinearLayout layout = new LinearLayout(mContext);
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.guide_dot_white);
            } else {
                layout.setPadding(20, 0, 0, 0);
                imageView.setBackgroundResource(R.drawable.guide_dot_black);
            }
            dotImageViews[i] = imageView;
            layout.addView(imageView);
            mDotsLinearLayout.addView(layout);
        }
    }

    //开启ViewPager的自动轮播
    @SuppressWarnings("deprecation")
    private void setAutoChangeViewPager() {
        mPageChangeListenerImpl = new MainActivity.PageChangeListenerImpl();
        mViewPager.setOnPageChangeListener(mPageChangeListenerImpl);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SET_VIEWPAGER_ITEM:
                        if (mViewPager != null && mViewPagerAdapter != null) {
                            int currentItemIndex = mViewPager.getCurrentItem();
                            int itemsCount = mViewPagerAdapter.getCount();
                            if ((currentItemIndex + 1) < itemsCount) {
                                mViewPager.setCurrentItem(currentItemIndex + 1, true);
                            } else {
                                mViewPager.setCurrentItem(0, false);
                            }
                        }
                        break;
                }
            }
        };

        mRunnable = new Runnable() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                message.what = SET_VIEWPAGER_ITEM;
                mHandler.sendMessage(message);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(this, INTERVAL);
            }
        };

        mHandler.postDelayed(mRunnable, INTERVAL);
    }


    //设置小圆点的显示
    private void setdotImageViews(int selected) {
        for (int i = 0; i < dotImageViews.length; i++) {
            dotImageViews[selected].setBackgroundResource(R.drawable.guide_dot_white);
            if (selected != i) {
                dotImageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
            }
        }
    }


    //ViewPager翻页后更新小圆点的显示
    private class PageChangeListenerImpl implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int selected) {
            setdotImageViews(selected % viewPagerItemSize);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mViewPager) {
            mViewPager.removeAllViews();
            mViewPager = null;
        }
    }

    private void initPopWindow() {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(onitemClick);
        // 给标题栏弹窗添加子类

        titlePopup.addAction(new ActionItem(this, R.string.menu_qrcode,
                R.drawable.icon_menu_sao));//扫一扫
        titlePopup.addAction(new ActionItem(this, R.string.menu_setting,
                R.drawable.settings03_min_solid));//设置
        titlePopup.addAction(new ActionItem(this, R.string.menu_money,
                R.drawable.abv));//捐助我们

    }

    private TitlePopup.OnItemOnClickListener onitemClick = new TitlePopup.OnItemOnClickListener() {

        @Override
        public void onItemClick(ActionItem item, int position) {
            // mLoadingDialog.show();
            switch (position) {
                case 0:// 扫一扫
                    if (Build.VERSION.SDK_INT > 22) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                        } else {
                            startScanActivity();
                        }
                    } else {
                        startScanActivity();
                    }

                    break;
                case 1://设置
                    Intent i_setting = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivityForResult(i_setting, 1);
                    break;
                case 2:// 捐助我们
                    // 用户手动输入金额
                    donateAlipay(Donate_USER_INPUT);
                    break;
                default:
                    break;
            }
        }
    };

    private void startScanActivity() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity2.class);
        intent.putExtra(CaptureActivity2.USE_DEFUALT_ISBN_ACTIVITY, true);
        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }

    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
     *                payCode = stx00187oxldjvyo3ofaw60
     *                注：不区分大小写
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode);
        }
    }

    //判断是否是第一次打开无笔健康 是则跳转设置界面 否则直接进入主页面
    private void whetherToOpen() {
        SharedPreferences shared = getSharedPreferences("firstInLHA", MODE_PRIVATE);
        boolean isfer = shared.getBoolean("isFirstInLHA", true);
        if (isfer) {
            //第一次进入跳转
            startActivity(LoginHealthActivity.class);
        } else {
            //第二次进入跳转
            startActivity(HealthMainActivity.class);
        }

    }
}

