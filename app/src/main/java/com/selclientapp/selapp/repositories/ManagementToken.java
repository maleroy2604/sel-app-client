package com.selclientapp.selapp.repositories;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import static com.selclientapp.selapp.App.context;

public class ManagementToken {

    private static final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

    // -----------------
    // ACTION FOR REPO-PACKAGE
    // -----------------
    static void saveToken(String token) {
        String JWTtoken = "JWT " + token;
        pref.edit().putString("TOKEN", JWTtoken).apply();
        pref.edit().putLong("SAVATIMETOKEN", new Date().getTime()).apply();
    }

    static String getToken() {
        return pref.getString("TOKEN", "NO TOKEN FOUND");
    }

    static void savecurrentUsername(String username, String password) {
        pref.edit().putString("CURRENTUSERNAME", username).apply();
        pref.edit().putString("PASSWORD", username).apply();
    }

    static void saveCurrentId(int userId){
        pref.edit().putInt("CURRENTID",userId).apply();
    }



    // -----------------
    // ACTION FOR ALL-APP
    // -----------------

    public static boolean contains(String token) {
        return pref.contains(token);
    }

    public static boolean hasToRefreshToken(Date date) {
        return date.getTime() - pref.getLong("SAVATIMETOKEN", 0) >=7200000;
    }

    public static TokenBody getCurrentTokenBody() {
        return new TokenBody(pref.getString("CURRENTUSERNAME", ""), pref.getString("PASSWORD", ""));
    }

    public static void logOut(){
        pref.edit().clear().apply();
    }

    public static int getCurrentId(){
        return pref.getInt("CURRENTID", 0);
    }
}
