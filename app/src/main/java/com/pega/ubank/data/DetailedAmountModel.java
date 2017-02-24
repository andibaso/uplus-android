package com.pega.ubank.data;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pega.ubank.R;

import java.text.DecimalFormat;
import java.util.Locale;

public class DetailedAmountModel implements DataModel {
    public static final int ACCOUNT_DETAILS = 2;

    private String description;
    private float amount;
    private String currency;
    private boolean noButtons = false;
    private boolean isCC = false;

    public DetailedAmountModel setAmount(float amount) {
        this.amount = amount;
        return this;
    }

    public DetailedAmountModel setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public DetailedAmountModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public DetailedAmountModel setNoButtons(boolean noButtons) {
        this.noButtons = noButtons;
        return this;
    }

    public DetailedAmountModel setCC(boolean cc) {
        isCC = cc;
        return this;
    }

    boolean isCC() {
        return isCC;
    }

    @Override
    public int getType() {
        return ACCOUNT_DETAILS;
    }

    public static RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_amount_item, parent, false);
        return new AccountDetailsHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, FragmentManager fragmentManager) {
        AccountDetailsHolder accountHolder = (AccountDetailsHolder) holder;
        accountHolder.description.setText(description);
        accountHolder.currencySymbol.setText(currency);
        int amountMain = (int) amount;
        int amountFractional = (int) ((amount - amountMain) * 100);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedAmountMain = formatter.format(amountMain);
        accountHolder.currencyMain.setText(formattedAmountMain);
        accountHolder.currencyFractional.setText(String.format(Locale.US, "%d", amountFractional));
        if (noButtons) {
            accountHolder.itemView.findViewById(R.id.offers).setVisibility(View.GONE);
            accountHolder.itemView.findViewById(R.id.makeAPayment).setVisibility(View.GONE);
        }
    }

    private static class AccountDetailsHolder extends RecyclerView.ViewHolder{
        private TextView description;
        private TextView currencySymbol;
        private TextView currencyMain;
        private TextView currencyFractional;

        private AccountDetailsHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.accDescription);
            currencySymbol = (TextView) itemView.findViewById(R.id.currencySymbol);
            currencyMain = (TextView) itemView.findViewById(R.id.currencyMain);
            currencyFractional = (TextView) itemView.findViewById(R.id.currencyFractional);
        }
    }
}
