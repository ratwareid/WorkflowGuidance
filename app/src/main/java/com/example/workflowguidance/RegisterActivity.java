package com.example.workflowguidance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.workflowguidance.api.ApiService;
import com.example.workflowguidance.api.ApiUrl;
import com.example.workflowguidance.api.module.UserModuleApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName,etAddress, etEmail, etPW, etRPW;
    private String name,address,email,pass,repass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRepass() {
        return repass;
    }

    public void setRepass(String repass) {
        this.repass = repass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.id_fullname);
        etAddress = findViewById(R.id.id_address);
        etEmail = findViewById(R.id.id_email);
        etPW = findViewById(R.id.id_password);
        etRPW = findViewById(R.id.id_re_password);
    }

    public boolean validateField(){
        boolean failed = false;

        setName(etName.getText().toString());
        setAddress(etAddress.getText().toString());
        setEmail(etEmail.getText().toString());
        setPass(etPW.getText().toString());
        setRepass(etRPW.getText().toString());

        if (TextUtils.isEmpty(name)){
            etName.setError("Please fill Name");
            failed = true;
        }
        if (TextUtils.isEmpty(address)){
            etAddress.setError("Please fill Address");
            failed = true;
        }
        if (TextUtils.isEmpty(email)){
            etEmail.setError("Please fill Email");
            failed = true;
        }
        if (TextUtils.isEmpty(pass)){
            etPW.setError("Please fill Password");
            failed = true;
        }
        if (TextUtils.isEmpty(repass)){
            etRPW.setError("Please Retype Password");
            failed = true;
        }

        if (!email.contains("@")){
            etEmail.setError("Invalid email input");
            failed = true;
        }

        if (!pass.contentEquals(repass)){
            etRPW.setError("Password doesn't match");
            failed = true;
        }

        return failed;
    }


    public void doRegister(View view){

        boolean failed = validateField();
        if (!failed) {
            sendRegister(name,address,email,pass);
        }
    }

    public void sendRegister(String name,String address,String email,String pass){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_HEAD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);
        Call<UserModuleApi> call = service.Register(ApiUrl.API_KEY,name,address,email,pass);

        call.enqueue(new Callback<UserModuleApi>() {
            @Override
            public void onResponse(Call<UserModuleApi> call, Response<UserModuleApi> response) {

                if (response.isSuccessful()){
                    String responCode = response.body().getResponCode();
                    String responDesc = response.body().getResponDesc();

                    if (responCode != null) {
                        if (responCode.equals("100")) {

                            Toast.makeText(RegisterActivity.this, responDesc, Toast.LENGTH_SHORT).show();
                            finish();
                        }else if (responCode.equals("104")){
                            etEmail.setError("Already Exist");
                            Toast.makeText(RegisterActivity.this, responDesc, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, responDesc, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModuleApi> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
