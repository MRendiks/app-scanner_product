package com.databarang.qrcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.databarang.qrcode.model.login.LoginData;

import java.util.HashMap;

public class SessionManager {
    private Context _context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String USERNAME = "username";
    public static final String NAMA = "nama";
    public static final String ROLE = "role";

    public SessionManager (Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(LoginData user){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(USERNAME, user.getUsername());
        editor.putString(NAMA, user.getNama());
        editor.putString(ROLE, user.getRole());
        editor.commit();
    }

    public HashMap<String,String> getUserDetail(){
        HashMap<String,String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME,null));
        user.put(NAMA, sharedPreferences.getString(NAMA,null));
        user.put(ROLE, sharedPreferences.getString(ROLE,null));
        return user;
    }

    public void logoutSession(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
