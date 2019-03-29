package com.gdi.activity.Audit;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gdi.R;
import com.gdi.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAuditActivity extends BaseActivity {

    @BindView(R.id.spinner_select_audit)
    Spinner spinnerSelectAudit;
    @BindView(R.id.tv_due_date)
    TextView tvDueDate;
    @BindView(R.id.et_audit_name)
    EditText etAuditName;
    @BindView(R.id.et_benchmarking)
    EditText etBenchmarking;
    @BindView(R.id.create_btn)
    Button createBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    private static final String TAG = CreateAuditActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_audit);
        context = this;
        ButterKnife.bind(CreateAuditActivity.this);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar();
    }

    private void setActionBar() {
        initToolbar(toolbar);
        setTitle("Create Audit");
        enableBack(true);
        enableBackPressed();
    }
}
