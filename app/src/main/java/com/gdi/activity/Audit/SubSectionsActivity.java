package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.gdi.R;
import com.gdi.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubSectionsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_sub_section_tab)
    RecyclerView subSectionTabList;
    @BindView(R.id.continue_btn)
    Button continueBtn;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_sections);
        context = this;
        ButterKnife.bind(SubSectionsActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        subSectionTabList = (RecyclerView) findViewById(R.id.rv_sub_section_tab);
        continueBtn = (Button) findViewById(R.id.continue_btn);
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Audit Option");
        enableBack(true);
        enableBackPressed();
    }
}
