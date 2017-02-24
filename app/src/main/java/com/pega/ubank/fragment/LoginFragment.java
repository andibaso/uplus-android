/*
 * Copyright (c) 2016 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.pega.commontools.androidlogger.ALog;
import com.pega.mobile.authentication.AuthenticatorResult;
import com.pega.mobile.authentication.pega.PegaAuthenticator;
import com.pega.mobile.authentication.pega.PegaAuthenticatorProvider;
import com.pega.mobile.authentication.pega.request.PegaActivity;
import com.pega.mobile.authentication.pega.result.PegaAuthenticatorResult;
import com.pega.mobile.commons.Credentials;
import com.pega.ubank.MashupUtilities;
import com.pega.ubank.R;
import com.pega.ubank.fragment.utils.BackAction;
import com.pega.ubank.fragment.utils.FragmentUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements BackAction {
    private static final String TAG = "LoginFragment";
    private static final String PREFS_NAME = "CredsFile";
    private static final String PREF_USER = "username";

    private Button loginButton;
    private EditText username;
    private EditText password;
    private ProgressBar loginProgress;

    private PegaAuthenticator pegaAuthenticator;
    private boolean saved;
    private String serverUrl = "https://lab0430.lab.pega.com/prweb";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) view.findViewById(R.id.login);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        loginProgress = (ProgressBar) view.findViewById(R.id.login_progress);

        pegaAuthenticator = new PegaAuthenticatorProvider().getPegaAuthenticator();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences pref = getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastUsername = pref.getString(PREF_USER, null);

        username.setText("user@uplus");
        password.setText("rules");
        if (lastUsername != null) {
            username.setText(lastUsername);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLoginButton(false);
                final Credentials credentials = new Credentials(
                        username.getText().toString(),
                        password.getText().toString()
                );

                authenticate(serverUrl, credentials);
            }
        });

        View gear = getView().findViewById(R.id.serverChanger);
        gear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        ALog.i(TAG, "Mashup application has started");
    }

    private void showInputDialog() {
        FragmentActivity activity = getActivity();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.input);
        editText.setText(serverUrl);
        alertDialogBuilder.setCancelable(false)
                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  serverUrl = editText.getText().toString();
                              }
                          })
                          .setNegativeButton("Cancel",
                                             new DialogInterface.OnClickListener() {
                                                 public void onClick(DialogInterface dialog, int id) {
                                                     dialog.cancel();
                                                 }
                                             });

        final AlertDialog alert = alertDialogBuilder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                int color = Color.parseColor("#13ACEC");
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color);
            }
        });
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loginButton.getVisibility() != View.VISIBLE) {
            toggleLoginButton(true);
        }
    }

    private void toggleLoginButton(boolean visible) {
        loginButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        loginProgress.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saved = true;
    }

    @Override
    public boolean onBackAction() {
        FragmentUtils.goBack(getActivity());
        return true;
    }

    private void authenticate(String serverUrl, Credentials credentials) {
        if (MashupUtilities.getAuthenticatedUrl() != null) {
            //already authenticated
            FragmentUtils.replaceFragment(getActivity(), R.id.fragment_placeholder, new MainFragment());
        } else {
            new LoginAsyncTask(serverUrl, credentials).execute();
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void,Void,PegaAuthenticatorResult> {
        private String serverUrl;
        private Credentials credentials;

        LoginAsyncTask(String serverUrl, Credentials credentials) {
            this.serverUrl = serverUrl;
            this.credentials = credentials;
        }

        @Override
        protected PegaAuthenticatorResult doInBackground(Void... params) {
            return pegaAuthenticator.logIn(serverUrl, credentials, PegaActivity.GET_MOBILE_APP_CONFIGURATION);
        }

        @Override
        protected void onPostExecute(PegaAuthenticatorResult result) {
            if (result.getType() != AuthenticatorResult.SUCCESS) {
                // Authentication failed
                showFailureDialog((String) result.getData());
                return;
            }

            // Authentication successful
            try {
                String authenticatedBaseUrl = ((JSONObject) result.getData()).getString("baseURL");
                URL authenticatedURL = new URL(new URL(serverUrl), authenticatedBaseUrl);

                MashupUtilities.setAuthenticatedUrl(authenticatedURL.toExternalForm());
                getActivity().getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
                        .edit()
                        .putString(PREF_USER, credentials.getUsername())
                        .apply();

                if (!saved) {
                    // fragment is still visible
                    FragmentUtils.replaceFragment(getActivity(), R.id.fragment_placeholder, new MainFragment());
                }
            } catch (JSONException e) {
                ALog.d(TAG, "Failed to get baseURL from server's response", e);
                showFailureDialog("Unable to process login info from server.");
            } catch (MalformedURLException e) {
                ALog.e(TAG, "Failed to parse authenticated URL", e);
                showFailureDialog("Unable to process login info from server.");
            }
        }

        private void showFailureDialog(String message) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Login failed: " + message)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
            toggleLoginButton(true);
        }
    }
}
