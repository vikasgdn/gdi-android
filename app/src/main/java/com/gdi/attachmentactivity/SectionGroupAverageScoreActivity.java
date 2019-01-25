package com.gdi.attachmentactivity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.SectionGroupAdapter2;
import com.gdi.model.sectiongroup.SectionGroupModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionGroupAverageScoreActivity extends BaseActivity {

    @BindView(R.id.recycler_view_average_score)
    RecyclerView recyclerViewAverageScore;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<SectionGroupModel> sectionGroupModels;
    Context context;
    private static final String TAG = SectionGroupAverageScoreActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_score);
        context = this;
        ButterKnife.bind(SectionGroupAverageScoreActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAverageScore = (RecyclerView) findViewById(R.id.recycler_view_average_score);
        sectionGroupModels = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        sectionGroupModels = bundle.getParcelableArrayList("sectionGroupModel");
        SectionGroupAdapter2 integrityAdapter3 = new SectionGroupAdapter2(context, sectionGroupModels);
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
