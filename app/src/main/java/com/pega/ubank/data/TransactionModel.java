package com.pega.ubank.data;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pega.ubank.R;
import com.pega.ubank.fragment.TransactionDetailsFragment;
import com.pega.ubank.fragment.utils.FragmentUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionModel implements DataModel, View.OnClickListener {
    public static final int TRANSACTION = 3;

    private String description;
    private float amount;
    private String currency;
    private Date date;
    private String status;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
    private FragmentManager fragmentManager;

    TransactionModel setAmount(float amount) {
        this.amount = amount;
        return this;
    }

    TransactionModel setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    TransactionModel setDescription(String description) {
        this.description = description;
        return this;
    }

    TransactionModel setDate(Date date) {
        this.date = date;
        return this;
    }

    TransactionModel setStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public int getType() {
        return TRANSACTION;
    }

    public static RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionModel.TransactionHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        TransactionHolder transactionHolder = (TransactionHolder) holder;
        transactionHolder.transactionDetails.setText(description);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedAmountMain = formatter.format(amount);
        transactionHolder.transactionAmount.setText(currency + " " + formattedAmountMain);
        if (date == null) {
            transactionHolder.date.setVisibility(View.GONE);
            ((LinearLayout.LayoutParams) holder.itemView.findViewById(R.id.amountWrapper)
                                                        .getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
            ((LinearLayout) holder.itemView.findViewById(R.id.transaction)).setGravity(Gravity.CENTER_VERTICAL);
        } else {
            transactionHolder.date.setText(dateFormat.format(date));
            holder.itemView.setOnClickListener(this);
        }
        if (status == null) {
            transactionHolder.transactionStatus.setVisibility(View.GONE);
        } else {
            transactionHolder.transactionStatus.setText(status);
        }
    }

    @Override
    public void onClick(View view) {
        FragmentActivity activity =
                view.getContext() instanceof FragmentActivity ? ((FragmentActivity) view.getContext()) : null;
        if (activity == null) {
            return;
        }
        Bundle data = new Bundle();
        data.putString("currencySymbol", currency);
        data.putFloat("amount", amount);
        data.putString("details", description);
        data.putLong("date", date.getTime());
        FragmentUtils.addChildFragment(fragmentManager, R.id.innerFragment, new TransactionDetailsFragment(), data,
                                       "transDetails", "Transaction Details");
    }

    private static class TransactionHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView transactionDetails;
        private TextView transactionAmount;
        private TextView transactionStatus;

        private TransactionHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            transactionDetails = (TextView) itemView.findViewById(R.id.transactionDetails);
            transactionAmount = (TextView) itemView.findViewById(R.id.transactionAmount);
            transactionStatus = (TextView) itemView.findViewById(R.id.transactionStatus);
        }
    }
}
