package com.gdi.interfaces;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by amantonk on 8/8/16.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClick(RecyclerView.Adapter adapter, View v, int position);
}
