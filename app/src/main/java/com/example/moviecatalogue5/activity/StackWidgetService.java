package com.example.moviecatalogue5.activity;

import android.content.Intent;
import android.widget.RemoteViewsService;
import com.example.moviecatalogue5.adapter.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
