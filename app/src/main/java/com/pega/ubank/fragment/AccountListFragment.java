/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pega.ubank.adapter.AccountListAdapter;
import com.pega.ubank.R;
import com.pega.ubank.data.DummyData;
import com.pega.ubank.fragment.utils.BackAction;

public class AccountListFragment extends Fragment implements BackAction {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.addOnBackStackChangedListener(new BackStackChangedListener(getParentFragment(), childFragmentManager));
        AccountListAdapter adapter = new AccountListAdapter(DummyData.getAccountListData(), childFragmentManager);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public boolean onBackAction() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager.getBackStackEntryCount() == 0) {
            return false;
        } else {
            childFragmentManager.popBackStack();
        }
        return true;
    }

}
