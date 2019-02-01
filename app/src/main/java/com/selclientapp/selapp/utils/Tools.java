package com.selclientapp.selapp.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.selclientapp.selapp.App;

public class Tools {

    // -----------------
    // ACTION FOR ALL-APP
    // -----------------

    public static boolean hasInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static void backgroundThreadShortToast(final String msg) {
        if (App.context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
