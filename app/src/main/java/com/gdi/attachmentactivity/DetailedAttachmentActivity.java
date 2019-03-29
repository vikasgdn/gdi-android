package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.DetailSummaryAdapter4;
import com.gdi.model.reportdetailedsummary.AttachmentsInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedAttachmentActivity extends BaseActivity {

    @BindView(R.id.recycler_view_attachment)
    RecyclerView recyclerViewAttachment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<AttachmentsInfo> attachmentsInfos;
    Context context;
    private static final String TAG = DetailedAttachmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attachment);
        context = this;
        ButterKnife.bind(DetailedAttachmentActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAttachment = (RecyclerView) findViewById(R.id.recycler_view_attachment);
        attachmentsInfos = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        attachmentsInfos = bundle.getParcelableArrayList("data");
        DetailSummaryAdapter4 integrityAdapter3 = new DetailSummaryAdapter4(context, attachmentsInfos);
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
