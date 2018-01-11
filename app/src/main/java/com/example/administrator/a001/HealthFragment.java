package com.example.administrator.a001;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.a001.bean.HealthInfoRequestBean;
import com.example.administrator.a001.bean.HealthInfoResponseBean;
import com.example.administrator.a001.bean.HealthLevelBean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 健康页面
 * <p>
 * Created by Administrator on 2017/10/27.
 */

public class HealthFragment extends Fragment {


    private static final String TAG = "HealthFragment";

    private TextView BtnEdit;
    private TextView BtnEditOk;

    private EditText edtHeight;
    private EditText edtWeight;
    private EditText edtHeartRate;
    private EditText edtLung;
    private EditText edtBloodPresure;
    private EditText edtBloodSugar;
    private EditText edtTemperature;
    private EditText edtWalking;
    private LinearLayout totalLayout;
    private TextView tvTotal;
    private TextView tvTotalDesc;

    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int infoStatus = 100;

//    private String height;
//    private String weight;
//    private String heartRate;
//    private String Lung;
//    private String bloodPresure;
//    private String bloodSugar;

    private HealthInfoResponseBean healthInfoResponseBean = null;


    public static HealthFragment newInstance(String param1) {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
//        TextView tv = (TextView)view.findViewById(R.id.health_text);
//        tv.setText(agrs1);
        totalLayout = view.findViewById(R.id.total_layout);
        scrollView = view.findViewById(R.id.health_scrollView);
        swipeRefreshLayout = view.findViewById(R.id.health_refresh);

        //防止scrollView与swipeRefreshLayout冲突
//        if (scrollView != null) {
//            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                @Override
//                public void onScrollChanged() {
//                    if (swipeRefreshLayout != null) {
//                        swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
//                    }
//                }
//            });
//        }
        //下拉刷新
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        tvTotal = view.findViewById(R.id.health_total_tv);
        tvTotalDesc = view.findViewById(R.id.health_lv_desc);

        edtHeight = view.findViewById(R.id.et_height);
        edtWeight = view.findViewById(R.id.et_weight);
        edtHeartRate = view.findViewById(R.id.et_heart_rate);
        edtLung = view.findViewById(R.id.et_lung);
        edtBloodPresure = view.findViewById(R.id.et_blood_presure);
        edtBloodSugar = view.findViewById(R.id.et_blood_sugar);
        edtTemperature = view.findViewById(R.id.et_temperature);
        edtWalking = view.findViewById(R.id.et_walking);

        BtnEdit = view.findViewById(R.id.edit_btn);//修改按钮
        BtnEditOk = view.findViewById(R.id.edit_ok);//修改完成按钮

        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEdtEnable(true);
                BtnEditOk.setVisibility(View.VISIBLE);
            }
        });

        BtnEditOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //还需要发送请求
                setEdtEnable(false);
                BtnEditOk.setVisibility(View.GONE);
                HealthInfoRequestBean healthInfoRequestBean = new HealthInfoRequestBean();
                healthInfoRequestBean.height = edtHeight.getText().toString();
                healthInfoRequestBean.weight = edtWeight.getText().toString();
                healthInfoRequestBean.heartRate = edtHeartRate.getText().toString();
                healthInfoRequestBean.vitalCapacity = edtLung.getText().toString();
                setHealthInfoRequest(healthInfoRequestBean);
            }
        });

        getHealthInfoRequest(UserInfo.getUsername(), UserInfo.getToken());

        return view;
    }

    /**
     * 设置为可编辑
     */
    private void setEdtEnable(boolean isEnable) {
        edtHeight.setEnabled(isEnable);
        edtWeight.setEnabled(isEnable);
        edtHeartRate.setEnabled(isEnable);
        edtLung.setEnabled(isEnable);
        edtBloodPresure.setEnabled(isEnable);
        edtBloodSugar.setEnabled(isEnable);
        edtTemperature.setEnabled(isEnable);
        edtWalking.setEnabled(isEnable);

        edtHeight.setFocusable(isEnable);
        edtWeight.setFocusable(isEnable);
        edtHeartRate.setFocusable(isEnable);
        edtLung.setFocusable(isEnable);
        edtBloodPresure.setFocusable(isEnable);
        edtBloodSugar.setFocusable(isEnable);
        edtTemperature.setFocusable(isEnable);
        edtWalking.setFocusable(isEnable);

        edtHeight.setFocusableInTouchMode(isEnable);
        edtWeight.setFocusableInTouchMode(isEnable);
        edtHeartRate.setFocusableInTouchMode(isEnable);
        edtLung.setFocusableInTouchMode(isEnable);
        edtBloodPresure.setFocusableInTouchMode(isEnable);
        edtBloodSugar.setFocusableInTouchMode(isEnable);
        edtTemperature.setFocusableInTouchMode(isEnable);
        edtWalking.setFocusableInTouchMode(isEnable);

        if (isEnable) {
            edtHeight.requestFocus();
        }
    }


    /**
     * 发送健康信息请求
     */
    private void getHealthInfoRequest(final String username, final String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody1 = new FormBody.Builder()
                            .add("username", username)
                            .add("token", token)
//                            .add("weight", healthInfoRequestBean.getWeight())
//                            .add("height", healthInfoRequestBean.getHeight())
//                            .add("vitalCapacity", healthInfoRequestBean.getVitalCapacity())
//                            .add("heartRate", healthInfoRequestBean.getHeartRate())
                            .build();
                    Request request1 = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/behavior/getBasicInfo")
                            .post(requestBody1)
                            .build();
                    Request request2 = new Request.Builder()
                            .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/behavior/getHealthyLevel")
                            .post(requestBody1)
                            .build();
                    Response response1 = client.newCall(request1).execute();
                    Response response2 = client.newCall(request2).execute();
                    if (response1.isSuccessful()) {
                        getResponse(response1.body().string());
//                        statusMsg = getResponse(response.body().string()).getMsg();
                    }
                    if (response2.isSuccessful()) {
                        getLevelResponse(response2.body().string());
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
     * 设置健康信息请求
     */
    private void setHealthInfoRequest(final HealthInfoRequestBean healthInfoRequestBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Log.d("23333", "sendRegisterRequest:------------ .");
                    if (healthInfoRequestBean != null) {

                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("username", UserInfo.getUsername())
                                .add("token", UserInfo.getToken())
                                .add("weight", healthInfoRequestBean.weight)
                                .add("height", healthInfoRequestBean.height)
                                .add("vitalCapacity", healthInfoRequestBean.vitalCapacity)
                                .add("heartRate", healthInfoRequestBean.heartRate)
                                .build();
                        Request request = new Request.Builder()
                                .url("http://120.78.134.216/kajousekki/public/index.php?s=/interfaces/behavior/setBasicInfo")
                                .post(requestBody)
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            getEditResponse(response.body().string());
//                        statusMsg = getResponse(response.body().string()).getMsg();
                        }
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
     * 解析结果
     *
     * @param res 返回值json
     */
    private void getResponse(final String res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
//                 healthInfoResponseBean = null;
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
                    infoStatus = status;
//                    Log.d(TAG, "------------------status: " + status);
                    if (status == 1) {
                        JSONObject basicInfo = (JSONObject) Obj.get("basicInfo");
//                    JSONObject statusCode = (JSONObject) Obj.get("statusCode");
                        healthInfoResponseBean = JSON.parseObject(basicInfo.toJSONString(), HealthInfoResponseBean.class);
                        setHealthInfo(healthInfoResponseBean);
//                        Log.d(TAG, "---------healthInfoResponseBean: " + basicInfo.toJSONString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析修改后的结果
     *
     * @param res 返回值json
     */
    private void getEditResponse(final String res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
//                    Log.d(TAG, "------------------status: " + status);
                    if (status == 1) {
                        Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "---------healthInfoResponseBean: " + basicInfo.toJSONString());
                    } else {
                        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                        setHealthInfo(healthInfoResponseBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 解析健康等级结果
     *
     * @param res 返回值json
     */
    private void getLevelResponse(final String res) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "-----------------getResponse: " + res);
                try {
                    JSONObject Obj = JSON.parseObject(res);
                    int status = Obj.getIntValue("statusCode");
//                    Log.d(TAG, "------------------status: " + status);
                    if (status == 1) {
                        HealthLevelBean healthLevelBean = null;
                        JSONObject basicInfo = (JSONObject) Obj.get("healthyLevel");
//                    JSONObject statusCode = (JSONObject) Obj.get("statusCode");
                        healthLevelBean = JSON.parseObject(basicInfo.toJSONString(), HealthLevelBean.class);
                        setHealthLevelInfo(healthLevelBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 设置健康信息
     *
     * @param healthInfoResponseBean 健康信息bean
     */
    private void setHealthInfo(HealthInfoResponseBean healthInfoResponseBean) {

        if (healthInfoResponseBean != null) {
            edtHeight.setText(healthInfoResponseBean.getHeight());
            edtWeight.setText(healthInfoResponseBean.getWeight());
            edtHeartRate.setText(healthInfoResponseBean.getHeartRate());
            edtLung.setText(healthInfoResponseBean.getVitalCapacity());
        }
    }

    /**
     * 设置健康等级信息
     *
     * @param healthLevelBean 健康等级信息bean
     */
    private void setHealthLevelInfo(HealthLevelBean healthLevelBean) {

        if (healthLevelBean != null) {
            int totalLevel = healthLevelBean.getTotal();
            if (totalLevel >= 4) {
                tvTotal.setText("健康");
                tvTotalDesc.setText("您很健康，请继续保持~");
                totalLayout.setBackground(getActivity().getDrawable(R.drawable.health_info_bg_green));
            }
            if (totalLevel >= 2 && totalLevel < 4) {
                tvTotal.setText("亚健康");
                tvTotalDesc.setText("身体有点小毛病哦，请多加注意");
                totalLayout.setBackground(getActivity().getDrawable(R.drawable.health_info_bg_yellow));
            } else {
                tvTotal.setText("不健康");
                tvTotalDesc.setText("您有很大的健康问题，建议您及时就医！");
                totalLayout.setBackground(getActivity().getDrawable(R.drawable.health_info_bg_red));
            }
        }
    }

    private void refresh() {
        getHealthInfoRequest(UserInfo.getUsername(), UserInfo.getToken());
        swipeRefreshLayout.setRefreshing(false);

        if (infoStatus == 1) {
            Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
        }
    }

}



