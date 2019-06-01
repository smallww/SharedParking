package com.cat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.R;
import com.cat.entity.DataPlateNum;
import com.jungly.gridpasswordview.GridPasswordView;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.layout.simple_spinner_item;

public class PlateNumActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> privince = new ArrayList<>();
    private ArrayList<String> city = new ArrayList<>();
    private Toolbar toolbar;
    private Spinner spinner;
    private Spinner spinner1;
    private TextView mTv;
    private TextView mTv1;
    private GridPasswordView gpv;
    private  String str;
    private  String str1;
    private String pwd ;
    private TextView tv_new;
    private GridPasswordView gpv1;
    private CheckBox cb_new;
    private Button bth_submit;
    private String plateNum;

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
        setContentView(R.layout.activity_plate_num);
        initView();
    }

    private void initView() {

         //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar_car);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        //mTv = (TextView) findViewById(R.id.tv_content);
        cb_new=(CheckBox) findViewById(R.id.cb_new);
       // mTv1 = (TextView) findViewById(R.id.tv_city);
        bth_submit=(Button) findViewById(R.id.bth_submit);
        gpv = (GridPasswordView) findViewById(R.id.plate_gpv);
        gpv.setPasswordVisibility(true);

        gpv1 = (GridPasswordView) findViewById(R.id.plate_gpv1);
        gpv1.setPasswordVisibility(true);

        toolbar.setTitle(getIntent().getStringExtra("title"));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> finish());


        privince = DataPlateNum.getPrivince();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                simple_spinner_item, privince);
        city=DataPlateNum.getCity();
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this,
                simple_spinner_item, city);
        spinner.setAdapter(spinnerAdapter);
        spinner1.setAdapter(spinnerAdapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str=spinnerAdapter.getItem(position);
             // mTv.setText(spinnerAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str1 = spinnerAdapter1.getItem(position);
               // mTv.setText(str + spinnerAdapter1.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gpv.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(String psw) {
                pwd=gpv.getPassWord();
                plateNum=str + str1+ pwd;
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onInputFinish(String psw) {

            }
        });
        gpv1.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(String psw) {
                pwd=gpv1.getPassWord();
                plateNum=str + str1+ pwd;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onInputFinish(String psw) {

            }
        });
        cb_new.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    gpv.clearPassword();
                    gpv.setVisibility(View.GONE);
                    gpv1.setVisibility(View.VISIBLE);
                }
                else {
                    gpv1.clearPassword();
                    gpv1.setVisibility(View.GONE);
                    gpv.setVisibility(View.VISIBLE);
                }
            }
        });
        bth_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                if(("").equals(gpv.getPassWord())&&("").equals(gpv1.getPassWord())){
                    Toast.makeText(PlateNumActivity.this, "车牌号不能为空，请仔细填写哦", Toast.LENGTH_SHORT).show();
                }else{
                    userId = sharedPreferences.getString("userid", null);
                    String carNum=plateNum;
                    new AlertDialog.Builder(PlateNumActivity.this).setTitle("友情提示").setMessage("确定您的车牌号: "+plateNum+" 哦")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestParams rp = new RequestParams();
                                    rp.put("userid",userId);
                                    rp.put("carNum",carNum);
                                    String s  = BASEURL + "SPuser/addPlateNum";
                                    asyncHttpClient = new AsyncHttpClient();
                                    asyncHttpClient.post(s,rp,new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            super.onSuccess(response);
                                            Toast.makeText(PlateNumActivity.this, "上报车牌号成功！啦啦啦", Toast.LENGTH_SHORT).show();
                                            String obj = null;
                                            try {
                                                obj = response.getString("obj");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("carNum",obj);
                                            editor.apply();
                                            finish();


                                        }
                                        @Override
                                        public void onFailure(Throwable error) {
                                            Toast.makeText(PlateNumActivity.this, "哎呀呀，出了点问题呢...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });






    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//
//        }
    }
}
