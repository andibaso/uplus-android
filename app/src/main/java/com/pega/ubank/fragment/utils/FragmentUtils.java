/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.pega.commontools.androidlogger.ALog;
import com.pega.ubank.R;

public class FragmentUtils {
    private static final String TAG = "FragmentUtils";

    private FragmentUtils() {
        // Private constructor
    }

    public static void showFragment(FragmentActivity activity, int layoutId, Class<? extends Fragment> fragmentClass) {
        try {
            Fragment fragment = fragmentClass.newInstance();
            showFragment(activity, layoutId, fragment);
        } catch (InstantiationException e) {
            ALog.e(TAG, "Unable to replace fragment", e);
        } catch (IllegalAccessException e) {
            ALog.e(TAG, "Unable to replace fragment", e);
        }
    }

    public static void showFragment(FragmentActivity activity, int layoutId, Fragment fragment) {
        if (activity == null) {
            return;
        }
        activity.getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(layoutId, fragment)
                .show(fragment)
                .commit();
    }

    public static void replaceFragment(FragmentActivity activity, int layoutId, Fragment fragment) {
        if (activity == null) {
            return;
        }
        activity.getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(layoutId, fragment)
                .show(fragment)
                .commit();
    }

    public static void addChildFragment(FragmentManager childFragmentManager, int layoutId, Fragment childFragment,
                                        Bundle data, String backStackName, String breadCrumbTitle) {
        FragmentTransaction childFragTrans = childFragmentManager.beginTransaction();
        childFragment.setArguments(data);
        childFragTrans.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                           R.anim.enter_from_left, R.anim.exit_to_right);
        childFragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        childFragTrans.add(layoutId, childFragment);
        childFragTrans.addToBackStack(backStackName);
        childFragTrans.setBreadCrumbTitle(breadCrumbTitle);
        childFragTrans.commit();
    }

    public static void goBack(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            activity.finish();
        } else {
            fragmentManager.popBackStack();
        }
    }

    public static Fragment getTopFragment(FragmentActivity activity, int layoutId) {
        return activity.getSupportFragmentManager().findFragmentById(layoutId);
    }

    // Go back to first fragment
    public static void goHome(FragmentActivity activity) {
        if (activity == null) {
            return;
        }
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < fragmentCount - 1; i++) {
            fragmentManager.popBackStack();
        }
    }
}
