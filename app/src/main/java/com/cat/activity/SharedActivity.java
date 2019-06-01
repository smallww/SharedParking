package com.cat.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cat.R;
import com.cat.entity.SpaceItem;
import com.cat.entity.SpaceJson;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONObject;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;

import static com.cat.activity.MainActivity.stringToList;

public class SharedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView startTime;
    private TextView endTime;
    private Integer select_id;
    //共享变量
    private SharedPreferences sharedPreferences;
    private android.app.AlertDialog dialog;

    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";

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
        dialog = new SpotsDialog(this);

        Intent intent = getIntent();
        SpaceJson spaceJson = (SpaceJson)intent.getSerializableExtra("spaceJson");

        /*初始化各个控件*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_shared);
        startTime= (TextView) findViewById(R.id.startTime);
        endTime=(TextView) findViewById(R.id.endTime);
        TextView select_text = (TextView) findViewById(R.id.select_text);
        Button submit = (Button) findViewById(R.id.btn_submit);

        TextView name_text = (TextView) findViewById(R.id.name);
        TextView people_text = (TextView) findViewById(R.id.people);
        TextView phone_text = (TextView) findViewById(R.id.phone);
        TextView code_text = (TextView) findViewById(R.id.code);

        if (spaceJson != null) {
            select_text.setHint("    已选择车位");
            select_text.setHintTextColor(Color.parseColor("#FF0099CC"));
            name_text.setText(spaceJson.getSpaceName());
            people_text.setText(spaceJson.getOwnerName());
            phone_text.setText(spaceJson.getContactNum());
            code_text.setText(spaceJson.getSpaceNum().toString());
            select_id=spaceJson.getSpaceId();

        }

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
                            String hour="";
                            String minu="";
                            if(hourOfDay<10){
                                hour="0"+hourOfDay;
                            }else{
                                hour= String.valueOf(hourOfDay);
                            }
                            if(minute<10){
                                minu="0"+minute;
                            }else{
                                minu= String.valueOf(minute);
                            }

                            startTime.setText( hour + ":" + minu);
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
                            String hour="";
                            String minu="";
                            if(hourOfDay<10){
                                hour="0"+hourOfDay;
                            }else{
                                hour= String.valueOf(hourOfDay);
                            }
                            if(minute<10){
                                minu="0"+minute;
                            }else{
                                minu= String.valueOf(minute);
                            }
                            endTime.setText( hour + ":" + minu);
                        }
                    }
                    // 设置初始时间
                    , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    // true表示采用24小时制
                    true).show();
        });
        select_text.setOnClickListener(v -> {
            Intent intent2 = new Intent(SharedActivity.this,IssueSpaceActivity.class);
            startActivity(intent2);
            finish();
        });
        submit.setOnClickListener(v ->{
            new AlertDialog.Builder(this).setTitle("友情提示").setMessage("是否发布车位？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String start = startTime.getText().toString();
                            String end = endTime.getText().toString();

                            if ("请选择您的车位".equals(select_text.getHint().toString().trim())) {
                                Toast.makeText(SharedActivity.this, "请选择您的车位", Toast.LENGTH_SHORT).show();
                            }
                            else if (start.isEmpty() || end.isEmpty()) {
                                Toast.makeText(SharedActivity.this, "请选择时间段", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                RequestParams rp = new RequestParams();


                                asyncHttpClient = new AsyncHttpClient();
                                rp.put("space_id", String.valueOf(select_id));
                                rp.put("start",start);
                                rp.put("end",end);


                                asyncHttpClient.post(BASEURL + "task/addTask",rp, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        super.onSuccess(response);
                                        try {
                                            String retcode = response.getString("retcode");
                                            if (retcode != null && !retcode.equals("0000")) {
                                                String errorMsg = response.getString("errorMsg");
                                                Toast.makeText(SharedActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                            } else {
                                                Toast.makeText(SharedActivity.this, "发布车位成功", Toast.LENGTH_SHORT).show();
                                                //Log.i("111",spaceListAdapter.getCount()+"!!!!!!!!!!");
                                            }
                                        } catch (Exception e) {
                                            Log.e("111",e.toString());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable error) {
                                        super.onFailure(error);
                                        dialog.dismiss();
                                        Toast.makeText(SharedActivity.this, "网络出错，请检查网络！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();

        });

    }


}
