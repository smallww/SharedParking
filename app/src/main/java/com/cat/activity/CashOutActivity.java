package com.cat.activity;

import android.app.VoiceInteractor;
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

import java.text.DecimalFormat;

import dmax.dialog.SpotsDialog;

public class CashOutActivity extends AppCompatActivity implements View.OnClickListener {

    private SpotsDialog dialog;
    private Toolbar toolbar;
    private TextView allin;
    private Button button;
    private EditText cash_out;


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
        setContentView(R.layout.activity_cash_out);
        initView();
    }

    private void initView() {


        dialog = new SpotsDialog(CashOutActivity.this);
        //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar_cash);
        allin = (TextView) findViewById(R.id.allin);
        button= (Button) findViewById(R.id.bth_out);
        cash_out= (EditText) findViewById(R.id.cash_out);

        allin.setOnClickListener(this);
        button.setOnClickListener(this);

        toolbar.setTitle("余额提现");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> finish());

        //输入框监听
        setTextWatcher();

    }

    private void setTextWatcher() {
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int max= Integer.parseInt(sharedPreferences.getString("balance",""));
        int min=0;
        cash_out.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start >= 0) {//从一输入就开始判断，
                    if (max != -1) {
                        try {
                            int num = Integer.parseInt(s.toString());
                            //判断当前edittext中的数字(可能一开始Edittext中有数字)是否大于max
                            if (num > max) {
                                s = String.valueOf(max);//如果大于max，则内容为max
                                cash_out.setText(s);
                                Toast.makeText(CashOutActivity.this, "提现金额的数目不能超过"+ max +"元哦", Toast.LENGTH_SHORT).show();

                            } else if (num <= min) {
                                Toast.makeText(CashOutActivity.this, "提现金额的数目不能为空哦", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        //edittext中的数字在max和min之间，则不做处理，正常显示即可。
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.allin:
                sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                cash_out.setText(sharedPreferences.getString("balance",""));
                break;
            case R.id.bth_out:
                Log.i("666","fgdfgdfgd");
                if(("").equals(cash_out.getText().toString())){
                    Toast.makeText(this, "提现金额不能为空，请仔细填写哦", Toast.LENGTH_SHORT).show();
                }else if(("0").equals(cash_out.getText().toString())){
                    Toast.makeText(this, "提现的金额不能为0哦", Toast.LENGTH_SHORT).show();
                }else{
                    sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    userId = sharedPreferences.getString("userid", null);
                    String balance=cash_out.getText().toString().trim();
                    new AlertDialog.Builder(this).setTitle("友情提示").setMessage("确定提现吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestParams rp = new RequestParams();
                                    rp.put("userid",userId);
                                    rp.put("balance",balance);
                                    String s  = BASEURL + "SPuser/outMoney";
                                    asyncHttpClient = new AsyncHttpClient();
                                    asyncHttpClient.post(s,rp,new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            super.onSuccess(response);
                                            Toast.makeText(CashOutActivity.this, "提现成功！啦啦啦", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(CashOutActivity.this, "哎呀呀，出了点问题呢...", Toast.LENGTH_SHORT).show();
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
