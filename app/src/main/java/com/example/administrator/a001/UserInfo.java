package com.example.administrator.a001;

import android.content.SharedPreferences;

/**
 * 用于存放用户信息
 *
 * Created by Cirno ⑨ on 2018/1/10 0010.
 */

public class UserInfo {

    private static String username;
    private static String token;


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserInfo.token = token;
    }
}
