/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uminho.uce15.cityroots;

import com.google.android.gms.common.ConnectionResult;
import android.content.Context;
import android.content.res.Resources;

import com.uminho.uce15.cityroots.R;

/**
 * Map error codes to error messages.
 */
public class LocationServiceErrorMessages {

    // Don't allow instantiation
    private LocationServiceErrorMessages() {}

    public static String getErrorString(Context context, int errorCode) {

        // Get a handle to resources, to allow the method to retrieve messages.
        Resources mResources = context.getResources();

        // Define a string to contain the error message
        String errorString="erro";

        // Decide which error message to get, based on the error code.
        switch (errorCode) {
            case ConnectionResult.DEVELOPER_ERROR:
                //errorString = mResources.getString(R.string.connection_error_misconfigured);
                errorString ="Erro";
                break;

            case ConnectionResult.INTERNAL_ERROR:
                //errorString = mResources.getString(R.string.connection_error_internal);
                errorString ="Erro";
                break;

            case ConnectionResult.INVALID_ACCOUNT:

                //errorString = mResources.getString(R.string.connection_error_invalid_account);
                errorString ="Erro";
                break;

            case ConnectionResult.LICENSE_CHECK_FAILED:
                //errorString = mResources.getString(R.string.connection_error_license_check_failed);
                errorString ="Erro";
                break;

            case ConnectionResult.NETWORK_ERROR:
                //errorString = mResources.getString(R.string.connection_error_network);
                errorString ="Erro";
                break;

            case ConnectionResult.RESOLUTION_REQUIRED:
                //errorString = mResources.getString(R.string.connection_error_needs_resolution);
                errorString ="Erro";
                break;

            case ConnectionResult.SERVICE_DISABLED:
                //errorString = mResources.getString(R.string.connection_error_disabled);
                break;

            case ConnectionResult.SERVICE_INVALID:
                //errorString = mResources.getString(R.string.connection_error_invalid);
                errorString ="Erro";
                break;

            case ConnectionResult.SERVICE_MISSING:
                //errorString = mResources.getString(R.string.connection_error_missing);
                errorString ="Erro";
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                //errorString = mResources.getString(R.string.connection_error_outdated);
                errorString ="Erro";
                break;

            case ConnectionResult.SIGN_IN_REQUIRED:
                //errorString = mResources.getString(R.string.connection_error_sign_in_required);
                errorString ="Erro";
                break;

            default:
                //errorString = mResources.getString(R.string.connection_error_unknown);
                errorString ="Erro";
                break;
        }

        // Return the error message
        return errorString;
    }
}
