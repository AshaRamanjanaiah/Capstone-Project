package com.example.android.hindutemple;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.hindutemple.utils.Constants;
import com.example.android.hindutemple.utils.SharedPreferenceUtils;

/**
 * Implementation of App Widget functionality.
 */
public class TempleWidgetProvider extends AppWidgetProvider {

    private static final String TAG = TempleWidgetProvider.class.getSimpleName();

    private String tempId = null;
    private String templeName = null;
    private String templeImageURL = null;

    private void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        readDataFromSharedPreference(context);

        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.temple_widget_provider);

        views.setTextViewText(R.id.appwidget_title, templeName);

        Bundle bundle = new Bundle();
        bundle.putString(Constants.TEMPLE_ID, tempId);
        bundle.putString(Constants.TEMPLE_NAME, templeName);
        bundle.putString(Constants.TEMPLE_IMAGE_URL, templeImageURL);

        Intent openActivityIntent = new Intent(context, TempleDetailActivity.class);
        openActivityIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0
                , openActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_title, pendingIntent);

        Intent intent = new Intent(context, WidgetListviewServcie.class);
        intent.putExtra(Constants.TEMPLE_ID, tempId);
        views.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent clickIntentTemplate = new Intent(context, TempleDetailActivity.class);
        clickIntentTemplate.putExtras(bundle);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void readDataFromSharedPreference(Context context) {
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);

        tempId = sharedPreferenceUtils.readValueFromSharedPreference(Constants.FIRST_VISITED_TEMPLE_ID);
        templeName = sharedPreferenceUtils.readValueFromSharedPreference(Constants.FIRST_VISITED_TEMPLE_NAME);
        templeImageURL = sharedPreferenceUtils.readValueFromSharedPreference(Constants.FIRST_VISITED_TEMPLE_IMAGE_URL);

        String templeIdLast = sharedPreferenceUtils.readValueFromSharedPreference(Constants.LAST_VISITED_TEMPLE_ID);

        if(!templeIdLast.equals("Empty")){
            tempId = templeIdLast;
            templeName = sharedPreferenceUtils.readValueFromSharedPreference(Constants.LAST_VISITED_TEMPLE_NAME);
            templeImageURL = sharedPreferenceUtils.readValueFromSharedPreference(Constants.LAST_VISITED_TEMPLE_IMAGE_URL);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        Log.d(TAG, "Widget data updated");

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

