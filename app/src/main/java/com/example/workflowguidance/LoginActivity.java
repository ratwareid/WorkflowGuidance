package com.example.workflowguidance;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workflowguidance.api.ApiService;
import com.example.workflowguidance.api.ApiUrl;
import com.example.workflowguidance.api.module.UserModuleApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {

    private EditText eTEmail,eTPassword;
    private String email,pass;
    private SharedPreferenceLogin sharedPreferenceLogin;
    private ProgressBar progressBar;

    public static final String EXTRA_USERNAME = "com.example.android.workflowguidance.extra.USERNAME";
    public static final String EXTRA_EMAIL = "com.example.android.workflowguidance.extra.EMAIL";
    public static final String EXTRA_ADDRESS = "com.example.android.workflowguidance.extra.ADDRESS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTEmail = findViewById(R.id.id_email_login);
        eTPassword = findViewById(R.id.id_password_login);

        sharedPreferenceLogin = new SharedPreferenceLogin(this);
        if (sharedPreferenceLogin.getOnLogin()){
            Intent intent = new Intent (LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //Set Full Screen Activity
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void doSignIn(View view){

        boolean error = checkAllData();

        if (!error) {
            email = eTEmail.getText().toString();
            pass = eTPassword.getText().toString();
            sendDatatoServer(email,pass);
        }else{
            Toast.makeText(getApplicationContext(), "Please check your input data", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendDatatoServer(String em, String pw){

        // Set up progress before call
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_HEAD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        progressBar.setProgress(10);
        Call<UserModuleApi> call = service.Login(ApiUrl.API_KEY,em,pw);

        progressBar.setProgress(20);
        call.enqueue(new Callback<UserModuleApi>() {
            @Override
            public void onResponse(Call<UserModuleApi> call, Response<UserModuleApi> response) {
                progressBar.setProgress(30);
                if (response.isSuccessful()){
                    String responCode = response.body().getResponCode();
                    String responDesc = response.body().getResponDesc();
                    progressBar.setProgress(40);
                    if (responCode != null) {
                        if (responCode.equals("100")) {
                            progressBar.setProgress(60);
                            String name = response.body().getUserName();
                            String email = response.body().getEmail();
                            String address = response.body().getAddress();
                            progressBar.setProgress(80);
                            Toast.makeText(LoginActivity.this, responDesc, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(EXTRA_USERNAME, name);
                            intent.putExtra(EXTRA_ADDRESS, address);
                            intent.putExtra(EXTRA_EMAIL, email);
                            progressBar.setProgress(85);
                            //Untuk save session login
                            sharedPreferenceLogin.saveSPString(SharedPreferenceLogin.SP_NAME, name);
                            sharedPreferenceLogin.saveSPString(SharedPreferenceLogin.SP_EMAIL, email);
                            sharedPreferenceLogin.saveSPBoolean(SharedPreferenceLogin.ON_LOGIN, true);

                            //Start Intent
                            progressBar.setProgress(95);
                            startActivity(intent);
                            progressBar.setProgress(100);
                            progressBar.setVisibility(View.GONE);
                            finish();
                        } else {
                            progressBar.setProgress(100);
                            Toast.makeText(LoginActivity.this, responDesc, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModuleApi> call, Throwable t) {
                progressBar.setProgress(100);
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public boolean checkAllData(){
        boolean error = false;
        email = eTEmail.getText().toString();
        pass = eTPassword.getText().toString();

        if (!email.contains("@")){
            eTEmail.setError("Invalid Email");
            error = true;
        }
        if (TextUtils.isEmpty(email)){
            eTEmail.setError("Please Fill Email");
            error = true;
        }
        if (TextUtils.isEmpty(pass)){
            eTPassword.setError("Please fill password");
            error = true;
        }

        return error;
    }

    public void toRegister(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}

