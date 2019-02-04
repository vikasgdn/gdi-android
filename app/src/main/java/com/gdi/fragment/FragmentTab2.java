package com.gdi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gdi.R;
import com.gdi.activity.AppTourPagerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTab2 extends Fragment {

    @BindView(R.id.frag_image)
    ImageView fragmentImage;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static FragmentTab2 newInstance(int position) {

        FragmentTab2 f = new FragmentTab2();
        Bundle b = new Bundle();
        b.putInt("pos", position);

        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        ButterKnife.bind(this, view);
        fragmentImage = (ImageView)view.findViewById(R.id.frag_image);
        ((AppTourPagerActivity)context).slidesIndicator.setVisibility(View.VISIBLE);
        fragmentImage.setImageDrawable(getResources().getDrawable(R.drawable.info_2));
        //initViews();
        return view;
    }
}
