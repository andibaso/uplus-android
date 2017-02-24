package com.pega.ubank.data;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

public interface DataModel {
    void bindViewHolder(RecyclerView.ViewHolder holder, FragmentManager fragmentManager);

    int getType();
}
