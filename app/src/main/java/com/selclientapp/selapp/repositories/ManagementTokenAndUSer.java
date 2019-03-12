package com.selclientapp.selapp.repositories;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.selclientapp.selapp.model.User;

import static com.selclientapp.selapp.App.context;

public class ManagementTokenAndUSer {

    private static final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

    // -----------------
    // ACTION FOR REPO-PACKAGE
    // -----------------
    public static void saveToken(String token) {
        String JWTtoken = "JWT " + token;
        pref.edit().putString("TOKEN", JWTtoken).apply();
    }

    static void saveUser(User user) {
        pref.edit().putInt("CURRENTID", user.getId()).apply();
        pref.edit().putInt("HOURS", user.getCounterhours()).apply();
    }

    static void saveTokenBody(TokenBody tokenBody ){
        pref.edit().putString("CURRENTUSERNAME", tokenBody.getUsername()).apply();
        pref.edit().putString("PASSWORD",tokenBody.getPassword()).apply();
    }

    // -----------------
    // ACTION FOR ALL-APP
    // -----------------

    public static boolean contains(String prefString) {
        return pref.contains(prefString);
    }

    public static TokenBody getCurrentTokenBody() {
        return new TokenBody(pref.getString("CURRENTUSERNAME", ""), pref.getString("PASSWORD", ""));
    }

    public static void logOut() {
        pref.edit().clear().apply();
    }

    public static int getCurrentId() {
        return pref.getInt("CURRENTID", 0);
    }

    public static String getToken() {
        return pref.getString("TOKEN", "NO TOKEN FOUND");
    }

    public static int getHours() {
        return pref.getInt("HOURS", 0);
    }
}
