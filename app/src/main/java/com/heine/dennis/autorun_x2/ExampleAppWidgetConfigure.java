package com.heine.dennis.autorun_x2;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class ExampleAppWidgetConfigure extends AppCompatActivity {
    boolean widgetExists(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        AppWidgetProviderInfo info = appWidgetManager.getAppWidgetInfo(appWidgetId);
        return (info != null);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_example_app_widget_configure);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View menuLayout = inflater.inflate(R.layout.activity_example_app_widget_configure, null, false);
        setContentView(menuLayout);

        Intent intent1 = getIntent();
        Bundle extras1 = intent1.getExtras();

        int appWidgetId1 = 0;
        if (extras1 != null) {
            appWidgetId1 = extras1.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if ( !ExampleAppWidgetProvider.config_done/*widgetExists(getApplicationContext(), appWidgetId1)*/) {
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            ExampleAppWidgetProvider.started=true;
            PackageManager pm = getPackageManager();

        /*
        List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
*/

            LinearLayout linearLayout = (LinearLayout) menuLayout.findViewById(R.id.linearlayout1);

            List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packList.size(); i++) {
                PackageInfo packInfo = packList.get(i);
                if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();

                    // PackageInfo p = packs.get(i);

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(appName);
                    //          textView.setText(p.applicationInfo.loadLabel(getPackageManager()).toString());
                    textView.setTag(packInfo.packageName);
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setTextColor(Color.BLACK);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setTextSize(35);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView tv = (TextView) v;
                            String text = tv.getTag().toString();
                            SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = settings.edit();
                            edit.putString("intent", text);
                            edit.apply();

                            //apply
                            Intent intent = getIntent();
                            Bundle extras = intent.getExtras();
                            int appWidgetId = 0;
                            if (extras != null) {
                                appWidgetId = extras.getInt(
                                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                                        AppWidgetManager.INVALID_APPWIDGET_ID);
                            }

                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

                            RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(),
                                    R.layout.example_appwidget);
                            appWidgetManager.updateAppWidget(appWidgetId, views);

                            Intent resultValue = new Intent();
                            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                            setResult(RESULT_OK, resultValue);
                            ExampleAppWidgetProvider.config_done=true;
                            finish();

                        }
                    });
                    linearLayout.addView(textView);
                    linearLayout.invalidate();
                }
            }
/*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                int appWidgetId=0;
                if (extras != null) {
                    appWidgetId = extras.getInt(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(),
                        R.layout.example_appwidget);
                appWidgetManager.updateAppWidget(appWidgetId, views);

                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });*/
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "One widget maximum. Please remove old widget first.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
        }
    }
}