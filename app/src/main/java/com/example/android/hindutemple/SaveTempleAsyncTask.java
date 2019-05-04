package com.example.android.hindutemple;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.utils.Constants;
import com.example.android.hindutemple.utils.SharedPreferenceUtils;

class SaveTempleAsyncTask extends AsyncTask<Temples, Void, Void> {

    private SharedPreferenceUtils sharedPreferenceUtils;
    private Boolean cardClicked;

    SaveTempleAsyncTask(Context context, Boolean cardClicked){
        sharedPreferenceUtils = new SharedPreferenceUtils(context);
        this.cardClicked = cardClicked;
    }

    @Override
    protected Void doInBackground(Temples... temples) {

        if(temples[0] == null){
            return null;
        }

        String tempIdValue = temples[0].getTempleId();
        String templeNameValue = temples[0].getTempleName();

        String templeIdKey = Constants.FIRST_VISITED_TEMPLE_ID;
        String templeNamekey = Constants.FIRST_VISITED_TEMPLE_NAME;

        if(cardClicked){
            templeIdKey = Constants.LAST_VISITED_TEMPLE_ID;
            templeNamekey = Constants.LAST_VISITED_TEMPLE_NAME;
        }

        sharedPreferenceUtils.saveToSharedPreference(templeIdKey, tempIdValue);
        sharedPreferenceUtils.saveToSharedPreference(templeNamekey, templeNameValue);

        return null;
    }
}
