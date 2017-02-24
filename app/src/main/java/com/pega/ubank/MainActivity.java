/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.pega.mobile.activity.ContainerDelegate;
import com.pega.mobile.activity.ContainerDelegateProvider;
import com.pega.mobile.di.BaseActivityModule;
import com.pega.mobile.di.ContainerApplication;
import com.pega.ubank.fragment.LoginFragment;
import com.pega.ubank.fragment.utils.BackAction;
import com.pega.ubank.fragment.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {
    private ContainerDelegate container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Module must be added first before calling super.onCreate
        ContainerApplication.instance.addModule(new BaseActivityModule(this));

        container = new ContainerDelegateProvider().getContainerDelegate();

        container.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentUtils.showFragment(this, R.id.fragment_placeholder, LoginFragment.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        container.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        container.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        container.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        container.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        container.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        container.onDestroy();

        ContainerApplication.instance.resetModules();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        container.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        container.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = FragmentUtils.getTopFragment(this, R.id.fragment_placeholder);
        if (fragment instanceof BackAction && !((BackAction) fragment).onBackAction()) {
            super.onBackPressed();
        }
    }
}
