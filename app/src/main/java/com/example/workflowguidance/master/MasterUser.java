package com.example.workflowguidance.master;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.workflowguidance.LoginActivity;
import com.example.workflowguidance.MainActivity;
import com.example.workflowguidance.MasterDataFragment;
import com.example.workflowguidance.R;

public class MasterUser extends AppCompatActivity {

    private Toolbar mToolbar;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_user);
        init();
    }

    public void init(){

        context = getApplicationContext();

        mToolbar =  findViewById(R.id.user_toolbar);
        this.setSupportActionBar(mToolbar);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackAction());
    }

    void onBackAction() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
