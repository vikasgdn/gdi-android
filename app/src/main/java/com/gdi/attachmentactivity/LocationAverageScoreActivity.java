package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.ReportLocationCampaignAdapter3;
import com.gdi.model.locationcampaign.LocationCampaignRound2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationAverageScoreActivity extends BaseActivity {

    @BindView(R.id.recycler_view_average_score)
    RecyclerView recyclerViewAverageScore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<LocationCampaignRound2> locationCampaignRound2s;
    Context context;
    private static final String TAG = LocationAverageScoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_average_score);
        context = this;
        ButterKnife.bind(LocationAverageScoreActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAverageScore = findViewById(R.id.recycler_view_average_score);
        locationCampaignRound2s = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        locationCampaignRound2s = bundle.getParcelableArrayList("sectionGroupModel");
        ReportLocationCampaignAdapter3 integrityAdapter3 = new ReportLocationCampaignAdapter3(context, locationCampaignRound2s);
        recyclerViewAverageScore.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewAverageScore.setAdapter(integrityAdapter3);

    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Average Score");
        enableBack(true);
        enableBackPressed();
    }
}
