package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.IntegrityAdapter3;
import com.gdi.model.reportintegrity.IntegrityAttachment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntegrityAttachmentActivity extends BaseActivity {

    @BindView(R.id.recycler_view_attachment)
    RecyclerView recyclerViewAttachment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<IntegrityAttachment> integrityAttachments;
    Context context;
    private static final String TAG = IntegrityAttachmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attachment);
        context = this;
        ButterKnife.bind(IntegrityAttachmentActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAttachment = (RecyclerView) findViewById(R.id.recycler_view_attachment);
        integrityAttachments = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        integrityAttachments = bundle.getParcelableArrayList("data");
        IntegrityAdapter3 integrityAdapter3 = new IntegrityAdapter3(context, integrityAttachments);
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
