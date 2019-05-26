package com.cat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cat.R;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;

import java.text.DecimalFormat;

import dmax.dialog.SpotsDialog;

public class MyCarActivity extends AppCompatActivity {



    private Toolbar toolbar;
    private TextView car_text;
    private ImageView carIV;
    private LinearLayout car_linear;

    private android.app.AlertDialog dialog;

    private String userId;
    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";

    //共享变量
    private SharedPreferences sharedPreferences;
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
        setContentView(R.layout.activity_my_car);
        initView();
    }

    private void initView() {


        dialog = new SpotsDialog(MyCarActivity.this);
        //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar_space);
        car_text= (TextView) findViewById(R.id.car_text);
        carIV= (ImageView) findViewById(R.id.carIV);
        car_linear= (LinearLayout) findViewById(R.id.car_linear);


        toolbar.setTitle("我的车辆");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> {
//            Intent intent = new Intent(MySpaceActivity.this,MainActivity.class);
//            startActivity(intent);
            finish();});

        car_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MyCarActivity.this,PlateNumActivity.class);
                intent.putExtra("title","修改车牌");
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String s=sharedPreferences.getString("carnumer","");
        if("null".equals(s)){
            new AlertDialog.Builder(this).setTitle("友情提示").setMessage("你还没有上报车牌，是否去上报车牌？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(MyCarActivity.this,PlateNumActivity.class);
                            intent.putExtra("title","上报车牌");
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
        else{
            carIV.setVisibility(View.VISIBLE);
            car_text.setText(s);
        }


    }
}
