package com.cat.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.baidu.mapapi.model.LatLng;
import com.cat.R;
import com.cat.entity.Bookstore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;





public class MainActivity extends AppCompatActivity implements View.OnClickListener
        ,NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    //变量声明
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private LinearLayout park;
    private LinearLayout share;
    private int mBackKeyPressedTimes;
    private ImageView headpic;
    private TextView username;
    private TextView phone;
    private static MainActivity mainActivity;
    SharedPreferences preferences;
    private ViewFlipper flipper;
    private GestureDetector mDetector; //手势检测


    //控件声明
    private AlertDialog dialog;
    private Dialog dialog1;

    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkDeviceToken();

    }

    private void checkDeviceToken() {
        preferences.edit().putBoolean("isLogin",true).apply();
        if(!preferences.getBoolean("isSend",false)&&preferences.getBoolean("isGetToken",false)) {
            //提交DeviceToken
            preferences.edit().putBoolean("isSend",true).apply();
            RequestParams rp = new RequestParams();
            rp.put("devicetoken", preferences.getString("devicetoken",""));
            rp.put("userid", preferences.getString("userid",""));
            asyncHttpClient.post(BASEURL + "SPuser/registerDevice", rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    super.onSuccess(response);
                    try {
                        String retcode = response.getString("retcode");
                        String errMsg = response.getString("errorMsg");
                        String obj = response.getString("obj");
                        if (!retcode.equals("0000")) {
                            Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable error) {
                    super.onFailure(error);
                    Toast.makeText(MainActivity.this, "device Token上传失败！", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mBackKeyPressedTimes = 0;
                }
            }).start();
            return;
        }
        else{
                finish();
            }
        super.onBackPressed();
    }

    private void initView() {
        asyncHttpClient = new AsyncHttpClient();
        dialog = new SpotsDialog(MainActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        park=(LinearLayout)findViewById(R.id.park) ;
        share=(LinearLayout)findViewById(R.id.share);
        park.setOnClickListener(this);
        share.setOnClickListener(this);
        NavigationView view = (NavigationView)findViewById(R.id.navigation_view);
        View headerLayout = view.inflateHeaderView(R.layout.header);
        headpic = (ImageView) headerLayout.findViewById(R.id.profile_image);
        username = (TextView) headerLayout.findViewById(R.id.username);
        phone = (TextView) headerLayout.findViewById(R.id.email);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        mainActivity = this;
        setSupportActionBar(toolbar);


        flipper = (ViewFlipper) findViewById(R.id.flipper);
        flipper.setOnTouchListener(this);

        flipper.setOnClickListener(this);
        //flipper.startFlipping();//开始切换，注意，如果设置了时间间隔，想让它自动切换，一定要记得加它

//        if(flipper.getDisplayedChild()==0){
//            flipper.getCurrentView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    // TODO Auto-generated method stub
//                    Toast.makeText(MainActivity.this, "我是第  "+flipper.getDisplayedChild(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }


        mDetector = new GestureDetector(new simpleGestureListener());

        headpic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PersonDataActivity.class);
                startActivity(intent);
            }
        });
        navigationView = view;
        navigationView.setNavigationItemSelectedListener(this);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()){
            case R.id.inbox:
                //个人资料
                Intent intent = new Intent(MainActivity.this,PersonDataActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_wallet:
                Intent intent2 = new Intent(MainActivity.this,WalletActivity.class);
                startActivity(intent2);
                return true;
            case R.id.nav_car:
                Intent intent3 = new Intent(MainActivity.this,MyCarActivity.class);
                startActivity(intent3);
                return true;
            case R.id.nav_space:
                Intent intent4 = new Intent(MainActivity.this,MySpaceActivity.class);
                startActivity(intent4);
                return true;
            case R.id.nav_fixer:
                Intent intent5 = new Intent(MainActivity.this,MySpaceActivity.class);
                startActivity(intent5);
                return true;
            case R.id.nav_share:
                Intent intent6 = new Intent(MainActivity.this,TaskShareActivity.class);
                startActivity(intent6);
                return true;



//            case R.id.starred:
//                //借阅记录
//                Intent intent2 = new Intent(MainActivity.this,BookLendHistoryActivity.class);
//                intent2.putExtra("title","借阅记录");
//                startActivity(intent2);
//                return true;
//            case R.id.sent_mail:
//                //我的书架
//                Intent intent3 = new Intent(MainActivity.this,BookShelfActivity.class);
//                intent3.putExtra("title","我的书架");
//                startActivity(intent3);
//                return true;
//            case R.id.drafts:
//                //设置
//                Intent intent4 = new Intent(MainActivity.this,ReservationSActivity.class);
//                intent4.putExtra("title","我的预约");
//                startActivity(intent4);
//                return true;
//            case R.id.allmail:
//                //使用指南
//                Intent intent5 = new Intent(MainActivity.this,ReservationRActivity.class);
//                intent5.putExtra("title","被预约信息");
//                startActivity(intent5);
//                return true;
//            case R.id.trash:
//                //关于我们
//                Intent intent6 = new Intent(MainActivity.this,TemplateActivity.class);
//                intent6.putExtra("title","关于我们");
//                startActivity(intent6);
//                return true;
            case R.id.nav_setting:
                Intent intent7 = new Intent(MainActivity.this,SettingOtherActivity.class);
                startActivity(intent7);
                return true;
            case R.id.nav_kfu:
                Intent intent8 = new Intent(MainActivity.this,MySpaceActivity.class);
                startActivity(intent8);
                return true;


            default:
                return true;

        }
    }



    private void initData() {
        dialog.show();
       // Picasso.with(context().load(preferences.getString("headpic","")).placeholder(R.drawable.default_image).into(headpic);//set bg
        Picasso.with(this).load(preferences.getString("headpic","")).placeholder(R.drawable.me).into(headpic);
        username.setText(preferences.getString("username",""));
        phone.setText(preferences.getString("phone",""));
        asyncHttpClient.post(BASEURL + "bookStore/getAllBookStore",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);

                try {
                    String s = response.getString("obj");
                    dialog.dismiss();
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Throwable error) {
                super.onFailure(error);
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "获取数据失败，请联系管理员！", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,RecentActiveActivity.class);
            intent.putExtra("title","消息");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        initData();
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.park:
                Intent intent = new Intent(MainActivity.this,Main_homeActivity.class);
                startActivity(intent);
                break;
            case R.id.share:
                Intent intent1 = new Intent(MainActivity.this,SharedActivity.class);
                startActivity(intent1);
                break;
            case R.id.flipper:
//                for (int i = 0; i < 4; i++) {
//                    Toast.makeText(MainActivity.this, "我是第 "+i+" 个", Toast.LENGTH_SHORT).show();
//                }
                break;

        }
    }



    /**
     * 把json 字符串转化成list
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json , Class<T> cls  ){
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(gson.fromJson(elem, cls));
        }
        return list ;
    }



    public static void finishMain(){
        mainActivity.finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
    private class simpleGestureListener extends GestureDetector.SimpleOnGestureListener{
        final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;

        //不知道为什么，不加上onDown函数的话，onFling就不会响应，真是奇怪
        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            Toast.makeText(MainActivity.this, "ondown", Toast.LENGTH_SHORT).show();
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // Fling left
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                flipper.setInAnimation(MainActivity.this, R.anim.right_in);
                flipper.setOutAnimation(MainActivity.this, R.anim.left_out);
                flipper.showNext();

                Toast.makeText(MainActivity.this, "Fling Left", Toast.LENGTH_SHORT).show();
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                // Fling right
                flipper.setInAnimation(MainActivity.this, R.anim.left_in);
                flipper.setOutAnimation(MainActivity.this, R.anim.right_out);
                flipper.showPrevious();

                Toast.makeText(MainActivity.this, "Fling Right", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

    }
}
