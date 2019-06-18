package com.example.workflowguidance.master;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.CallSuper;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;

import com.example.workflowguidance.LoginActivity;
import com.example.workflowguidance.MainActivity;
import com.example.workflowguidance.MasterDataFragment;
import com.example.workflowguidance.R;
import com.example.workflowguidance.adapter.MasterDataAdapter;
import com.example.workflowguidance.adapter.MasterUserAdapter;
import com.example.workflowguidance.model.UserData;

import java.util.ArrayList;

public class MasterUser extends AppCompatActivity {

    private Toolbar mToolbar;
    private Context context;
    private ArrayList<UserData> mUserData;
    private MasterUserAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_user);
        init();
        preventkeyboard();
    }



    public void init(){

        context = getApplicationContext();

        mToolbar =  findViewById(R.id.user_toolbar);
        this.setSupportActionBar(mToolbar);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackAction());

        mUserData =  new ArrayList<>();
        MainActivity activity = new MainActivity();
        mAdapter = new MasterUserAdapter(this.getApplicationContext(), mUserData, activity);

        mRecyclerView = this.findViewById(R.id.recycler_user);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getApplicationContext(),1));
        mRecyclerView.setAdapter(mAdapter);

        initiliazeData();
    }

    void onBackAction() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void initiliazeData(){

        String[] username = getResources().getStringArray(R.array.username);
        String[] userdiv = getResources().getStringArray(R.array.userdiv);
        String[] userpos = getResources().getStringArray(R.array.userpos);
        TypedArray userimg = getResources().obtainTypedArray(R.array.userpict);

        if (mUserData != null) {
            //Clear the existing data (to avoid duplication)
            mUserData.clear();
        }

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i=0;i<username.length;i++){
            UserData mData = new UserData();
            mData.setName(username[i]);
            mData.setPositionName(userpos[i]);
            mData.setDivisionName(userdiv[i]);
            mData.setImgBmp(userimg.getResourceId(i,0));
            mUserData.add(mData);
        }
        userimg.recycle();
        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }

    public void preventkeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
