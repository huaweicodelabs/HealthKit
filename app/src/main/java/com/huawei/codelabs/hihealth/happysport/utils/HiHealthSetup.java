
package com.huawei.codelabs.hihealth.happysport.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huawei.codelabs.hihealth.happysport.viewmodels.MainViewModel;
import com.huawei.hms.hihealth.SettingController;
import com.huawei.hms.support.account.result.AuthAccount;

/**
 * Setup environment.
 * Do login and request permission
 *
 * @since 2019-12-05
 */
public class HiHealthSetup {
    private static final String TAG = "HiHealthSetup";

    private static final int REQUEST_CODE_LOGIN = 3624;

    private static SettingController mSettingController;

    private static AuthAccount mAccount;

    private static MainViewModel mViewModel;


    /**
     * login with huawei id.
     *
     * @param activity Represent the activity object.
     */
    public static void login(Activity activity, MainViewModel viewModel) {
        Log.d(TAG, "login");

        mViewModel = viewModel;

        // TODO: login

    }

    public static void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                handleSignInResult(data);
                break;
            default:
                Log.e(TAG, "invalid request code");
        }
    }

    private static void handleSignInResult(Intent data) {
        // TODO: handleLoginResult

    }
}
