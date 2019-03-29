package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.BackHouseAdapter4;
import com.gdi.model.reportbackhouse.BackHouseAttachment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BackHouseAttachmentActivity extends BaseActivity {

    @BindView(R.id.recycler_view_attachment)
    RecyclerView recyclerViewAttachment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<BackHouseAttachment> backHouseAttachments;
    Context context;
    private static final String TAG = BackHouseAttachmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attachment);
        context = this;
        ButterKnife.bind(BackHouseAttachmentActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAttachment = (RecyclerView) findViewById(R.id.recycler_view_attachment);
        backHouseAttachments = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        backHouseAttachments = bundle.getParcelableArrayList("data");
        BackHouseAdapter4 integrityAdapter3 = new BackHouseAdapter4(context, backHouseAttachments);
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
