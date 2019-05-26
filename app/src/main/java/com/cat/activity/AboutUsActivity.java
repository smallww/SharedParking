package com.cat.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.R;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;

import java.io.InputStream;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";
    //定义本地存储
    SharedPreferences preferences;
    private ImageView imageView;
    private Toolbar toolbar;

    private Dialog dialog;
    private ImageView mImageView;
    private LinearLayout version;
    private LinearLayout protocol;
    private LinearLayout about_service;
    private TextView service_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //获取样式中的属性值
            TypedValue typedValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
            int[] attribute = new int[]{android.R.attr.colorPrimary};
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();

            window.setStatusBarColor(color);

        }
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {

        //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar_about);
        imageView = (ImageView) findViewById(R.id.logoIV);

        version= (LinearLayout) findViewById(R.id.version);
        protocol= (LinearLayout) findViewById(R.id.protocol);
        about_service=(LinearLayout) findViewById(R.id.about_service);
        service_phone= (TextView) findViewById(R.id.service_phone);

        version.setOnClickListener(this);
        protocol.setOnClickListener(this);
        about_service.setOnClickListener(this);

        toolbar.setTitle("关于我们");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> finish());



        imageView.setOnClickListener(v -> dialog.show());
        //大图所依附的dialog
        dialog = new Dialog(this, R.style.edit_AlertDialog_style);
        mImageView =getImageView();
        dialog.setContentView(mImageView);

        //大图的点击事件（点击让他消失）
        mImageView.setOnClickListener(v -> dialog.dismiss());
        
    }
    //动态的ImageView
    private ImageView getImageView(){
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20,20,20,20);
        //imageView设置图片
        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.my_logo);
        Drawable drawable = BitmapDrawable.createFromStream(is, null);
        iv.setImageDrawable(drawable);
        return iv;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.version:
                Toast.makeText(this, "当前版本为最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.protocol:
                Toast.makeText(this, "后续加入，敬请期待！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_service:
                String str=service_phone.getText().toString().trim();

                new AlertDialog.Builder(AboutUsActivity.this).setTitle("温馨提示").setMessage("确定拨打该电话吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("tel:" + str);
                                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                startActivity(intent);
                            }
                        }) .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
        }
    }
}
