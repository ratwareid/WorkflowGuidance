package com.example.workflowguidance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.workflowguidance.api.ApiService;
import com.example.workflowguidance.api.ApiUrl;
import com.example.workflowguidance.api.SharedPreferenceManager;
import com.example.workflowguidance.api.module.CompanyModuleApi;
import com.example.workflowguidance.api.spkey.SPUserDataKey;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CompanyFragment extends Fragment implements View.OnClickListener {

    private ImageView IVbadge;
    private MaterialButton btEdit,btSave,btCancel;
    private EditText etCompanyName,etDirector,etBusinessType,etAddress,etPhone,etWeb,etIncorp;
    private String companyName,director,businesstype,address,phone,web,indcorp;
    SharedPreferenceManager spManager;

    public CompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        spManager = new SharedPreferenceManager(Objects.requireNonNull(this.getContext()));

        requestData();
        getCompanyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        //Inisialisasi Btn on click
        this.init(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void init(View view) {
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
        etIncorp = view.findViewById(R.id.id_etIncorp);
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
            etCompanyName.setEnabled(true);
            etDirector.setEnabled(true);
            etBusinessType.setEnabled(true);
            etAddress.setEnabled(true);
            etPhone.setEnabled(true);
            etWeb.setEnabled(true);
            etIncorp.setEnabled(true);

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
    }

    public void doCancel(){
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        CompanyFragment thisfrag = new CompanyFragment();
        ft.replace(R.id.FrameFragment, thisfrag);
        ft.commit();
    }

    public void doSave(){
        // Hit WS to Save data

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

    public void requestData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_HEAD)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        String companyID = spManager.getSp().getString(SPUserDataKey.CompanyID,"");

        Call<CompanyModuleApi> call = service.GetData(ApiUrl.API_KEY,companyID);
        /*call.enqueue(new Callback<CompanyModuleApi>() {
            @Override
            public void onResponse(Call<CompanyModuleApi> call, Response<CompanyModuleApi> response) {

            }*/
    }

    public void getCompanyData(){

    }
}
