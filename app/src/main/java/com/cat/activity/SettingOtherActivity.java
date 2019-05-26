package com.cat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.R;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;

import static com.cat.activity.MainActivity.finishMain;

public class SettingOtherActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button exit;
    private LinearLayout news_setting;
    private LinearLayout help_center;
    private LinearLayout feedback;
    private LinearLayout about_us;
    private LinearLayout custom_service;
    private TextView phone_num;

    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";
    //定义本地存储
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >=21) {
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
            int[] attribute = new int[] { android.R.attr.colorPrimary };
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();

            window.setStatusBarColor(color);
        }
        setContentView(R.layout.activity_setting_other);
        initView();
    }


    private void initView() {

        toolbar= (Toolbar) findViewById(R.id.toolbar_setting);
        news_setting= (LinearLayout) findViewById(R.id.news_setting);
        help_center= (LinearLayout) findViewById(R.id.help_center);
        feedback= (LinearLayout) findViewById(R.id.feedback);
        about_us= (LinearLayout) findViewById(R.id.about_us);
        custom_service=(LinearLayout) findViewById(R.id.custom_service);

        phone_num= (TextView) findViewById(R.id.phone_num);
        exit= (Button) findViewById(R.id.exit);

        news_setting.setOnClickListener(this);
        help_center.setOnClickListener(this);
        feedback.setOnClickListener(this);
        about_us.setOnClickListener(this);
        custom_service.setOnClickListener(this);
        phone_num.setOnClickListener(this);
        exit.setOnClickListener(this);

        //初始化ToolBar
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
     int id=v.getId();
        switch (id) {
            case R.id.news_setting:
                Toast.makeText(this, "后续加入，敬请期待！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help_center:
                Toast.makeText(this, "后续加入，敬请期待！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.feedback:
                Toast.makeText(this, "后续加入，敬请期待！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Intent intent = new Intent(SettingOtherActivity.this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.custom_service:
                String s=phone_num.getText().toString().trim();

                new AlertDialog.Builder(SettingOtherActivity.this).setTitle("温馨提示").setMessage("确定拨打该电话吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("tel:" + s);
                                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                                startActivity(intent);
                            }
                        }) .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

                break;
            case R.id.phone_num:
                String str=phone_num.getText().toString().trim();

                new AlertDialog.Builder(SettingOtherActivity.this).setTitle("温馨提示").setMessage("确定拨打该电话吗")
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
            case R.id.exit:
                preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                new AlertDialog.Builder(SettingOtherActivity.this).setTitle("友情提示").setMessage("确定退出登录吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //保留token
                                String token = preferences.getString("devicetoken","");
                                boolean isGetToken = preferences.getBoolean("isGetToken",false);
                                preferences.edit().clear().apply();
                                preferences.edit().putString("devicetoken",token).apply();
                                preferences.edit().putBoolean("isGetToken",isGetToken).apply();

                                //跳转Login
                                Intent intent = new Intent(SettingOtherActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finishMain();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;

        }
    }
}
