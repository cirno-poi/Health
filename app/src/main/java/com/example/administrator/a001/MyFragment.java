package com.example.administrator.a001;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/27.
 */

public class MyFragment extends Fragment {
//    ListView lvMyList = null;

    private TextView tvUsername;
    private Button btnLogout;

    private TextView btnCalorie;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
//        TextView tv = (TextView) view.findViewById(R.id.my_info_title_tv);
//        tv.setText(agrs1);

        tvUsername = view.findViewById(R.id.my_username);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnCalorie = view.findViewById(R.id.tv_Calorie);

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

        return view;
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
