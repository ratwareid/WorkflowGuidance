package com.example.workflowguidance;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.workflowguidance.api.ApiService;
import com.example.workflowguidance.api.ApiUrl;
import com.example.workflowguidance.api.SharedPreferenceManager;
import com.example.workflowguidance.api.module.CompanyModuleApi;
import com.example.workflowguidance.api.spkey.SPCompanyDataKey;
import com.example.workflowguidance.api.spkey.SPUserDataKey;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.app.Activity.RESULT_OK;


public class CompanyFragment extends Fragment implements View.OnClickListener, DatePickerFragment.DatePickerDialogFragmentEvents {

    private ImageView IVProfile;
    private MaterialButton btEdit,btSave,btCancel;
    private EditText etCompanyName,etDirector,etBusinessType,etAddress,etPhone,etWeb;
    private String companyName,director,businesstype,address,phone,web,incorp;
    private Integer totalUser,totalWO,totalReport;
    SharedPreferenceManager spManager;
    public Activity mActivity;
    private String mode;
    private TextView tvTotalUser,tvIncorp;
    private String selectedDate;
    private ImageView ibTakePhoto;
    private String imgProfile;
    private ProgressDialog progressDialog;
    private RelativeLayout RLparentProfile;

    Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private Bitmap bitmap;

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

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void init(View view) {
        RLparentProfile = view.findViewById(R.id.id_ParentProfile);

        //Header
        tvTotalUser = view.findViewById(R.id.id_tvTotalUser);

        //Detail
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

        //Other Component
        ibTakePhoto =  view.findViewById(R.id.id_take_photo);
        ibTakePhoto.setOnClickListener(this);
        IVProfile = view.findViewById(R.id.id_IVphoto_profile);
    }

    @Override
    public void onClick(View v) {
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

        if (v.equals(ibTakePhoto)){
            showDialog();
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
        getCompanyData();
        placeAllData();
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

                        //Image
                        String Cprofile = response.body().getCompanyProfile();

                        String CName = response.body().getCompanyName();
                        String CDir = response.body().getCompanyDirector();
                        String CType = response.body().getBusinessType();
                        String CAddr= response.body().getCompanyAddress();
                        String CPhone = response.body().getPhoneNumber();
                        String CWeb = response.body().getWebsite();
                        String CInc = response.body().getIncorporatedDate();



                        //Simpan data ke Shared Preference
                        spManager.saveSPInt(SPCompanyDataKey.TotalUser,TotalUser);
                        spManager.saveSPString(SPCompanyDataKey.ProfilePicture,Cprofile);
                        spManager.saveSPString(SPCompanyDataKey.CompanyName, CName);
                        spManager.saveSPString(SPCompanyDataKey.Director, CDir);
                        spManager.saveSPString(SPCompanyDataKey.BusinessType, CType);
                        spManager.saveSPString(SPCompanyDataKey.ADDRESS, CAddr);
                        spManager.saveSPString(SPCompanyDataKey.PHONE, CPhone);
                        spManager.saveSPString(SPCompanyDataKey.WEB, CWeb);
                        spManager.saveSPString(SPCompanyDataKey.INCORP, CInc);

                        getCompanyData();
                        IVProfile.setVisibility(View.VISIBLE);
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

        //Image
        imgProfile = spManager.getSp().getString(SPCompanyDataKey.ProfilePicture,"");

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

        //Image
        if (imgProfile.equalsIgnoreCase("")){
            IVProfile.setImageResource(R.drawable.imageshigh);
        }else{
            //Convert imgProfile ke bitmap lalu save ke IVProfile
            byte[] decodedString = Base64.decode(imgProfile, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (decodedByte != null){
                IVProfile.setImageBitmap(decodedByte);
            }
        }

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

    public void showDialog(){
        final Dialog dialogPicture = new Dialog(getActivity());
        dialogPicture.setCancelable(true);

        View view  = getActivity().getLayoutInflater().inflate(R.layout.dialog_changepp, null);
        dialogPicture.setContentView(view);

        TextView photo = (TextView) view.findViewById(R.id.tv_photo);
        TextView gallery = (TextView) view.findViewById(R.id.tv_gallery);


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //define the file-name to save photo taken by Camera activity
                String fileName = "new-photo-name.jpg";
                //create parameters for Intent with filename
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
                //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //create new Intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, PICK_Camera_IMAGE);

                dialogPicture.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);//
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
                dialogPicture.dismiss();
            }
        });

        dialogPicture.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    selectedImageUri = imageUri;
		 		    	/*Bitmap mPic = (Bitmap) data.getExtras().get("data");
						selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mPic, getResources().getString(R.string.app_name), Long.toString(System.currentTimeMillis())));*/
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if(selectedImageUri != null){
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @SuppressLint("ShowToast")
    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        boolean failed = false;

        //Try to endcode bitmap
        if (bitmap == null){
            //If bitmap failed to encode
            IVProfile.setImageResource(R.drawable.imageshigh);
            Toast.makeText(getContext(),"Cant find specific images",Toast.LENGTH_LONG).show();
        }else {
            //If success encode
            doUploadImage(bitmap); //Try to upload base64 to server
        }
    }

    public void doUploadImage(Bitmap bitmap){

        String img64 = decodeBitmap(bitmap);
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor mHttpLoggingInterceptor = new HttpLoggingInterceptor();
        mHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mBuilder.connectTimeout(30, TimeUnit.SECONDS);
        mBuilder.readTimeout(30, TimeUnit.SECONDS);
        mBuilder.addInterceptor(mHttpLoggingInterceptor);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_HEAD)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mBuilder.build())
                .build();
        ApiService service = retrofit.create(ApiService.class);

        String companyID = spManager.getSp().getString(SPUserDataKey.CompanyID, "");

        Call<CompanyModuleApi> call = service.UploadImg(ApiUrl.API_KEY, companyID,img64);
        call.enqueue(new Callback<CompanyModuleApi>() {
            @Override
            public void onResponse(Call<CompanyModuleApi> call, Response<CompanyModuleApi> response) {

                if (response.body() != null) {
                    String responCode = response.body().getResponCode();
                    String responDesc = response.body().getResponDesc();

                    if (responCode != null) {
                        if (responCode.equals("100")) {

                            String img64 = response.body().getCompanyProfile();
                            spManager.saveSPString(SPCompanyDataKey.ProfilePicture, img64);

                            requestData();
                        }else{
                            Toast.makeText(getContext(),responCode +" : "+responDesc,Toast.LENGTH_LONG).show();
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

    public String decodeBitmap(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray  = byteArrayOutputStream .toByteArray();
        String val = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return val;
    }
}
