package com.gdi.fragment.Tour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gdi.R;
import com.gdi.activity.AppTourPagerActivity;
import com.gdi.activity.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTab4 extends Fragment {

    @BindView(R.id.continue_button)
    Button continueButton;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4, container, false);
        ButterKnife.bind(this, view);
        continueButton = view.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SignInActivity.class));
                ((AppTourPagerActivity)context).finish();
            }
        });
        return view;
    }
}
