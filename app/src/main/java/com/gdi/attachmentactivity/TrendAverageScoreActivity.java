package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.gdi.hotel.mystery.audits.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.ReportTrendLocationAdapter3;
import com.gdi.model.trendlocation.TrendLocationRound2;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendAverageScoreActivity extends BaseActivity {

    @BindView(R.id.recycler_view_average_score)
    RecyclerView recyclerViewAverageScore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<TrendLocationRound2> trendLocationRound2s;
    Context context;
    private static final String TAG = TrendAverageScoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_average_score);
        context = this;
        ButterKnife.bind(TrendAverageScoreActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAverageScore = findViewById(R.id.recycler_view_average_score);
        trendLocationRound2s = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        trendLocationRound2s = bundle.getParcelableArrayList("sectionGroupModel");
        ReportTrendLocationAdapter3 integrityAdapter3 = new ReportTrendLocationAdapter3(context, trendLocationRound2s);
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
