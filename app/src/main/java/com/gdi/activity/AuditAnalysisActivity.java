package com.gdi.activity;

import android.content.Context;
import android.os.Bundle;

import com.gdi.hotel.mystery.audits.R;


import butterknife.ButterKnife;

public class AuditAnalysisActivity extends BaseActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        context = this;
        ButterKnife.bind(AuditAnalysisActivity.this);
        initView();
    }

    private void initView(){}
}
