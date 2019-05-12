package com.cat.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cat.R;

import java.util.Calendar;

public class SharedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView startTime;
    private TextView endTime;
    private Button submit;
    private TextView select_text;

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
            int[] attribute = new int[]{android.R.attr.colorPrimary};
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();

            window.setStatusBarColor(color);

        }
        setContentView(R.layout.activity_shared);
        initView();
    }

    private void initView() {

        /*初始化各个控件*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_shared);
        startTime= (TextView) findViewById(R.id.startTime);
        endTime=(TextView) findViewById(R.id.endTime);
        select_text= (TextView) findViewById(R.id.select_text);
        submit=(Button)findViewById(R.id.btn_submit);

        setSupportActionBar(toolbar);
        //
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> finish());

        startTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            // 创建一个TimePickerDialog实例，并把它显示出来
            new TimePickerDialog(SharedActivity.this,
                    // 绑定监听器
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {
                            startTime.setText( hourOfDay + ":" + minute);
                        }
                    }
                    // 设置初始时间
                    , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    // true表示采用24小时制
                    true).show();
        });
        endTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            // 创建一个TimePickerDialog实例，并把它显示出来
            new TimePickerDialog(SharedActivity.this,
                    // 绑定监听器
                    new TimePickerDialog.OnTimeSetListener() {

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {
                            endTime.setText( hourOfDay + ":" + minute);
                        }
                    }
                    // 设置初始时间
                    , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    // true表示采用24小时制
                    true).show();
        });
        select_text.setOnClickListener(v -> {
            Intent intent = new Intent(SharedActivity.this,IssueSpaceActivity.class);
            startActivity(intent);
        });
        submit.setOnClickListener(v ->{

                });

    }


}
