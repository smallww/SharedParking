package com.cat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.mapapi.map.Text;
import com.cat.R;
import com.cat.entity.BookJson;
import com.cat.entity.Travel;
import com.squareup.picasso.Picasso;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.mapapi.BMapManager.getContext;
import static com.cat.activity.MainActivity.stringToList;

public class ShelfBookActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_TRAVEL = "EXTRA_TRAVEL";
    private AsyncHttpClient asyncHttpClient;
    private Travel travel;
    private String userId;
    private String ISBN;
    private String summary;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.isbn) TextView isbn;
    @BindView(R.id.author) TextView author;
    @BindView(R.id.rating) ImageView rating;
    @BindView(R.id.price) TextView price;
    @BindView(R.id.authortext) TextView authortext;
    @BindView(R.id.publisher) TextView publisher;
    @BindView(R.id.binding) TextView binding;
    @BindView(R.id.url) TextView url;
    @BindView(R.id.pages) TextView pages;
    @BindView(R.id.url_btn) Button url_btn;
    @BindView(R.id.isbnText) TextView isbnText;
    @BindView(R.id.messageText) TextView messageText;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_book);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();

    }

    private void initData() {
        Intent intent = getIntent();
        try {
            JSONObject doubanJson = new JSONObject(intent.getStringExtra("serverResponse"));
            Travel t = new Travel();
            ISBN = doubanJson.getString("isbn");
            summary = doubanJson.getString("describes");
            t.setAuthor(doubanJson.getString("author"));
            t.setName(doubanJson.getString("title"));
            t.setIsbn(doubanJson.getString("isbn"));
            t.setImage(doubanJson.getString("bookImageURI"));
            t.setPrice(doubanJson.getString("price"));
            t.setDoubanURL(doubanJson.getString("douBanURI"));
            t.setPublisher(doubanJson.getString("publisher"));
            t.setPages(doubanJson.getString("page"));
            t.setBinds(doubanJson.getString("binding"));
            t.setRating(doubanJson.getString("rating"));
            t.setBookStoreMessage(doubanJson.getString("message"));
            travel = t;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        url_btn.setOnClickListener(this);
    }

    private void initView() {
        if (travel != null) {
            Picasso.with(getContext()).load(travel.getImage()).placeholder(R.drawable.default_image).into(image);//set bg
            title.setText(travel.getName());

            List<String> tags = travel.getTags();//set tags
//            mTagContainerLayout.setTags(tags);

            author.setText("作者："+travel.getAuthor());
            isbn.setText("ISBN:"+travel.getIsbn());
            url.setText(travel.getDoubanURL());
            binding.setText(travel.getBinds());
            publisher.setText(travel.getPublisher());
            authortext.setText(travel.getAuthor());
            price.setText(travel.getPrice());
            pages.setText(travel.getPages());
            isbnText.setText(travel.getIsbn());
            messageText.setText(travel.getBookStoreMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.url_btn:
                Intent intent = new Intent(ShelfBookActivity.this,WebActivity.class);
                intent.putExtra("url",travel.getDoubanURL());
                startActivity(intent);
                break;
        }
    }

}
