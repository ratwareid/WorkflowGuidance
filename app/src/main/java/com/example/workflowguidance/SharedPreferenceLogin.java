package com.example.workflowguidance;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceLogin {

    public static final String SP_USER = "spUser";

    public static final String SP_EMAIL = "spEmail";
    public static final String SP_NAME = "spName";

    public static final String ON_LOGIN = "spLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPreferenceLogin(Context context){
        sp = context.getSharedPreferences(SP_USER,Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpEmail() {
        return sp.getString(SP_EMAIL,"");
    }

    public String getSpName() {
        return sp.getString(SP_NAME,"");
    }

    public boolean getOnLogin() {
        return sp.getBoolean(ON_LOGIN,false);
    }
}
