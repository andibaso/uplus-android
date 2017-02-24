package com.pega.ubank.data;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pega.ubank.R;
import com.pega.ubank.fragment.AccountDetailsFragment;
import com.pega.ubank.fragment.utils.FragmentUtils;

import java.text.DecimalFormat;
import java.util.Locale;

public class AccountModel implements DataModel, View.OnClickListener {
    public static final int ACCOUNT = 1;

    private String name;
    private String description;
    private float amount;
    private String currency;
    private FragmentManager fragmentManager;

    AccountModel setAmount(float amount) {
        this.amount = amount;
        return this;
    }

    AccountModel setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    AccountModel setName(String name) {
        this.name = name;
        return this;
    }

    AccountModel setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public int getType() {
        return ACCOUNT;
    }

    public static RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acc_item, parent, false);
        return new AccountHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, FragmentManager fragmentManager) {
        AccountHolder accountHolder = (AccountHolder) holder;
        accountHolder.title.setText(name);
        accountHolder.description.setText(description);
        accountHolder.currencySymbol.setText(currency);
        int amountMain = (int) amount;
        int amountFractional = (int) ((amount - amountMain) * 100);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedAmountMain = formatter.format(amountMain);
        accountHolder.currencyMain.setText(formattedAmountMain);
        accountHolder.currencyFractional.setText(String.format(Locale.US, "%d", amountFractional));
        this.fragmentManager = fragmentManager;
        holder.itemView.setOnClickListener(this);
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
        // fake credit-card check
        if (name.contains("Divident World")) {
            data.putBoolean("isCC", true);
        }
        FragmentUtils.addChildFragment(fragmentManager, R.id.innerFragment, new AccountDetailsFragment(), data,
                                       "accDetails", activity.getResources().getString(R.string.app_name));
    }

    private static class AccountHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView currencySymbol;
        private TextView currencyMain;
        private TextView currencyFractional;

        private AccountHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.accName);
            description = (TextView) itemView.findViewById(R.id.accDescription);
            currencySymbol = (TextView) itemView.findViewById(R.id.currencySymbol);
            currencyMain = (TextView) itemView.findViewById(R.id.currencyMain);
            currencyFractional = (TextView) itemView.findViewById(R.id.currencyFractional);
        }
    }
}
