package com.cat.activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cat.R;
import com.cat.fileprovider.PhotoUtils;
import com.cat.view.BlurImageview;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;
import com.wx.wheelview.common.WheelData;
import com.wx.wheelview.widget.WheelView;
import com.wx.wheelview.widget.WheelViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import dmax.dialog.SpotsDialog;

import static com.cat.activity.MainActivity.finishMain;


/**
 * Created by 汤汤 on 2017/3/28.
 */

public class PersonDataActivity extends AppCompatActivity implements View.OnClickListener {

    //声明自有变量
    private Toolbar title;
    private ImageView personal_icon;
    private LinearLayout linearLayout;
    private LinearLayout slinerlayout;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private TextView textView;
    private WheelView wheelView;
    private WheelViewDialog wdialog;
    private TextView gtextview;
    private String value;
    private TextView phonetext;
    private TextView storetext;
    private TextView uptext;
    private Button exit;
    Dialog dialog;
    //定义本地存储
    SharedPreferences preferences;
    //文件
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/pictures/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/pictures/crop_photo.jpg");
    private Uri cropImageUri;

    //服务器相关
    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/bookstore/restful/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //状态栏颜色一致
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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

        setContentView(R.layout.person_data);
        initview();
        initdata();
    }

    private void initview() {






       /* title.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
        /*初始化各个控件*/
        title = (Toolbar) findViewById(R.id.toolbar2);
        asyncHttpClient = new AsyncHttpClient();
        dialog = new SpotsDialog(this);
        textView = (TextView) findViewById(R.id.username);
        slinerlayout = (LinearLayout) findViewById(R.id.sex_linear);
        gtextview = (TextView) findViewById(R.id.textView5);
        phonetext = (TextView) findViewById(R.id.textView8);
        storetext = (TextView) findViewById(R.id.textView9);
        uptext = (TextView) findViewById(R.id.uptextView);
        exit = (Button) findViewById(R.id.exitbutton);
        exit.setOnClickListener(this);
        //初始化ToolBar
        title.setTitle("");
        setSupportActionBar(title);
        title.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*从person_icon中取出bitmap图片获取权限*/
//        //Bitmap bitmap = Bitmap.createBitmap(personal_icon.getDrawingCache());
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.weather);
//        mBlurImage.setImageBitmap(BlurImageview.getBlurBitmap(mBlurImage.getContext(),bitmap,15));
        /*头像点击*/
        personal_icon = (ImageView) findViewById(R.id.person_image);
        personal_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });

        /*跳转到昵称编辑界面*/
        linearLayout = (LinearLayout) findViewById(R.id.name_linear);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonDataActivity.this,EditNameActivity.class);
                startActivity(intent);
            }
        });

        /*跳转到性别编辑界面*/
        slinerlayout = (LinearLayout) findViewById(R.id.sex_linear);
        slinerlayout.setOnClickListener(this);



    }

    private void initdata()
    {
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        String p = preferences.getString("username","");
        textView.setText(p);
        uptext.setText(p);

        String g = preferences.getString("gender","");
        if (g.equals("null"))
            gtextview.setText("男");
        else {
            gtextview.setText(g);
        }

        String headpic = preferences.getString("headpic","");
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                personal_icon.setImageBitmap(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        };

        Picasso.with(PersonDataActivity.this).load(headpic).placeholder(R.drawable.default_image).into(target);

        String phone = preferences.getString("phone","");
        phonetext.setText(phone);

        String store = preferences.getString("score","");
        storetext.setText(store);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    /*显示选择头像对话框*/

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置你的头像");
        String[] items = {"从图库中选择照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog,int which){
                switch (which) {
                    case CHOOSE_PICTURE: // 从相册选择照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        tempUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            //通过FileProvider创建一个content类型的Uri
                            tempUri = FileProvider.getUriForFile(PersonDataActivity.this, "com.cat.fileprovider", fileUri);
                        PhotoUtils.takePicture(PersonDataActivity.this, tempUri, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    //对图像进行裁剪处理
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, tempUri, cropImageUri, 1, 1, output_X, output_Y, CROP_SMALL_PICTURE);
                    break;
                case CHOOSE_PICTURE://访问相册完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        newUri = FileProvider.getUriForFile(this, "com.cat.fileprovider", new File(newUri.getPath()));
                    PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CROP_SMALL_PICTURE);
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        dialog.show();
                        Bitmap headpic = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                        try {
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileCropUri));
                            headpic.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            bos.flush();
                            bos.close();

                            RequestParams rp = new RequestParams();
                            rp.put("userid",preferences.getString("userid",""));
                            rp.put("headpic",fileCropUri);
                            asyncHttpClient.post(BASEURL+"user/updateheadpic",rp,new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(JSONObject response) {
                                    super.onSuccess(response);
                                    String retcode = null;
                                    try {
                                        retcode = response.getString("retcode");
                                        String errMsg = response.getString("errorMsg");
                                        String obj = response.getString("obj");
                                        if (!retcode.equals("0000")) {
                                            Toast.makeText(PersonDataActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(PersonDataActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
                                            setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                                            preferences.edit().putString("headpic",obj).apply();
                                            dialog.dismiss();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable e, JSONObject errorResponse) {
                                    super.onFailure(e, errorResponse);
                                    Toast.makeText(PersonDataActivity.this, "服务器出错！", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "文件转换出错！", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "文件IO出错！", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                    break;
            }
        }
    }


    //    显示图片以及设置背景
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            personal_icon.setImageBitmap(photo);
        }
    }

    /*设置dialog数据*/
    private ArrayList<WheelData> createDatas() {
        ArrayList<WheelData> list = new ArrayList<WheelData>();
        WheelData man;
        WheelData woman;
        man = new WheelData();
        woman = new WheelData();
        man.setId(R.drawable.man);
        woman.setId(R.drawable.woman);
        man.setName("男");
        woman.setName("女");
        list.add(man);
        list.add(woman);
        return list;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sex_linear:
                wdialog = new WheelViewDialog(this);
                wdialog.setTitle("性别").setItems(createDatas()).setButtonText("确定").setDialogStyle(Color
                        .parseColor("#6699ff")).setCount(3).show().setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object s) {
                        WheelData w = (WheelData) s;
                        String g = w.getName();
                        dialog.show();
                        RequestParams rp = new RequestParams();
                        String uid;
                        uid = preferences.getString("userid","");
                        rp.put("id",uid);
                        rp.put("gender",g);
                        asyncHttpClient.post(BASEURL + "user/updategender",rp,new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(JSONObject response) {
                                super.onSuccess(response);
                                try{
                                    String retcode = response.getString("retcode");
                                    String errormsg = response.getString("errorMsg");
                                    String obj = response.getString("obj");
                                    if(!retcode.equals("0000")) {
                                        Toast.makeText(PersonDataActivity.this, errormsg, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                    else{
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("gender",obj);
                                        editor.commit();
                                        dialog.dismiss();
                                        gtextview.setText(obj);
                                        Toast.makeText(PersonDataActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e){
                                    dialog.dismiss();
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Throwable e, JSONObject errorResponse) {
                                super.onFailure(e, errorResponse);
                                dialog.dismiss();
                                Toast.makeText(PersonDataActivity.this, "修改失败，请检查网络！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            case R.id.exitbutton:
                //保留token
                String token = preferences.getString("devicetoken","");
                boolean isGetToken = preferences.getBoolean("isGetToken",false);
                preferences.edit().clear().apply();
                preferences.edit().putString("devicetoken",token).apply();
                preferences.edit().putBoolean("isGetToken",isGetToken).apply();

                //跳转Login
                Intent intent = new Intent(PersonDataActivity.this, LoginActivity.class);
                startActivity(intent);
                finishMain();
                finish();
                break;
        }
    }

}