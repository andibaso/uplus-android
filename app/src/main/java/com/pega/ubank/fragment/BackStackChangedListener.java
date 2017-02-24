package com.pega.ubank.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import com.pega.ubank.R;

class BackStackChangedListener implements FragmentManager.OnBackStackChangedListener {
    private final Fragment parentFragment;
    private final FragmentManager childFragmentManager;

    BackStackChangedListener(Fragment parentFragment, FragmentManager childFragmentManager) {
        this.parentFragment = parentFragment;
        this.childFragmentManager = childFragmentManager;
    }

    @Override
    public void onBackStackChanged() {
        if (parentFragment instanceof MainFragment) {
            MainFragment mainFragment = (MainFragment) parentFragment;
            MainFragment.ToolbarWrapper toolbarWrapper = mainFragment.getToolbarWrapper();
            TabLayout tabs = mainFragment.getTabLayout();

            String fragmentName = childFragmentManager.getBackStackEntryCount() > 0 ? childFragmentManager
                    .getBackStackEntryAt(childFragmentManager.getBackStackEntryCount() - 1).getName() : "accList";
            switch (fragmentName) {
                case "accDetails":
                    toolbarWrapper.leftButton.setVisibility(View.VISIBLE);
                    toolbarWrapper.rightButton.setVisibility(View.INVISIBLE);
                    toolbarWrapper.title.setText(parentFragment.getString(R.string.app_name));
                    tabs.setVisibility(View.VISIBLE);
                    break;
                case "accList":
                    toolbarWrapper.leftButton.setVisibility(View.INVISIBLE);
                    toolbarWrapper.rightButton.setVisibility(View.VISIBLE);
                    toolbarWrapper.title.setText(parentFragment.getString(R.string.app_name));
                    tabs.setVisibility(View.VISIBLE);
                    break;
                case "transDetails":
                    toolbarWrapper.title.setText("Transaction Details");
                    tabs.setVisibility(View.GONE);
                    break;
                case "disputeTransaction":
                    toolbarWrapper.title.setText("Dispute Transaction");
                    tabs.setVisibility(View.GONE);
                    break;
                default:
                    toolbarWrapper.title.setText(parentFragment.getString(R.string.app_name));
            }
        }
    }
}
