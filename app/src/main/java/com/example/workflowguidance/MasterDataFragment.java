package com.example.workflowguidance;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.workflowguidance.adapter.MasterDataAdapter;
import com.example.workflowguidance.model.MasterData;

import java.util.ArrayList;
import java.util.Collections;

public class MasterDataFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<MasterData> mMasterData;
    private MasterDataAdapter mAdapter;
    private ItemTouchHelper itemTouchHelper;
    private TextView marqueetext;
    public MainActivity mainActivity;

    public MasterDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_masterdata, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    public void init(View view){

        mMasterData =  new ArrayList<>();
        mAdapter = new MasterDataAdapter(this.getContext(), mMasterData,mainActivity);

        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);
        mRecyclerView = view.findViewById(R.id.recycler_master);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),gridColumnCount));
        mRecyclerView.setAdapter(mAdapter);

        int swipeDirs;
        if(gridColumnCount > 1){
            swipeDirs = 0;
        } else {
            swipeDirs = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        }

        //Recycler item toucher helper
        initializeData();

        //Marquee
        marqueetext = view.findViewById(R.id.footer_running);
        marqueetext.setSelected(true);
    }

    private void initializeData() {

        //Get the resources from the XML file
        String[] masterName = getResources().getStringArray(R.array.master_name);
        String[] masterDetail = getResources().getStringArray(R.array.master_detail);
        TypedArray masterImage = getResources().obtainTypedArray(R.array.master_images);

        if (mMasterData != null) {
            //Clear the existing data (to avoid duplication)
            mMasterData.clear();
        }

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i=0;i<masterName.length;i++){
            MasterData mData = new MasterData();
            mData.setName(masterName[i]);
            mData.setDesc(masterDetail[i]);
            mData.setImgResouce(masterImage.getResourceId(i,0));
            mMasterData.add(mData);
        }
        masterImage.recycle();
        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }

}
