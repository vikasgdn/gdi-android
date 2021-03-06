package com.gdi.fragment.Tour;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gdi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTab extends Fragment {

    @BindView(R.id.frag_image)
    ImageView fragmentImage;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, view);
        fragmentImage = view.findViewById(R.id.frag_image);
        return view;
    }
}
