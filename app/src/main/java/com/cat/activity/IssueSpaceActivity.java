package com.cat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cat.R;
import com.cat.adapter.SpaceListAdapter;
import com.cat.entity.SpaceItem;
import com.cat.entity.SpaceJson;
import com.cat.layout.VerticalSwipeRefreshLayout;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.cat.activity.MainActivity.stringToList;

public class IssueSpaceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private android.app.AlertDialog dialog;
    private SwipeMenuListView listView;
    private VerticalSwipeRefreshLayout refreshLayout;
    private final List<SpaceItem> list = new ArrayList<>();
    private List<SpaceJson> spaceJsons;
    private String userId;
    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";

    //共享变量
    private SharedPreferences sharedPreferences;

    private SpaceListAdapter spaceListAdapter;

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
        setContentView(R.layout.activity_issue_space);
        initView();
    }

    private void initView() {
        dialog = new SpotsDialog(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_issue);

        toolbar.setTitle("可发布的车位");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(v ->
               { Intent intent = new Intent(IssueSpaceActivity.this,SharedActivity.class);
                startActivity(intent);
                finish();});

        asyncHttpClient = new AsyncHttpClient();

        listView = (SwipeMenuListView) findViewById(R.id.spaceList);
        refreshLayout = (VerticalSwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setVerticalScrollBarEnabled(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("112","lailelaodi");
                        loadData();
                        refreshLayout.setRefreshing(false);
                    }
                },2500);
            }
        });
        spaceListAdapter = new SpaceListAdapter(IssueSpaceActivity.this,list);
        //listView.setAdapter(spaceListAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(this, SharedActivity.class);
            if (spaceJsons != null)
                intent.putExtra("spaceJson",spaceJsons.get(position));
                startActivity(intent);
                finish();
        });
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                SpaceItem item = list.get(position);
                switch (index) {
                    case 0:
                        deleteMethod(position,item.getSpaceId());
                        break;
                }
                return false;
            }
        });
    }


    private void loadData() {
        //伪数据
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        list.clear();
        userId = sharedPreferences.getString("userid", null);

        RequestParams rp = new RequestParams();
        rp.put("userid", userId);
        asyncHttpClient.post(BASEURL + "space/getSpacesByUserId",rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                try {
                    String retcode = response.getString("retcode");
                    if (retcode != null && !retcode.equals("0000")) {
                        String errorMsg = response.getString("errorMsg");
                        Toast.makeText(IssueSpaceActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        if(retcode.equals("0003")){
                            SpaceCreate();
                        }

                    } else {
                        spaceJsons = stringToList(response.getJSONArray("obj").toString(),SpaceJson.class);
                        for(SpaceJson sj:spaceJsons){
                            SpaceItem item = new SpaceItem();
                            item.setSpaceName(sj.getSpaceName());
                            item.setSpaceNum(sj.getSpaceNum());
                            item.setOwnerName(sj.getOwnerName());
                            item.setInnerTime(sj.getReleaseTime());
                            item.setContactNum(sj.getContactNum());
                            list.add(item);
                            listView.setAdapter(spaceListAdapter);
                        }
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
                Toast.makeText(IssueSpaceActivity.this, "网络出错，请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(spaceListAdapter);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private void SpaceCreate(){
        Log.i("112","hhh");
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage("你还没有车位，是否去上报车位？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(IssueSpaceActivity.this,AddParkingActivity.class);
                        intent.putExtra("judge","1");
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent= new Intent(IssueSpaceActivity.this,SharedActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();

    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private void deleteMethod(final int position, final Integer bookId) {
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage("确定要下架吗,这样做会扣除一点积分")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        // 通过程序我们知道删除了，但是怎么刷新ListView呢？
//                        // 只需要重新设置一下adapter
//                        asyncHttpClient = new AsyncHttpClient();
//                        RequestParams rp = new RequestParams();
//                        rp.put("bookid", String.valueOf(bookId));
//                        asyncHttpClient.post(BASEURL + "bookShelf/deleteBooksByBookId",rp, new JsonHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(JSONObject response) {
//                                super.onSuccess(response);
//                                try {
//                                    String errorMsg = response.getString("errorMsg");
//                                    Toast.makeText(MySpaceActivity.this,errorMsg, Toast.LENGTH_SHORT).show();
//                                    String retcode = response.getString("retcode");
//                                    if(retcode.equals("0000")){
//                                        list.remove(position);
//                                        listView.setAdapter(spaceListAdapter);
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Throwable error) {
//                                super.onFailure(error);
//                                Toast.makeText(BookShelfActivity.this, "网络出错，请检查网络！", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
