package com.gdi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gdi.R;
import com.gdi.activity.AppTourPagerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTab2 extends Fragment {

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        ButterKnife.bind(this, view);
        //((AppTourPagerActivity)context).slidesIndicator.setVisibility(View.VISIBLE);
        return view;
    }
}
