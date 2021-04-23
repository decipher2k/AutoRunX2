package com.heine.dennis.autorun_x2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    public static boolean started=false;
    public static boolean config_done=false;
    private static Context _context=null;
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        config_done=false;
        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("intent", "");
        edit.apply();
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        _context=context;
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ExampleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
           // views.setOnClickPendingIntent(R.id.button, pendingIntent);




            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        SharedPreferences settings = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        String classname=settings.getString("intent","");
        if(!started && classname!="")
        {
            started=true;

            Intent intent = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage(classname);
            if (intent != null) {

                // We found the activity now start the activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
          /*  Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.termux","com.termux.MainActivity"));
            context.startActivity(intent);*/
        }


    }

}