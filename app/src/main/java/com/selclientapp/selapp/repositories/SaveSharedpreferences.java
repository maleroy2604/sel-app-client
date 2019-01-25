package com.selclientapp.selapp.repositories;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import static com.selclientapp.selapp.App.context;

public class SaveSharedpreferences {

    private static SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

    // -----------------
    // ACTION FOR REPO-PACKAGE
    // -----------------
    protected static void saveToken(String token) {
        String JWTtoken = "JWT " + token;
        pref.edit().putString("TOKEN", JWTtoken).apply();
        pref.edit().putLong("SAVATIMETOKEN", new Date().getTime()).apply();
    }

    protected static String getToken() {
        return pref.getString("TOKEN", "NO TOKEN FOUND");
    }

    protected static void savecurrentUsername(String username) {
        pref.edit().putString("CURRENTUSERNAME", username).apply();
    }

    protected static boolean hasToRefreshToken(Date date) {
        return pref.getLong("SAVATIMETOKEN", 0) - date.getTime() <= 0;
    }

    // -----------------
    // ACTION FOR ALL-APP
    // -----------------

    public static boolean contains(String token) {
        return pref.contains(token);
    }

}
