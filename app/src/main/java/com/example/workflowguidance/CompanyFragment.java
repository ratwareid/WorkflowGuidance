package com.example.workflowguidance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Objects;


public class CompanyFragment extends Fragment implements View.OnClickListener {

    private ImageView test;

    public CompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        this.init(view);
        return view;
    }

    private void init(View view) {
        test = view.findViewById(R.id.testest);
        test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(test)) {
            FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            PremiumMemberFragment fragmentpremium = new PremiumMemberFragment();
            ft.replace(R.id.FrameFragment, fragmentpremium);
            ft.commit();
        }
    }
}
