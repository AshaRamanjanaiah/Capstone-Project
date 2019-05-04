package com.example.android.hindutemple;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetListviewServcie extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewFactory(this.getApplicationContext(), intent);
    }
}
