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
        String templeImageURLValue = temples[0].getTempleImageUri();

        String templeIdKey = Constants.FIRST_VISITED_TEMPLE_ID;
        String templeNamekey = Constants.FIRST_VISITED_TEMPLE_NAME;
        String templImageURLkey = Constants.FIRST_VISITED_TEMPLE_IMAGE_URL;

        if(cardClicked){
            templeIdKey = Constants.LAST_VISITED_TEMPLE_ID;
            templeNamekey = Constants.LAST_VISITED_TEMPLE_NAME;
            templImageURLkey = Constants.LAST_VISITED_TEMPLE_IMAGE_URL;
        }

        sharedPreferenceUtils.saveToSharedPreference(templeIdKey, tempIdValue);
        sharedPreferenceUtils.saveToSharedPreference(templeNamekey, templeNameValue);
        sharedPreferenceUtils.saveToSharedPreference(templImageURLkey, templeImageURLValue);

        return null;
    }
}
