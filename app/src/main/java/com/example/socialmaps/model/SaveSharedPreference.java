package com.example.socialmaps.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("PREF_USER_NAME", userName);
        editor.commit();
    }

    public static void setUserID(Context ctx, String userID)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("PREF_USER_ID", userID);
        editor.commit();
    }

    public static void setToken(Context ctx, String token)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("PREF_TOKEN", token);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString("PREF_USER_NAME", "");
    }

    public static String getUserID(Context ctx)
    {
        return getSharedPreferences(ctx).getString("PREF_USER_ID", "");
    }

    public static String getToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString("PREF_TOKEN", "");
    }
}
