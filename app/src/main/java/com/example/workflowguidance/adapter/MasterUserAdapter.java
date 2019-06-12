package com.example.workflowguidance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.workflowguidance.MainActivity;
import com.example.workflowguidance.R;
import com.example.workflowguidance.model.UserData;

import java.util.ArrayList;

public class MasterUserAdapter extends RecyclerView.Adapter<MasterUserAdapter.ViewHolder> {

    private ArrayList<UserData> mUserData;
    private Context mContext;
    private MainActivity activity;

    public MasterUserAdapter(Context context, ArrayList<UserData> userData, MainActivity mainactivity) {
        this.mUserData = userData;
        this.mContext = context;
        this.activity = mainactivity;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private TextView mUserName;
        private TextView mPositionName;
        private TextView mDivisionName;
        private ImageView mUserImg;

        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            mUserName =itemView.findViewById(R.id.tv_UserName);
            mPositionName = itemView.findViewById(R.id.tv_PositionName);
            mDivisionName = itemView.findViewById(R.id.tv_DivisionName);
            mUserImg = itemView.findViewById(R.id.cv_UserData);
            itemView.setOnClickListener(this);
        }

        void bindTo(UserData currentMaster){
            //Populate the textviews with data
            mUserName.setText(currentMaster.getName());
            mPositionName.setText(currentMaster.getPositionName());
            mDivisionName.setText(currentMaster.getDivisionName());
            //TODO:: Tambahin set gambar ke profilenya UserImg dari hash bmp currentMaster
        }

        @Override
        public void onClick(View view) {
            UserData currentMaster = mUserData.get(getAdapterPosition());
            //TODO::Event ketika di klik
        }
    }

    @Override
    public MasterUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new MasterUserAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_userdata, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MasterUserAdapter.ViewHolder holder, int position) {
        UserData currentMaster = mUserData.get(position);
        //Populate the textviews with data
        holder.bindTo(currentMaster);

        //Memuat sumber gambar
        Glide.with(mContext).load(currentMaster.getImgBmp()).into(holder.mUserImg);
    }

    @Override
    public int getItemCount() {
        return mUserData.size();
    }
}
