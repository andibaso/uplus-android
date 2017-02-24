/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pega.mobile.authentication.pega.PegaAuthenticator;
import com.pega.mobile.authentication.pega.PegaAuthenticatorProvider;
import com.pega.ubank.adapter.FragmentAdapter;
import com.pega.ubank.MashupUtilities;
import com.pega.ubank.R;
import com.pega.ubank.fragment.utils.BackAction;
import com.pega.ubank.fragment.utils.FragmentUtils;

public class MainFragment extends Fragment implements BackAction {
    private Toolbar toolbar;

    private PegaAuthenticator pegaAuthenticator;
    private FragmentAdapter pagerAdapter;
    private ViewPager pager;
    private ToolbarWrapper toolbarWrapper;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        pegaAuthenticator = new PegaAuthenticatorProvider().getPegaAuthenticator();
        pager = (ViewPager) view.findViewById(R.id.vpPager);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setText(getString(R.string.app_name));
        TextView rightButton = (TextView) toolbar.findViewById(R.id.rightButton);
        rightButton.setText("Sign Off");
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutAndClose();
            }
        });
        View leftButton = toolbar.findViewById(R.id.leftButton);
        leftButton.setVisibility(View.INVISIBLE);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        toolbarWrapper = new ToolbarWrapper();
        toolbarWrapper.title = title;
        toolbarWrapper.rightButton = rightButton;
        toolbarWrapper.leftButton = leftButton;
        pagerAdapter = new FragmentAdapter(getChildFragmentManager(), getContext());
        pager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View tabView = pagerAdapter.getTabView(i);
            if (i > 0) {
                tabView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            }
            tab.setCustomView(tabView);
        }
        tabLayout.getTabAt(0).getCustomView().setSelected(true);

        pager.addOnPageChangeListener(new OnPageChangeToolbarHandler(pagerAdapter, toolbarWrapper,
                                                                     getString(R.string.app_name)));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
    }

    private void logOutAndClose() {
        MashupUtilities.logOut(pegaAuthenticator);
        FragmentUtils.goHome(getActivity());
    }

    @Override
    public boolean onBackAction() {
        Fragment currentFragment = pagerAdapter.getRegisteredFragment(pager.getCurrentItem());
        if (currentFragment != null && currentFragment instanceof BackAction) {
            if (!((BackAction) currentFragment).onBackAction()) {
                MashupUtilities.logOut(pegaAuthenticator);
                return false;
            }
            return true;
        }
        return false;
    }

    ToolbarWrapper getToolbarWrapper() {
        return toolbarWrapper;
    }

    TabLayout getTabLayout() {
        return tabLayout;
    }

    class ToolbarWrapper {
        TextView title;
        TextView rightButton;
        View leftButton;
    }
}
