package com.example.workflowguidance.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.workflowguidance.R;

import java.util.Date;

public class SharedPreferenceManager {

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    private static final String SP_NAME = "SP_RATWAREID";


    public SharedPreferenceManager(Context context){
        sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void clearData(){
        spEditor.clear();
        spEditor.commit();
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


    public SharedPreferences getSp() {
        return sp;
    }

    public void setSp(SharedPreferences sp) {
        this.sp = sp;
    }
}
