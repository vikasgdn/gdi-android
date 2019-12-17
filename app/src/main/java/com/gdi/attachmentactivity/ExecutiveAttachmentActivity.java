package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.ExecutiveSummaryAdapter2;
import com.gdi.model.reportexecutivesummary.ExecutiveAttachmentsInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExecutiveAttachmentActivity extends BaseActivity {

    @BindView(R.id.recycler_view_attachment)
    RecyclerView recyclerViewAttachment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<ExecutiveAttachmentsInfo> executiveAttachmentsInfos;
    Context context;
    private static final String TAG = ExecutiveAttachmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attachment);
        context = this;
        ButterKnife.bind(ExecutiveAttachmentActivity.this);
        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAttachment = findViewById(R.id.recycler_view_attachment);
        executiveAttachmentsInfos = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        executiveAttachmentsInfos = bundle.getParcelableArrayList("data");
        ExecutiveSummaryAdapter2 integrityAdapter3 = new ExecutiveSummaryAdapter2(context, executiveAttachmentsInfos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2
                , LinearLayoutManager.VERTICAL,false);
        recyclerViewAttachment.setLayoutManager(gridLayoutManager);
        recyclerViewAttachment.setAdapter(integrityAdapter3);

    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Attachments");
        enableBack(true);
        enableBackPressed();
    }
}
