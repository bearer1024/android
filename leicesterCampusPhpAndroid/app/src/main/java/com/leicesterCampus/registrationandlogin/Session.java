package com.leicesterCampus.registrationandlogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class Session {
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    Context context;

    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "id";
    public static final String KEY_MAIL = "email";
    private static final String PREF_NAME = "AndroidPHP";

    public Session(Context context) {
//        sp = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
        sp = this.context.getSharedPreferences(PREF_NAME,0);
        spEditor = sp.edit();
    }

    public void createLoginSession(String userId,String email){

//        spEditor.putString(KEY_NAME,username);
        spEditor.putString(KEY_ID,userId);
        spEditor.putString(KEY_MAIL,email);
        spEditor.commit();
    }

    public HashMap<String,String> getUserDetails(){
        HashMap<String,String> user = new HashMap<>();
        user.put(KEY_ID,sp.getString(KEY_ID,null));
        user.put(KEY_MAIL,sp.getString(KEY_MAIL,null));
//        user.put(KEY_NAME,sp.getString(KEY_NAME,null));
        return user;
    }

    public boolean setLogin(boolean status) {
        spEditor.putBoolean("is_logged_in", status);
        spEditor.commit();
        return true;
    }


    public boolean getLoggedIn() {
        return sp.getBoolean("is_logged_in", false);
    }
}
