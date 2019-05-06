package com.cat.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cat.R;
import com.cat.activity.SearchParkActivity;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class SearchParkAdapter extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> list_data;
    private LayoutInflater layoutInflater;
    private Context context;

    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/bookstore/restful/";

    //控件声明
    AlertDialog dialog;


    public SearchParkAdapter(Context context, ArrayList<HashMap<String, Object>> list_data) {
        this.context=context;
        this.list_data = list_data;
        this.layoutInflater = LayoutInflater.from(context);
        asyncHttpClient = new AsyncHttpClient();
        dialog = new SpotsDialog(context);

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.search_history_item,null);
        return null;
    }
}
