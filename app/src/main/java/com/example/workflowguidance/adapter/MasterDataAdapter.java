package com.example.workflowguidance.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.workflowguidance.master.MasterUser;
import com.example.workflowguidance.model.MasterData;

import java.util.ArrayList;
import java.util.List;

public class MasterDataAdapter extends RecyclerView.Adapter<MasterDataAdapter.ViewHolder> {
    private ArrayList<MasterData> mMasterData;
    private Context mContext;
    private MainActivity activity;

    public MasterDataAdapter(Context context, ArrayList<MasterData> masterData, MainActivity mainActivity) {
        this.mMasterData = masterData;
        this.mContext = context;
        this.activity = mainActivity;
    }

    @Override
    public MasterDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_masterdata, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MasterDataAdapter.ViewHolder holder, int position) {
        MasterData currentMaster = mMasterData.get(position);
        //Populate the textviews with data
        holder.bindTo(currentMaster);

        //Memuat sumber gambar
        Glide.with(mContext).load(currentMaster.getImgResouce()).into(holder.mMasterImage);
    }

    @Override
    public int getItemCount() {
        return mMasterData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private TextView mMasterName;
        private TextView mMasterDetail;
        private ImageView mMasterImage;

        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            mMasterName =itemView.findViewById(R.id.master_name);
            mMasterDetail = itemView.findViewById(R.id.master_detail);
            mMasterImage = itemView.findViewById(R.id.masterImage);
            itemView.setOnClickListener(this);
        }

        void bindTo(MasterData currentMaster){
            //Populate the textviews with data
            mMasterName.setText(currentMaster.getName());
            mMasterDetail.setText(currentMaster.getDesc());

        }

        @Override
        public void onClick(View view) {
            MasterData currentMaster = mMasterData.get(getAdapterPosition());
            if (currentMaster.getName().equalsIgnoreCase("Master User")) {
                Intent i = new Intent(mContext,MasterUser.class);
                activity.startActivityForResult(i,1);
            }

        }
    }
}
