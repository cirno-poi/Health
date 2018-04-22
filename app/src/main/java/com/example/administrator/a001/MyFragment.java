package com.example.administrator.a001;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MyFragment extends Fragment {
//    ListView lvMyList = null;

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOITO = 2;

    private TextView tvUsername;
    private Button btnLogout;

    private TextView tvSetNewPwd;
    private TextView btnCalorie;
    private TextView btnMyRecord;




    //    private ImageView userIcon;
    private SimpleDraweeView userIcon;

    private SwipeRefreshLayout swipeRefreshLayout;


    private Uri imgUri;

    private String uploadImagePath = "";

    public static MyFragment newInstance(String param1) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
//        TextView tv = (TextView) view.findViewById(R.id.my_info_title_tv);
//        tv.setText(agrs1);

        swipeRefreshLayout = view.findViewById(R.id.my_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        tvUsername = view.findViewById(R.id.my_username);
        tvSetNewPwd = view.findViewById(R.id.change_psw_tv);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnCalorie = view.findViewById(R.id.tv_Calorie);
        userIcon = view.findViewById(R.id.user_icon);

        btnMyRecord = view.findViewById(R.id.my_record_tv);
        btnMyRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MedicalRecordActivity.actionStart(getActivity());
            }
        });

        tvSetNewPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetActivity.actionStart((getActivity()));
            }
        });
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("上传头像");
                builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
////                        toast("取消");
                        File outputImg = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImg.exists()) {
                                outputImg.delete();
                            }
                            outputImg.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24) {
                            imgUri = FileProvider.getUriForFile(getActivity(), ".fileprovider", outputImg);
                        }

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }
                });
                builder.setPositiveButton("从手机相册选择", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        toast("确定");
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                        } else {
                            openAlbum();//打开相册
                        }


                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLogoutRequest(UserInfo.getUsername(), UserInfo.getToken());
                LoginActivity.actionStart(getActivity());
                getActivity().finish();
            }
        });

        btnCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalorieActivity.actionStart(getActivity());
            }
        });

        tvUsername.setText(UserInfo.getUsername());

        getUserIcon();

        return view;
    }

    /**
     * 刷新
     */
    private void refresh() {
        getUserIcon();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 上传图片请求
     *
     * @param imagePath 图片路径
     */
    private void upImage(final String imagePath) {

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
                            .addFormDataPart("image", "output_image.jpg",
                                    RequestBody.create(MediaType.parse("image/*"), file));

                    RequestBody requestBody = builder.build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/file/setProfilePicture")
                            .post(requestBody)
                            .build();
                    Response response = mOkHttpClent.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.d("23333", "upImageRes:-----------------------------> " + data);
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
     * 获取头像请求
     */
    private void getUserIcon() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", UserInfo.getUsername())
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/file/getProfilePicture")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        getUserIconResponse(data);
                        Log.d("233333", "----------response: " + data);
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
     * 获取上传头像结果返回值
     */
    private void getUploadResponse(final String res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
//                 healthInfoResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
                    if (status == 1) {
                        Toast.makeText(getActivity(), "上传成功，请刷新", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "上传失败！", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取用户头像返回值
     *
     * @param res
     */
    private void getUserIconResponse(final String res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
//                 healthInfoResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
                    Log.d("233333", "status: --------------------->" + status);
                    if (status == 1) {
                        String iconUrl = Obj.getString("url");
                        Log.d("23333", "iconUrl:------------------->" + iconUrl);
                        Uri imageUri = Uri.parse(iconUrl);

                        ImagePipeline imagePipeline = Fresco.getImagePipeline();
                        imagePipeline.clearMemoryCaches();
                        imagePipeline.clearDiskCaches();

                        // combines above two lines
                        imagePipeline.clearCaches();

                        userIcon.setImageURI(Uri.parse(iconUrl));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOITO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "未获取查看相册权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().
                                getContentResolver().openInputStream(imgUri));
//                        userIcon.setImageBitmap(bitmap);
                        upImage(getActivity().getExternalCacheDir() + "/output_image.jpg");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOITO:
                handleImage(data);
                upImage(uploadImagePath);
                break;
            default:
                break;
        }
    }

    private void handleImage(Intent data) {
        if (data != null) {
            String imagePath = null;
            Uri uri = data.getData();
            if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
                //如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];//解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.download.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId
                            (Uri.parse("content://downloads//public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                    //如果是content类型的uri，则使用普通方式处理
                    imagePath = getImagePath(uri, null);
                } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                    //如果是file类型的uri，直接获取图片路径即可
                    imagePath = uri.getPath();
                }
                displayImage(imagePath);//根据图片路径显示图片
                uploadImagePath = imagePath;
            }
        }
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            userIcon.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "获取图片失败！", Toast.LENGTH_SHORT).show();
        }

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 发送登出请求
     *
     * @param username 用户名
     * @param password 密码
     */
    private void sendLogoutRequest(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", username)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/user/logou")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();

//                    String responseDate = JSON.toJSONString(response.body());
//                    Log.d("23333", "responseDate:------------ ." + JSON.toJSONString(registerResponseBean));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
