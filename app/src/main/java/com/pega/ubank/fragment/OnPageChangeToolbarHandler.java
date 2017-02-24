package com.pega.ubank.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.pega.ubank.adapter.FragmentAdapter;

class OnPageChangeToolbarHandler implements ViewPager.OnPageChangeListener {
    private final FragmentAdapter pagerAdapter;
    private final MainFragment.ToolbarWrapper toolbarWrapper;
    private String defaultTitle;

    OnPageChangeToolbarHandler(FragmentAdapter pagerAdapter, MainFragment.ToolbarWrapper toolbarWrapper, String defaultTitle) {
        this.pagerAdapter = pagerAdapter;
        this.toolbarWrapper = toolbarWrapper;
        this.defaultTitle = defaultTitle;
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                Fragment fragment = pagerAdapter.getRegisteredFragment(position);
                FragmentManager childFragmentManager = fragment.getChildFragmentManager();
                String fragmentName = getFragmentName(childFragmentManager);
                toolbarWrapper.title.setText(fragmentName);
                break;
            case 1:
                toolbarWrapper.leftButton.setVisibility(View.INVISIBLE);
                toolbarWrapper.rightButton.setVisibility(View.INVISIBLE);
                toolbarWrapper.title.setText(pagerAdapter.getPageTitle(position));
                break;
            default:
        }
    }

    private String getFragmentName(FragmentManager childFragmentManager) {
        return childFragmentManager.getBackStackEntryCount() > 0 ? childFragmentManager
                            .getBackStackEntryAt(childFragmentManager.getBackStackEntryCount() - 1)
                            .getBreadCrumbTitle().toString() : defaultTitle;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // empty
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //empty
    }
}
