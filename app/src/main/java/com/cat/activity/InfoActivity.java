package com.cat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.cat.R;
import com.cat.entity.Travel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.mapapi.BMapManager.getContext;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_TRAVEL = "EXTRA_TRAVEL";
    private Travel travel;
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

    public static Intent newInstance(Context context, Travel travel) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(EXTRA_TRAVEL, travel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        travel = getIntent().getParcelableExtra(EXTRA_TRAVEL);
        initView();
        initListener();

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
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.url_btn:
                Intent intent = new Intent(InfoActivity.this,WebActivity.class);
                intent.putExtra("url",travel.getDoubanURL());
                startActivity(intent);
                break;

        }
    }
}
