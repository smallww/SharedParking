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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.R;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class ChargeMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView money;
    private Button button;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private EditText editText;
    private Button bth_charge;

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
        setContentView(R.layout.activity_charge_money);
        initView();
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar_charge);
        money= (TextView) findViewById(R.id.money);
        button= (Button) findViewById(R.id.button);
        button1= (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button4= (Button) findViewById(R.id.button4);
        button5= (Button) findViewById(R.id.button5);
        editText= (EditText) findViewById(R.id.other_money);

        bth_charge= (Button) findViewById(R.id.bth_charge);

        money.setOnClickListener(this);
        button.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        bth_charge.setOnClickListener(this);



        toolbar.setTitle("现金充值");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> finish());

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                money.setText(editText.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.button:
                money.setText(button.getText().toString().substring(0,2));
                break;
            case R.id.button1:
                money.setText(button1.getText().toString().substring(0,2));
                break;
            case R.id.button2:
                money.setText(button2.getText().toString().substring(0,2));
                break;
            case R.id.button3:
                money.setText(button3.getText().toString().substring(0,3));
                break;
            case R.id.button4:
                money.setText(button4.getText().toString().substring(0,3));
                break;
            case R.id.button5:
                money.setText(button5.getText().toString().substring(0,3));
                break;
            case R.id.bth_charge:
                if(("").equals(money.getText().toString())){
                    Toast.makeText(this, "充值金额不能为空，请仔细填写哦", Toast.LENGTH_SHORT).show();
                }else if(("0").equals(editText.getText().toString())){
                    Toast.makeText(this, "填写的金额不能为0哦", Toast.LENGTH_SHORT).show();
            }else{
                    sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    userId = sharedPreferences.getString("userid", null);
                    String balance=money.getText().toString().trim();
                    new AlertDialog.Builder(this).setTitle("友情提示").setMessage("确定充值吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestParams rp = new RequestParams();
                                    rp.put("userid",userId);
                                    rp.put("balance",balance);
                                    String s  = BASEURL + "SPuser/chargeMoney";
                                    asyncHttpClient = new AsyncHttpClient();
                                    asyncHttpClient.post(s,rp,new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            super.onSuccess(response);
                                            Toast.makeText(ChargeMoneyActivity.this, "充值成功！啦啦啦", Toast.LENGTH_SHORT).show();
                                            String obj = null;
                                            try {
                                                obj = response.getString("obj");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("balance",obj);
                                            editor.apply();
                                            finish();


                                        }
                                        @Override
                                        public void onFailure(Throwable error) {
                                            Toast.makeText(ChargeMoneyActivity.this, "哎呀呀，出了点问题呢...", Toast.LENGTH_SHORT).show();
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

                break;

        }
    }
}
