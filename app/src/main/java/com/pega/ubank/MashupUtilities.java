/*
 * Copyright (c) 2015 and Confidential to Pegasystems Inc. All rights reserved.
 */
package com.pega.ubank;

import android.os.AsyncTask;
import com.pega.mobile.authentication.AuthenticatorResult;
import com.pega.mobile.authentication.pega.PegaAuthenticator;
import com.pega.mobile.authentication.pega.result.PegaAuthenticatorResult;

public class MashupUtilities {
    private static String authenticatedUrl;

    private MashupUtilities() {
        // hidden
    }

    public static void setAuthenticatedUrl(String authenticatedUrl) {
        MashupUtilities.authenticatedUrl = authenticatedUrl;
    }

    public static String getAuthenticatedUrl() {
        return authenticatedUrl;
    }

    public static void logOut(final PegaAuthenticator pegaAuthenticator) {
        if (authenticatedUrl == null) {
            return;
        }

        new AsyncTask<Void,Void,PegaAuthenticatorResult>() {
            @Override
            protected PegaAuthenticatorResult doInBackground(Void... params) {
                return pegaAuthenticator.logOff(MashupUtilities.authenticatedUrl);
            }

            @Override
            protected void onPostExecute(PegaAuthenticatorResult pegaAuthenticatorResult) {
                if (pegaAuthenticatorResult.getType() == AuthenticatorResult.SUCCESS) {
                    MashupUtilities.authenticatedUrl = null;
                }
            }
        }.execute();
    }
}
