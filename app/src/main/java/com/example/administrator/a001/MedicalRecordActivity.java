package com.example.administrator.a001;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.MedicalRecordBean;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MedicalRecordActivity extends AppCompatActivity {


    private SimpleDraweeView imgRecord1;
    private SimpleDraweeView imgRecord2;
    private SimpleDraweeView imgRecord3;
    private SimpleDraweeView imgRecord4;

    private TextView tvFirstTip;

    private Uri imgUri;

    private static final int TAKE_RECORD_PHOTO = 1;

    private SwipeRefreshLayout swipeRefreshLayout;



    /**
     * 页面跳转方法
     *
     * @param context 源页面context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MedicalRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.record_toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = findViewById(R.id.my_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        imgRecord1 = findViewById(R.id.record_1);
        imgRecord2 = findViewById(R.id.record_2);
        imgRecord3 = findViewById(R.id.record_3);
        imgRecord4 = findViewById(R.id.record_4);

        tvFirstTip = findViewById(R.id.tip_tv);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        FloatingActionButton fab = findViewById(R.id.record_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用相机
                //发送请求

                File outputImg = new File(getExternalCacheDir(), "record_image.jpg");
                try {
                    if (outputImg.exists()) {
                        outputImg.delete();
                    }
                    outputImg.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imgUri = FileProvider.getUriForFile(MedicalRecordActivity.this, ".fileprovider", outputImg);
                }

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(intent, TAKE_RECORD_PHOTO);
            }
        });
        getMedicalRecord();
    }


    private void refresh() {
        getMedicalRecord();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_RECORD_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
//                        imgRecord1.setImageBitmap(bitmap);
//                        upImage(getActivity().getExternalCacheDir() + "/output_image.jpg");
                        uploadImage(getExternalCacheDir() + "/record_image.jpg");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传图片请求
     *
     * @param imagePath 图片路径
     */
    private void uploadImage(final String imagePath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient mOkHttpClent = new OkHttpClient();
                    File file = new File(imagePath);
                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("username", UserInfo.getUsername())
                            .addFormDataPart("token", UserInfo.getToken())
                            .addFormDataPart("image", "record_image.jpg",
                                    RequestBody.create(MediaType.parse("image/*"), file));

                    RequestBody requestBody = builder.build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/file/uploadmedicalrecord")
                            .post(requestBody)
                            .build();
                    Response response = mOkHttpClent.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.d("MedicalRecordActivity", "uploadImage:-----------------------------> " + data);
                        getUploadResponse(data);
                    }
//                    Log.d("233333", "=------------>upImage: " + data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 获取上传病历结果返回值
     */
    private void getUploadResponse(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
//                 healthInfoResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
                    if (status == 1) {
                        Toast.makeText(MedicalRecordActivity.this, "上传成功，请刷新", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MedicalRecordActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取病历请求
     */
    private void getMedicalRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", UserInfo.getUsername())
                            .add("token", UserInfo.getToken())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/behavior/getMedicalRecord")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.d("233333", "----------getMedicalRecord: " + data);
                        getMedicalRecordRes(data);
//                        statusMsg = getResponse(response.body().string()).getMsg();
                    }
//                    String responseDate = JSON.toJSONString(response.body());
//                    Log.d("23333", "responseDate:------------ ." + JSON.toJSONString(registerResponseBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析病历返回值
     *
     * @param res
     */
    private void getMedicalRecordRes(final String res) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
//                 healthInfoResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
                    if (status == 1) {
                        List<String> urlList = (List<String>) Obj.get("medicalRecord");
                        Log.d("23333", "medicalRecord:------------------->" + urlList);
                        if (urlList.isEmpty()) {
                            Toast.makeText(MedicalRecordActivity.this, "请上传病历", Toast.LENGTH_SHORT).show();
                            tvFirstTip.setVisibility(View.VISIBLE);
                        } else {
                            ImagePipeline imagePipeline = Fresco.getImagePipeline();
                            imagePipeline.clearMemoryCaches();
                            imagePipeline.clearDiskCaches();

                            // combines above two lines
                            imagePipeline.clearCaches();

                            int size = urlList.size();

                            if (size == 1) {
                                imgRecord1.setImageURI(Uri.parse(urlList.get(0)));
                            } else if (size == 2) {
                                imgRecord1.setImageURI(Uri.parse(urlList.get(0)));
                                imgRecord2.setImageURI(Uri.parse(urlList.get(1)));
                            } else if (size == 3) {
                                imgRecord1.setImageURI(Uri.parse(urlList.get(0)));
                                imgRecord2.setImageURI(Uri.parse(urlList.get(1)));
                                imgRecord3.setImageURI(Uri.parse(urlList.get(2)));
                            } else if (size >= 4) {
                                imgRecord1.setImageURI(Uri.parse(urlList.get(0)));
                                imgRecord2.setImageURI(Uri.parse(urlList.get(1)));
                                imgRecord3.setImageURI(Uri.parse(urlList.get(2)));
                                imgRecord4.setImageURI(Uri.parse(urlList.get(3)));
                            }
                        }


//                        imgRecord1.setImageURI(Uri.parse());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
