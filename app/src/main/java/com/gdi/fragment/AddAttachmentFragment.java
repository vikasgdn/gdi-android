package com.gdi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gdi.R;
import com.gdi.activity.BaseActivity;
import com.gdi.adapter.AuditActionAdapter;
import com.gdi.api.ApiEndPoints;
import com.gdi.api.GetReportRequest;
import com.gdi.api.VolleyNetworkRequest;
import com.gdi.model.audit.AuditInfo;
import com.gdi.model.audit.AuditRootObject;
import com.gdi.utils.ApiResponseKeys;
import com.gdi.utils.AppLogger;
import com.gdi.utils.AppPrefs;
import com.gdi.utils.AppUtils;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAttachmentFragment extends Fragment {

    @BindView(R.id.iv_attached_image)
    ImageView attachedImage;
    @BindView(R.id.et_description)
    EditText attachedDescription;
    @BindView(R.id.tv_submit_btn)
    TextView submitBtn;
    @BindView(R.id.tv_cancel_btn)
    TextView cancelBtn;
    private Context context;
    ArrayList<AuditInfo> mainArrayList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppUtils.isNetworkConnected(context)) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_attachment, container, false);
        ButterKnife.bind(this, view);
        attachedImage = view.findViewById(R.id.iv_attached_image);
        attachedDescription = view.findViewById(R.id.et_description);
        submitBtn = view.findViewById(R.id.tv_submit_btn);
        cancelBtn = view.findViewById(R.id.tv_cancel_btn);
        mainArrayList = new ArrayList<>();

        return view;
    }


}
