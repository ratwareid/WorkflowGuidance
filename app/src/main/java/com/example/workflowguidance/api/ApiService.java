package com.example.workflowguidance.api;

import com.example.workflowguidance.api.module.UserModuleApi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<UserModuleApi>
    Login (
            @Field("key") String key,
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("register.php")
    Call<UserModuleApi>
    Register (
            @Field("key") String key,
            @Field("name") String name,
            @Field("address") String address,
            @Field("email") String email,
            @Field("password") String password
    );
}
