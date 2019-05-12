package com.cat.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.cat.R;

public class AddParkingActivity extends AppCompatActivity {

    private TextView add_text;
    private ImageView imageView;
    private Toolbar toolbar;
    private TextView text_latitude;
    private TextView text_Longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //状态栏颜色一致
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
            int[] attribute = new int[] { android.R.attr.colorPrimary };
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();

            window.setStatusBarColor(color);
        }
        setContentView(R.layout.activity_add_parking);

        initView();
        Intent intent=getIntent();
        //获取地址信息
        String addressTitle=intent.getStringExtra("address");
        add_text.setText(addressTitle);
        //获取经纬度信息
        String latitude=intent.getStringExtra("latitude");
        String longitude=intent.getStringExtra("longitude");
        text_latitude.setText(latitude);
        text_Longitude.setText(longitude);

    }

    private void initView() {

        /*初始化各个控件*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_add);
        add_text= (TextView) findViewById(R.id.add_text);
        imageView= (ImageView) findViewById(R.id.location);
        text_latitude= (TextView) findViewById(R.id.text_latitude);
        text_Longitude=(TextView) findViewById(R.id.text_Longitude);

        setSupportActionBar(toolbar);
        //
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddParkingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddParkingActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
