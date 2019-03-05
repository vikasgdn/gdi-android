package com.gdi.attachmentactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.FaqAttachmentAdapter;
import com.gdi.adapter.IntegrityAdapter3;
import com.gdi.model.faq.FAQAttachment;
import com.gdi.model.integrity.IntegrityAttachment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqAttachmentActivity extends BaseActivity {

    @BindView(R.id.recycler_view_attachment)
    RecyclerView recyclerViewAttachment;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<FAQAttachment> faqAttachments;
    Context context;
    private static final String TAG = FaqAttachmentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attachment);
        context = this;
        ButterKnife.bind(FaqAttachmentActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
        recyclerViewAttachment = (RecyclerView) findViewById(R.id.recycler_view_attachment);
        faqAttachments = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        faqAttachments = bundle.getParcelableArrayList("data");
        FaqAttachmentAdapter integrityAdapter3 = new FaqAttachmentAdapter(context, faqAttachments);
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
