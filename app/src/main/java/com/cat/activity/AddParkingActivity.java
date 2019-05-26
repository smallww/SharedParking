package com.cat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.R;
import com.cat.fileprovider.PhotoUtils;
import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.JsonHttpResponseHandler;
import com.ta.util.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.github.xudaojie.qrcodelib.zxing.decoding.Intents.SearchBookContents.ISBN;

public class AddParkingActivity extends AppCompatActivity {

    private TextView add_text;
    private ImageView imageView;
    private Toolbar toolbar;
    private TextView text_latitude;
    private TextView text_Longitude;
    private Button btn_submit;
    private EditText address_full;

    //网络请求相关
    @TAInject
    private TASyncHttpClient syncHttpClient;
    @TAInject
    private AsyncHttpClient asyncHttpClient;
    final String BASEURL = "http://192.168.199.206:8080/share/restful/";
    //共享变量
    private SharedPreferences sharedPreferences;

    private String userId;
    private EditText SpaceId;
    private EditText publisher;
    private EditText phoneNum;
    private EditText comment;
    private Button btn_upload;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    //文件
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/pictures/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/pictures/crop_photo.jpg");
    private Uri cropImageUri;

    private android.app.AlertDialog dialog;
    private boolean checked=false;
    private String s1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_add_parking);

        initView();
        Intent intent=getIntent();
        //获取地址信息
        String addressTitle=intent.getStringExtra("address");
        add_text.setText(addressTitle);
        //获取经纬度信息
        String latitude=intent.getStringExtra("latitude");
        String longitude=intent.getStringExtra("longitude");
        text_latitude.setText(latitude);
        text_Longitude.setText(longitude);


        s1 = intent.getStringExtra("judge");
        Log.i("666",s1+"    56566565665");
    }

    private void initView() {

        /*初始化各个控件*/
        toolbar = (Toolbar) findViewById(R.id.toolbar_add);
        add_text= (TextView) findViewById(R.id.add_text);
        imageView= (ImageView) findViewById(R.id.location);
        text_latitude= (TextView) findViewById(R.id.text_latitude);
        text_Longitude=(TextView) findViewById(R.id.text_Longitude);
        btn_submit= (Button) findViewById(R.id.btn_submit);

        address_full=(EditText)findViewById(R.id.address_full);
        SpaceId=(EditText)findViewById(R.id.SpaceId);
        publisher=(EditText)findViewById(R.id.publisher);
        phoneNum=(EditText)findViewById(R.id.phoneNum);
        comment=(EditText)findViewById(R.id.comment);
        btn_upload=(Button) findViewById(R.id.btn_upload);

        setSupportActionBar(toolbar);
        //
        toolbar.setNavigationIcon(R.drawable.return_btn);//设置返回icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddParkingActivity.this,MySpaceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddParkingActivity.this,HomeActivity.class);
                intent.putExtra("judge",s1);
                startActivity(intent);

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(("").equals(add_text.getText().toString())){
                    Toast.makeText(AddParkingActivity.this, "您还选择您的地址，记得选择哦", Toast.LENGTH_SHORT).show();
                }else if(("").equals(address_full.getText().toString())){
                    Toast.makeText(AddParkingActivity.this, "您还没有填写您的详细地址，记得填写哦", Toast.LENGTH_SHORT).show();
                }else if(("").equals(SpaceId.getText().toString())){
                Toast.makeText(AddParkingActivity.this, "您还没填写您的车位编号，记得填写哦", Toast.LENGTH_SHORT).show();
                }else if(("").equals(publisher.getText().toString())){
                    Toast.makeText(AddParkingActivity.this, "您还没填写您姓名，记得填写哦", Toast.LENGTH_SHORT).show();
                }else if(phoneNum.getText().toString().length()!=11){
                    Toast.makeText(AddParkingActivity.this, "您的联系电话格式不对，不能为空，且要11位哦", Toast.LENGTH_SHORT).show();
                }else if(!checked) {
                    Toast.makeText(AddParkingActivity.this, "您还没上传车位图片，记得上传哦", Toast.LENGTH_SHORT).show();
                }
                else {
                    AddMethod();
                }

            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked=true;
                showChoosePicDialog();
            }
        });

    }
    private void AddMethod() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        final String releaseTime = formatter.format(time);
        sharedPreferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", null);
        new AlertDialog.Builder(this).setTitle("友情提示").setMessage("确定上报车位吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams rp = new RequestParams();
                        rp.put("userid",userId);
                        rp.put("spaceNum",SpaceId.getText().toString().trim());
                        rp.put("spacePic" , String.valueOf(cropImageUri));
                        rp.put("spaceRemark", String.valueOf(comment.getText()).trim());
                        rp.put("ownerName", String.valueOf(publisher.getText()));
                        rp.put("contactNum", String.valueOf(phoneNum.getText()));
                        rp.put("releaseTime",releaseTime);
                        rp.put("address",String.valueOf(add_text.getText()));
                        rp.put("addressFull",String.valueOf(address_full.getText()));
                        rp.put("latitude",String.valueOf(text_latitude.getText()));
                        rp.put("longtitude",String.valueOf(text_Longitude.getText()));
                        String s  = BASEURL + "space/addSpace";
                        asyncHttpClient = new AsyncHttpClient();
                        asyncHttpClient.post(s,rp,new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                Toast.makeText(AddParkingActivity.this, "车位上架成功！啦啦啦", Toast.LENGTH_SHORT).show();

                                if("0".equals(s1)) {
                                    Log.i("666","sfygsdjfsjdfhjk");
                                    Intent intent = new Intent(AddParkingActivity.this, MySpaceActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else if("1".equals(s1)){
                                    Intent intent = new Intent(AddParkingActivity.this, IssueSpaceActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                            @Override
                            public void onFailure(Throwable error) {
                                Toast.makeText(AddParkingActivity.this, "哎呀呀，出了点问题呢...", Toast.LENGTH_SHORT).show();
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
                            tempUri = FileProvider.getUriForFile(AddParkingActivity.this, "com.cat.fileprovider", fileUri);
                        PhotoUtils.takePicture(AddParkingActivity.this, tempUri, TAKE_PICTURE);
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
                    break;
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("from",s1);
        Log.i("666",s1+"111111111");
    }
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        // 总是调用超类，以便它可以恢复视图层次超级
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // 从已保存的实例中恢复状态成员
//        s1=savedInstanceState.getString("from");
//        Log.i("666",s1+"222222222222222222");
//
//    }
}
