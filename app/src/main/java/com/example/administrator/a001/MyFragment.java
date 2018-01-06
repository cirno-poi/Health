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

/**
 * Created by Administrator on 2017/10/27.
 */

public class MyFragment extends Fragment {
    Button btnLogout = null;
    ListView lvMyList = null;


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
        TextView tv = (TextView)view.findViewById(R.id.my_info_title_tv);
        tv.setText(agrs1);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        btnLogout = (Button) getActivity().findViewById(R.id.btnLogout);
        lvMyList = (ListView) getActivity().findViewById(R.id.lvMyList);

        // 每行布局(系统自带)
        // android.R.layout.simple_list_item_1

        // 数据
        String[] data = { "我的联系人", "我的账户", "我的密码"};
        // String[] data = getResourcesmm().getStringArray(R.array.my_list_data);

        // 适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_list_item_1, data);

        // 绑定
        lvMyList.setAdapter(adapter);

        // 事件处理
        lvMyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();

                //暂定Login界面
                switch (position) {
                    case 0:
                        intent.setClass(getActivity(), LoginActivity.class);
                        break;
                    case 1:
                        intent.setClass(getActivity(), LoginActivity.class);
                        break;
                    case 2:
                        intent.setClass(getActivity(), LoginActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });

        MyButtonListener listener = new MyButtonListener();
        btnLogout.setOnClickListener(listener);
    }

    class MyButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // 区分按钮
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }
}
