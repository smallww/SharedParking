package com.cat.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;

import com.cat.R;
import com.cat.adapter.SearchParkAdapter;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchParkActivity extends AppCompatActivity {

    //控件变量声明
    private SearchView search_park;
    private TextView cancel1;
    private ImageView delete1;
    private ListView park_list;


    private ArrayList<HashMap<String, Object>> list_data;

    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";

    private static SearchParkActivity searchParkActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  Log.i("112","asdahgsdfahs");
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

        setContentView(R.layout.activity_search_park);
        initView();




    }
    private void initView(){
         //初始化控件
         search_park= (SearchView) findViewById(R.id.search_park);
         cancel1=(TextView)findViewById(R.id.cancel);
         delete1=(ImageView)findViewById(R.id.delete);
         park_list = (ListView) findViewById(R.id.park_list);
         //设置搜索
         setSearch();
         //取消按钮
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        SearchParkAdapter adapter = new SearchParkAdapter(SearchParkActivity.this,list_data);
        park_list.setAdapter(adapter);
    }
    private void setSearch(){
        //去掉下划线
        try {        //--拿到字节码
            Class<?> argClass = search_park.getClass();
            //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            //--暴力反射,只有暴力反射才能拿到私有属性
            ownField.setAccessible(true);
            View view = (View) ownField.get(search_park);
            //--设置背景
            view.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //搜索图标是否显示在搜索框内
        search_park.setIconifiedByDefault(false);
        //设置搜索框展开时是否显示提交按钮，可不显示
        search_park.setSubmitButtonEnabled(false);
        //让键盘的回车键设置成搜索
        search_park.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        search_park.setIconified(true);

        search_park.setQueryHint("请输入目的地/地标");
        //获取焦点
        search_park.setFocusable(false);
        search_park.requestFocusFromTouch();

        // 设置搜索文本监听
        search_park.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent7 = new Intent(SearchParkActivity.this,HomeActivity.class);
                intent7.putExtra("title","高德地图");
                startActivity(intent7);
                Log.i("111","23");
                //清除焦点，收软键盘
               // search_park.clearFocus();
                return false;

            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //do something
                //当没有输入任何内容的时候清除结果，看实际需求
                Log.i("111","45646");


                return false;
            }
        });
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }

}
