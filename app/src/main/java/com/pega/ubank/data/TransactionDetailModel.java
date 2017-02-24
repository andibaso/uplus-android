package com.pega.ubank.data;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pega.ubank.R;

public class TransactionDetailModel implements DataModel {
    public static final int TRANSACTION_DETAIL = 4;
    private String key;
    private String value;

    TransactionDetailModel setKey(String key) {
        this.key = key;
        return this;
    }

    TransactionDetailModel setValue(String value) {
        this.value = value;
        return this;
    }

    public static RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_detail_item, parent, false);
        return new TransactionDetailModel.TransactionDetail(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, FragmentManager fragmentManager) {
        ((TransactionDetail) holder).itemKey.setText(key);
        ((TransactionDetail) holder).itemValue.setText(value);
    }

    @Override
    public int getType() {
        return TRANSACTION_DETAIL;
    }

    private static class TransactionDetail extends RecyclerView.ViewHolder {
        private TextView itemKey;
        private TextView itemValue;

        private TransactionDetail(View itemView) {
            super(itemView);
            itemKey = (TextView) itemView.findViewById(R.id.itemKey);
            itemValue = (TextView) itemView.findViewById(R.id.itemValue);
        }
    }
}
