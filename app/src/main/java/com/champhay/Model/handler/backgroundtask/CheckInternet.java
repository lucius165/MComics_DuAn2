package com.champhay.Model.handler.backgroundtask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public final class CheckInternet {

    public static boolean check(Activity activity) {
        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        return (conMgr != null ? conMgr.getActiveNetworkInfo() : null) != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }
}
