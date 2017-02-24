package com.pega.ubank.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.pega.ubank.data.DetailedAmountModel;
import com.pega.ubank.data.AccountModel;
import com.pega.ubank.data.DataModel;
import com.pega.ubank.data.HeaderModel;
import com.pega.ubank.data.TransactionDetailModel;
import com.pega.ubank.data.TransactionModel;

import java.util.List;


public class AccountListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataModel> list;
    private FragmentManager fragmentManager;

    public AccountListAdapter(List<DataModel> list, FragmentManager fragmentManager) {
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HeaderModel.HEADER:
                return HeaderModel.createViewHolder(parent);
            case AccountModel.ACCOUNT:
                return AccountModel.createViewHolder(parent);
            case TransactionModel.TRANSACTION:
                return TransactionModel.createViewHolder(parent);
            case DetailedAmountModel.ACCOUNT_DETAILS:
                return DetailedAmountModel.createViewHolder(parent);
            case TransactionDetailModel.TRANSACTION_DETAIL:
                return TransactionDetailModel.createViewHolder(parent);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DataModel dataModel = list.get(position);
        if (dataModel != null) {
            dataModel.bindViewHolder(holder, fragmentManager);
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list != null) {
            DataModel object = list.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return -1;
    }
}

