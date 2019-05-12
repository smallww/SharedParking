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

import java.text.DecimalFormat;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imageView;
    private TextView money;
    private DecimalFormat myFormatter;
    Double balance;
    private TextView charge_money;
    private TextView cash_money;

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
        setContentView(R.layout.activity_wallet);
        initView();

    }

    private void initView() {
        //初始化控件
        toolbar = (Toolbar) findViewById(R.id.toolbar_wallet);
        money = (TextView) findViewById(R.id.money);
        imageView = (ImageView) findViewById(R.id.iv_show_ye);
        charge_money= (TextView) findViewById(R.id.charge_money);
        cash_money = (TextView) findViewById(R.id.cash_money);

        money.setOnClickListener(this);
        imageView.setOnClickListener(this);
        cash_money.setOnClickListener(this);
        charge_money.setOnClickListener(this);

        myFormatter = new DecimalFormat("0.00");
        balance = 0.00; //todo 服务器


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_show_ye:
                if (!money.getText().equals("****")) {
                    imageView.setImageResource(R.drawable.pass_visuable);
                    money.setText("****");
                }
                else {
                    money.setText(myFormatter.format(balance));
                    imageView.setImageResource(R.drawable.pass_gone);
                }

                break;
            case R.id.charge_money:
                Intent intent = new Intent(WalletActivity.this,ChargeMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.cash_money:
                break;
        }
    }
}
