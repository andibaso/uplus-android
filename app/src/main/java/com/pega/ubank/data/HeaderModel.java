package com.pega.ubank.data;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pega.ubank.R;

public class HeaderModel implements DataModel {
    public static final int HEADER = 0;
    private String name;

    public String getName() {
        return name;
    }

    public HeaderModel setName(String name) {
        this.name = name;
        return this;
    }

    public static RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
        return new HeaderModel.HeaderHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, FragmentManager fragmentManager) {
        ((HeaderHolder) holder).title.setText(name);
    }

    @Override
    public int getType() {
        return HEADER;
    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView title;

        private HeaderHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.sectionHeader);
        }
    }
}
