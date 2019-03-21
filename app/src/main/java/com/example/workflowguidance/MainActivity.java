package com.example.workflowguidance;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.workflowguidance.api.SharedPreferenceManager;
import com.example.workflowguidance.api.spkey.SPUserDataKey;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TextView tvName,tvEmail;
    private NavigationView navigationView;
    private SharedPreferenceManager spManager;
    private String username,email,companyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); //Menghilangkan title

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getuserdata();
        frag_company();
        preventkeyboard();
        checkpermission();
    }

    public void checkpermission(){

        PermissionManager pm = new PermissionManager();
        boolean allowPM = pm.checkPermissionForReadExtertalStorage(this);
        if (!allowPM){
            pm.requestPermissionForReadExtertalStorage(this);
        }
    }

    public void preventkeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void frag_company(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CompanyFragment fragmentcompany = new CompanyFragment();
        ft.replace(R.id.FrameFragment, fragmentcompany);
        ft.commit();
        getSupportActionBar().setTitle("Company Profile");
    }

    public void frag_master(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MasterDataFragment fragmentmaster = new MasterDataFragment();
        ft.replace(R.id.FrameFragment, fragmentmaster);
        ft.commit();
        getSupportActionBar().setTitle("Master Data");
    }

    public void getuserdata(){

        if (username == null && email == null && companyID == null){
            spManager = new SharedPreferenceManager(this);

            username = spManager.getSp().getString(SPUserDataKey.Username,"");
            email   = spManager.getSp().getString(SPUserDataKey.Email,"");
            companyID = spManager.getSp().getString(SPUserDataKey.CompanyID,"");

        }
        View view = navigationView.getHeaderView(0);

        tvName = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);
        if (tvName != null && tvEmail != null) {
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email)) {
                tvName.setText(username);
                tvEmail.setText(email);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            doLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_company) {
            frag_company();
        } else if (id == R.id.nav_master) {
            frag_master();
        } else if (id == R.id.nav_workorder) {

        } else if (id == R.id.nav_report) {

        } else if (id == R.id.nav_premium) {

        } else if (id == R.id.nav_support) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void doLogout() {
        spManager.clearData();
        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
