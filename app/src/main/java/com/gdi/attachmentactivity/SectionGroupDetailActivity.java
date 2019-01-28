package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.SectionGroupAdapter2;
import com.gdi.adapter.SectionGroupAdapter3;
import com.gdi.model.sectiongroup.SectionGroupInfo;
import com.gdi.model.sectiongroup.SectionGroupLocation;
import com.gdi.model.sectiongroup.SectionGroupModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionGroupDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.hotel_name)
    TextView hotelName;
    @BindView(R.id.city_name)
    TextView cityName;
    @BindView(R.id.country_name)
    TextView countryName;
    @BindView(R.id.general_manager)
    TextView generalManager;
    @BindView(R.id.tv_overall_score)
    TextView tvOverallScore;
    @BindView(R.id.recycler_view_section_group)
    RecyclerView recyclerViewAverageScore;
    ArrayList<SectionGroupModel> sectionGroupModels;
    private SectionGroupLocation sectionGroupLocation;
    Context context;
    private static final String TAG = SectionGroupDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_section_group_detail);
        context = this;
        ButterKnife.bind(SectionGroupDetailActivity.this);
        sectionGroupLocation = getIntent().getParcelableExtra("sectionGroupData");
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAverageScore = (RecyclerView) findViewById(R.id.recycler_view_section_group);
        hotelName = (TextView) findViewById(R.id.hotel_name);
        cityName = (TextView) findViewById(R.id.city_name);
        countryName = (TextView) findViewById(R.id.country_name);
        generalManager = (TextView) findViewById(R.id.general_manager);
        tvOverallScore = (TextView) findViewById(R.id.tv_overall_score);
        sectionGroupModels = new ArrayList<>();
        sectionGroupModels.addAll(sectionGroupLocation.getSection_groups());
        hotelName.setText(sectionGroupLocation.getLocation());
        cityName.setText(sectionGroupLocation.getCity());
        countryName.setText(sectionGroupLocation.getCountry());
        generalManager.setText(sectionGroupLocation.getGeneral_manager());
        tvOverallScore.setText(sectionGroupLocation.getOverall_score());
        SectionGroupAdapter3 sectionGroupAdapter3 = new SectionGroupAdapter3(context, sectionGroupModels);
        recyclerViewAverageScore.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewAverageScore.setAdapter(sectionGroupAdapter3);

    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle(sectionGroupLocation.getLocation());
        enableBack(true);
        enableBackPressed();
    }


}
