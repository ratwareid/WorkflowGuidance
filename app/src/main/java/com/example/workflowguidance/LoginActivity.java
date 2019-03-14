package com.example.workflowguidance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.workflowguidance.api.ApiService;
import com.example.workflowguidance.api.ApiUrl;
import com.example.workflowguidance.api.SharedPreferenceManager;
import com.example.workflowguidance.api.module.UserModuleApi;
import com.example.workflowguidance.api.spkey.SPUserDataKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText eTEmail,eTPassword;
    private String email,pass;
    private SharedPreferenceManager spManager;
    private ProgressBar progressBar;

    public static final String EXTRA_USERNAME = "com.example.android.workflowguidance.extra.USERNAME";
    public static final String EXTRA_EMAIL = "com.example.android.workflowguidance.extra.EMAIL";
    public static final String EXTRA_COMPANYID = "com.example.android.workflowguidance.extra.COMPANYID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eTEmail = findViewById(R.id.id_email_login);
        eTPassword = findViewById(R.id.id_password_login);

        spManager = new SharedPreferenceManager(this);

        if (spManager.getSp().getBoolean(SPUserDataKey.LOGGED,false)){
            Intent intent = new Intent (LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
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
                            String companyID = response.body().getCompanyID();

                            progressBar.setProgress(80);
                            Toast.makeText(LoginActivity.this, responDesc, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           /* intent.putExtra(EXTRA_USERNAME, name);
                            intent.putExtra(EXTRA_EMAIL, email);
                            intent.putExtra(EXTRA_COMPANYID, companyID);*/
                            progressBar.setProgress(85);

                            //Untuk save session login
                            spManager.saveSPString(SPUserDataKey.Username, name);
                            spManager.saveSPString(SPUserDataKey.Email, email);
                            spManager.saveSPString(SPUserDataKey.CompanyID, companyID);
                            spManager.saveSPBoolean(SPUserDataKey.LOGGED, true);

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

