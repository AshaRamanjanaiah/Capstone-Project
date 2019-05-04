package com.example.android.hindutemple.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceUtils {

    private static final String TAG = SharedPreferenceUtils.class.getSimpleName();

    private SharedPreferences sharedPref;

    public SharedPreferenceUtils(Context context){
        sharedPref = context.getSharedPreferences(
                "temple_info", Context.MODE_PRIVATE);
    }

    public void saveToSharedPreference(String key, String value){

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();

        Log.d(TAG , "Temple Id saved");
    }

    public String  readValueFromSharedPreference(String key){
        return sharedPref.getString(key, "Empty");
    }
}
