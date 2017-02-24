/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.pega.ubank.adapter.AccountListAdapter;
import com.pega.ubank.R;
import com.pega.ubank.data.DetailedAmountModel;
import com.pega.ubank.data.DummyData;
import com.pega.ubank.fragment.utils.FragmentUtils;

import java.util.Date;

public class TransactionDetailsFragment extends AccountDetailsFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_details, container, false);
    }

    @Override
    protected AccountListAdapter getAdapter() {
        DetailedAmountModel detailedAmountModel = getDetailedAmountModel();
        detailedAmountModel.setDescription("Amount").setNoButtons(true);
        Bundle arguments = getArguments();
        String header = null;
        Date date = null;
        if (arguments != null) {
            header = arguments.getString("details");
            date = new Date(arguments.getLong("date"));
        }
        return new AccountListAdapter(DummyData.getTransactionDetailsData(detailedAmountModel, header, date),
                                      getParentFragment().getChildFragmentManager());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getView().findViewById(R.id.button);
        button.setText("Dispute transaction");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                FragmentUtils.addChildFragment(getParentFragment().getChildFragmentManager(), R.id.innerFragment,
                                               new WebViewFragment(), null,
                                               "disputeTransaction", "Dispute Transaction");
            }
        });
    }
}
