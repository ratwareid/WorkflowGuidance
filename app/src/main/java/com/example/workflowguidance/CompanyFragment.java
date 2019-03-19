package com.example.workflowguidance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.workflowguidance.api.ApiService;
import com.example.workflowguidance.api.ApiUrl;
import com.example.workflowguidance.api.SharedPreferenceManager;
import com.example.workflowguidance.api.module.CompanyModuleApi;
import com.example.workflowguidance.api.spkey.SPCompanyDataKey;
import com.example.workflowguidance.api.spkey.SPUserDataKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CompanyFragment extends Fragment implements View.OnClickListener, DatePickerFragment.DatePickerDialogFragmentEvents {

    private ImageView IVbadge;
    private MaterialButton btEdit,btSave,btCancel;
    private EditText etCompanyName,etDirector,etBusinessType,etAddress,etPhone,etWeb;
    private String companyName,director,businesstype,address,phone,web,incorp;
    private Integer totalUser,totalWO,totalReport;
    SharedPreferenceManager spManager;
    public Activity mActivity;
    private String mode;
    private TextView tvTotalUser,tvIncorp;
    private String selectedDate;

    public CompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity =(Activity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        spManager = new SharedPreferenceManager(Objects.requireNonNull(this.getContext()));
        mode = "view";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        //Inisialisasi Btn on click
        this.init(view);
        requestData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void init(View view) {

        //Header
        tvTotalUser = view.findViewById(R.id.id_tvTotalUser);

        //Detail
        IVbadge = view.findViewById(R.id.id_IVbadge);
        IVbadge.setOnClickListener(this);
        btEdit = view.findViewById(R.id.btn_editcompany);
        btEdit.setOnClickListener(this);
        btSave = view.findViewById(R.id.btn_Save);
        btSave.setOnClickListener(this);
        btCancel = view.findViewById(R.id.btn_Cancel);
        btCancel.setOnClickListener(this);
        etCompanyName = view.findViewById(R.id.id_etCompanyName);
        etDirector = view.findViewById(R.id.id_etDirector);
        etBusinessType = view.findViewById(R.id.id_etBusinesstype);
        etAddress = view.findViewById(R.id.id_etAddress);
        etPhone = view.findViewById(R.id.id_etPhone);
        etWeb = view.findViewById(R.id.id_etWeb);
        tvIncorp =  view.findViewById(R.id.id_tvIncorp);
        tvIncorp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(IVbadge)) {
            FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            PremiumMemberFragment fragmentpremium = new PremiumMemberFragment();
            ft.replace(R.id.FrameFragment, fragmentpremium);
            ft.commit();
        }
        if (v.equals(btEdit)){
            mode = "edit";
            etCompanyName.setEnabled(true);
            etDirector.setEnabled(true);
            etBusinessType.setEnabled(true);
            etAddress.setEnabled(true);
            etPhone.setEnabled(true);
            etWeb.setEnabled(true);

            //Remove btn Edit and visible btn save,cancel
            btEdit.setVisibility(View.GONE);
            btCancel.setVisibility(View.VISIBLE);
            btSave.setVisibility(View.VISIBLE);
        }

        if (v.equals(btCancel)){
            //call to Fragment Company again
            confirmDialog("Cancel");
        }
        if (v.equals(btSave)){
            //Hit to WS
            confirmDialog("Save");
        }
        if (v.equals(tvIncorp)){
            if (mode.equalsIgnoreCase("edit")) {
                DialogFragment picker = new DatePickerFragment();
                ((DatePickerFragment) picker).setDatePickerDialogFragmentEvents(this); //Changed
                picker.show(getFragmentManager(), "Date Picker");
            }
        }
    }

    public void doCancel(){
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        CompanyFragment thisfrag = new CompanyFragment();
        ft.replace(R.id.FrameFragment, thisfrag);
        ft.commit();
    }

    public void doSave(){
        // Hit WS to Save data
        saveData();
        //End Hit

        doCancel();
    }

    public void confirmDialog(final String typeentry){
        new AlertDialog.Builder(Objects.requireNonNull(this.getContext()))
                .setTitle( typeentry+" Entry")
                .setMessage("Are you sure you want to "+typeentry+" this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        if (typeentry.equalsIgnoreCase("Cancel")) {
                            doCancel();
                        }
                        if (typeentry.equalsIgnoreCase("Save")){
                            doSave();
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void requestData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_HEAD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        String companyID = spManager.getSp().getString(SPUserDataKey.CompanyID, "");

        Call<CompanyModuleApi> call = service.GetData(ApiUrl.API_KEY, companyID);
        call.enqueue(new Callback<CompanyModuleApi>() {
            @Override
            public void onResponse(Call<CompanyModuleApi> call, Response<CompanyModuleApi> response) {
                String responCode = response.body().getResponCode();
                String responDesc = response.body().getResponDesc();

                if (responCode != null) {
                    if (responCode.equals("100")) {

                        //Report Data
                        Integer TotalUser = response.body().getTotalUser();

                        String CName = response.body().getCompanyName();
                        String CDir = response.body().getCompanyDirector();
                        String CType = response.body().getBusinessType();
                        String CAddr= response.body().getCompanyAddress();
                        String CPhone = response.body().getPhoneNumber();
                        String CWeb = response.body().getWebsite();
                        String CInc = response.body().getIncorporatedDate();


                        //Simpan data ke Shared Preference
                        spManager.saveSPInt(SPCompanyDataKey.TotalUser,TotalUser);
                        spManager.saveSPString(SPCompanyDataKey.CompanyName, CName);
                        spManager.saveSPString(SPCompanyDataKey.Director, CDir);
                        spManager.saveSPString(SPCompanyDataKey.BusinessType, CType);
                        spManager.saveSPString(SPCompanyDataKey.ADDRESS, CAddr);
                        spManager.saveSPString(SPCompanyDataKey.PHONE, CPhone);
                        spManager.saveSPString(SPCompanyDataKey.WEB, CWeb);
                        spManager.saveSPString(SPCompanyDataKey.INCORP, CInc);

                        getCompanyData();
                        placeAllData();
                    }else{
                        Toast.makeText(mActivity, responDesc, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CompanyModuleApi> call, Throwable t) {
                Toast.makeText(mActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_HEAD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        String companyID = spManager.getSp().getString(SPUserDataKey.CompanyID, "");

        getCurrentData(); //Ambil data terbaru yang sudah di edit

        Call<CompanyModuleApi> call = service.SendData(ApiUrl.API_KEY, companyID,companyName,director,businesstype,address,phone,web,incorp);
        call.enqueue(new Callback<CompanyModuleApi>() {
            @Override
            public void onResponse(Call<CompanyModuleApi> call, Response<CompanyModuleApi> response) {

                if (response.body() != null) {
                    String responCode = response.body().getResponCode();
                    String responDesc = response.body().getResponDesc();

                    if (responCode != null) {
                        if (responCode.equals("100")) {

                            String CName = response.body().getCompanyName();
                            String CDir = response.body().getCompanyDirector();
                            String CType = response.body().getBusinessType();
                            String CAddr = response.body().getCompanyAddress();
                            String CPhone = response.body().getPhoneNumber();
                            String CWeb = response.body().getWebsite();
                            String CInc = response.body().getIncorporatedDate();

                            //Simpan data ke Shared Preference
                            spManager.saveSPString(SPCompanyDataKey.CompanyName, CName);
                            spManager.saveSPString(SPCompanyDataKey.Director, CDir);
                            spManager.saveSPString(SPCompanyDataKey.BusinessType, CType);
                            spManager.saveSPString(SPCompanyDataKey.ADDRESS, CAddr);
                            spManager.saveSPString(SPCompanyDataKey.PHONE, CPhone);
                            spManager.saveSPString(SPCompanyDataKey.WEB, CWeb);
                            spManager.saveSPString(SPCompanyDataKey.INCORP, CInc);

                            getCompanyData();
                            placeAllData();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CompanyModuleApi> call, Throwable t) {
                Toast.makeText(mActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCurrentData(){

        companyName = etCompanyName.getText().toString();
        director = etDirector.getText().toString();
        businesstype = etBusinessType.getText().toString();
        address = etAddress.getText().toString();
        phone = etPhone.getText().toString();
        web = etWeb.getText().toString();
        incorp = tvIncorp.getText().toString();
    }

    public void getCompanyData(){

        //Header
        totalUser = spManager.getSp().getInt(SPCompanyDataKey.TotalUser,0);

        companyName = spManager.getSp().getString(SPCompanyDataKey.CompanyName,"");
        director = spManager.getSp().getString(SPCompanyDataKey.Director,"");
        businesstype = spManager.getSp().getString(SPCompanyDataKey.BusinessType,"");
        address = spManager.getSp().getString(SPCompanyDataKey.ADDRESS,"");
        phone = spManager.getSp().getString(SPCompanyDataKey.PHONE,"");
        web = spManager.getSp().getString(SPCompanyDataKey.WEB,"");
        incorp = spManager.getSp().getString(SPCompanyDataKey.INCORP,"");
    }

    public void placeAllData(){

        //Header
        tvTotalUser.setText(totalUser.toString());

        etCompanyName.setText(companyName);
        etDirector.setText(director);
        etBusinessType.setText(businesstype);
        etAddress.setText(address);
        etPhone.setText(phone);
        etWeb.setText(web);
        tvIncorp.setText(incorp);
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        tvIncorp.setText(selectedDate);
    }
}
