package com.selclientapp.selapp.repositories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.selclientapp.selapp.App;
import com.selclientapp.selapp.activities.MainActivity;
import com.selclientapp.selapp.model.SelApiToken;
import com.selclientapp.selapp.model.User;


import java.lang.reflect.Type;

import static android.content.Context.MODE_PRIVATE;
import static com.selclientapp.selapp.App.context;

public class ManagementTokenAndUSer {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private User user;


    public ManagementTokenAndUSer() {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        gson = new Gson();
    }

    public void saveSelApiToken(SelApiToken selApiToken) {
        String user = gson.toJson(selApiToken.getUser());
        saveAcessToken(selApiToken.getAccessToken());
        saveRefreshToken(selApiToken.getRefreshToken());
        editor.putString("USER", user);
        editor.apply();
    }

    public User getCurrentUser() {
        String json = pref.getString("USER", null);
        Type type = new TypeToken<User>() {
        }.getType();
        user = gson.fromJson(json, type);
        return user;
    }

    public boolean contains(String prefString) {
        return pref.contains(prefString);
    }

    public void logOut() {
        pref.edit().clear().apply();
        App.context.startActivity(new Intent(App.context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public String getAccessToken() {
        return pref.getString("ACCESSTOKEN", null);
    }

    public String getRefreshToken() {
        return pref.getString("REFRESHTOKEN", null);
    }

    public void saveAcessToken(String accessToken) {
        editor.putString("ACCESSTOKEN", "Bearer " + accessToken);
    }

    public void saveRefreshToken(String refreshToken) {
        editor.putString("REFRESHTOKEN", "Bearer" + refreshToken);
    }

}
