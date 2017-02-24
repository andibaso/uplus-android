/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pega.ubank.R;
import com.pega.ubank.adapter.AccountListAdapter;
import com.pega.ubank.DividerDecoration;
import com.pega.ubank.data.DetailedAmountModel;
import com.pega.ubank.data.DummyData;

public class AccountDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prepareRecyclerView(getAdapter());
    }

    DetailedAmountModel getDetailedAmountModel() {
        Bundle arguments = getArguments();
        DetailedAmountModel detailedAmountModel = null;
        if (arguments != null) {
            detailedAmountModel = new DetailedAmountModel().setDescription("Current balance")
                                                           .setCurrency(arguments.getString("currencySymbol"))
                                                           .setAmount(arguments.getFloat("amount"))
                                                           .setCC(arguments.getBoolean("isCC"));
        }
        return detailedAmountModel;
    }

    protected AccountListAdapter getAdapter() {
        DetailedAmountModel detailedAmountModel = getDetailedAmountModel();
        return new AccountListAdapter(DummyData.getAccountDetailsData(detailedAmountModel),
                              getParentFragment().getChildFragmentManager());
    }

    private RecyclerView prepareRecyclerView(RecyclerView.Adapter adapter) {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerDecoration dividerItemDecoration = new DividerDecoration(recyclerView.getContext(),
                                                                        linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

}
