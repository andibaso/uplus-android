/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.pega.mobile.snapstart.SnapStart;
import com.pega.mobile.snapstart.SnapStartError;
import com.pega.mobile.snapstart.SnapStartWebViewListener;
import com.pega.mobile.snapstart.snapstartrequest.SnapStartRequest;
import com.pega.mobile.webview.HybridWebView;
import com.pega.ubank.MashupUtilities;
import com.pega.ubank.R;

import java.util.concurrent.TimeUnit;

public class WebViewFragment extends Fragment {
    private static final int FADE_IN_DURATION = 700;
    private static final int TIMEOUT_SECONDS = 20;

    private String baseUrl;
    private String insClass;
    private String flowType;

    private HybridWebView webView;
    private boolean webViewLoaded = false;
    private View progressBar;
    private View mainWrapper;
    private boolean getStartedPressed = false;

    private Handler timerHandler = new Handler();
    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Dispute Error");
            builder.setMessage("Sorry, the disputes system isn’t available right now.  Please try again later.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().onBackPressed();
                }
            });
            builder.setCancelable(false);
            builder.show();
            if (webView != null) {
                webView.stopLoading();
            }
        }
    };

    private void setUpParams() {
        insClass = "Uplu-UPlusDis-Work-DisputeTransaction";
        flowType = "pyStartCase";
        baseUrl = MashupUtilities.getAuthenticatedUrl();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        final Button button = (Button) view.findViewById(R.id.button);
        button.setText("Get started");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebview();
            }
        });
        webView = (HybridWebView) view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progressWrapper);
        mainWrapper = view.findViewById(R.id.mainWrapper);
        showLoading();

        return view;
    }

    private void showLoading() {
        animateAlpha(progressBar, 1, null);
    }

    private void hideLoading() {
        Runnable endAction = new Runnable() {
            @Override
            public void run() {
                webView.setVisibility(View.VISIBLE);
                mainWrapper.setVisibility(View.INVISIBLE);
            }
        };
        animateEnterFromRight(webView, endAction);
        animateAlpha(progressBar, 0, null);
    }

    private void animateAlpha(View view, float targetAlpha, final Runnable endAction) {
        view.animate()
            .alpha(targetAlpha)
            .setDuration(FADE_IN_DURATION)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (endAction != null) {
                        endAction.run();
                    }
                }
            });
    }

    private void animateEnterFromRight(View view, final Runnable endAction) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.enter_from_right);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // not used currently
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (endAction != null) {
                    endAction.run();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // not used currently
            }
        });
        view.startAnimation(animation);
    }

    private void showWebview() {
        if (timerHandler != null) {
            timerHandler.postDelayed(timeoutRunnable, TimeUnit.SECONDS.toMillis(TIMEOUT_SECONDS));
        }

        getStartedPressed = true;
        if (webViewLoaded) {
            hideLoading();
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpParams();

        SnapStart snapStart = new SnapStart(baseUrl);
        createCase(snapStart);
    }

    private void createCase(SnapStart snapStart) {
        SnapStartRequest request = snapStart.getCreateCaseRequestBuilder()
                                          .setInsClass(insClass).setFlowType(flowType)
                                          .build();
        snapStart.executeSnapStartRequest(request, webView, new WebViewLoadingListener());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
    }

    private class WebViewLoadingListener extends SnapStartWebViewListener {
        @Override
        public void onActionFinished() {
            getActivity().onBackPressed();
        }

        @Override
        public void onActionStarted(SnapStartRequest request) {
            if (timerHandler != null) {
                timerHandler.removeCallbacks(timeoutRunnable);
            }
            timerHandler = null;

            if (getStartedPressed) {
                hideLoading();
            }
            webViewLoaded = true;
        }

        @Override
        public void onActionFailure(SnapStartRequest request, SnapStartError error) {
            showAlert();
        }

        private void showAlert() {
            if (timerHandler != null) {
                timerHandler.removeCallbacks(timeoutRunnable);
            }
            timerHandler = null;
            timeoutRunnable.run();
        }
    }
}
