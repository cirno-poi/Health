package com.example.administrator.a001;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/10/27.
 */

public class SearchFragment extends Fragment implements View.OnClickListener {

    private EditText editText_search = null;
    private Button btnSearch = null;

    private Button quickbtn1 = null;
    private Button quickbtn2 = null;
    private Button quickbtn3 = null;
    private Button quickbtn4 = null;
    private Button quickbtn5 = null;
    private Button quickbtn6 = null;
    private Button quickbtn7 = null;
    private Button quickbtn8 = null;

    private String url = "";


    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        TextView tv = (TextView) view.findViewById(R.id.lucky_title_tv);
        tv.setText(agrs1);

        editText_search = (EditText) view.findViewById(R.id.editText_search);
        btnSearch = (Button) view.findViewById(R.id.serchbutton);

        quickbtn1 = (Button) view.findViewById(R.id.quickbtn1);
        quickbtn2 = (Button) view.findViewById(R.id.quickbtn2);
        quickbtn3 = (Button) view.findViewById(R.id.quickbtn3);
        quickbtn4 = (Button) view.findViewById(R.id.quickbtn4);
        quickbtn5 = (Button) view.findViewById(R.id.quickbtn5);
        quickbtn6 = (Button) view.findViewById(R.id.quickbtn6);
        quickbtn7 = (Button) view.findViewById(R.id.quickbtn7);
        quickbtn8 = (Button) view.findViewById(R.id.quickbtn8);

        quickbtn1.setOnClickListener(this);
        quickbtn2.setOnClickListener(this);
        quickbtn3.setOnClickListener(this);
        quickbtn4.setOnClickListener(this);
        quickbtn5.setOnClickListener(this);
        quickbtn6.setOnClickListener(this);
        quickbtn7.setOnClickListener(this);
        quickbtn8.setOnClickListener(this);

        btnSearch.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quickbtn1:
                url = "https://zhidao.baidu.com/question/40142824.html?fr=iks&word=%B7%E7%BA%AE&ie=gbk";
                break;
            case R.id.quickbtn2:
                url = "https://zhidao.baidu.com/question/1987931117300953747.html?fr=iks&word=%B7%E7%C8%C8&ie=gbk";
                break;
            case R.id.quickbtn3:
                url = "http://tag.120ask.com/zhengzhuang/fare/";
                break;
            case R.id.quickbtn4:
                url = "http://www.120ask.com/dept/xiaochuan/";
                break;
            case R.id.quickbtn5:
                url = "http://baike.120ask.com/art/6744";
                break;
            case R.id.quickbtn6:
                url = "https://zhidao.baidu.com/question/981974631378114659.html";
                break;
            case R.id.quickbtn7:
                url = "http://tag.120ask.com/jibing/biyan/";
                break;
            case R.id.quickbtn8:
                url = "http://www.120ask.com/dept/pfgm/";
                break;
            case R.id.serchbutton:
                url = "https://zhidao.baidu.com/search?word="+editText_search.getText().toString();
                break;
            default:
                url = "http://www.baidu.com";
                break;
        }
        Intent intent = new Intent(getActivity(), WebviewActivity.class);
        intent.putExtra("str", url);
        startActivity(intent);

    }
}
