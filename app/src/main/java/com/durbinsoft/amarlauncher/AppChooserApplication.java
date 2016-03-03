package com.durbinsoft.amarlauncher;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppChooserApplication extends ListActivity {

    String[] appLabel;
    Drawable[] icon;
    String[] appPackage;

    ApplicationPackage aPackage;
    PreferenceClassForData sPrefs;

    ListView lv;
    TextView tv;

    private int appCount = 0;
    private  int [] selectedApps = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_chooser_application);

        lv = getListView();
        tv = (TextView)findViewById(R.id.appChooserItemsHeader);
        sPrefs = new PreferenceClassForData(this);


        initializeEverything();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {
                //after each click call a slide in animation and refresh the view

                if(appCount<4){
                    selectedApps[appCount] = position;
                    appCount++;
                    switch (appCount){
                        case 1:
                            initializeEverything();
                            tv.setText("Select Your Messaging App");
                            break;
                        case 2:
                            initializeEverything();
                            tv.setText("Select Your Browser App");
                            break;
                        case 3:
                            initializeEverything();
                            tv.setText("Select Your Camera App");
                            break;

                    }
                }

                if(appCount == 4){
                    sPrefs.setApps(selectedApps[0],selectedApps[1],selectedApps[2],selectedApps[3]);
                    sPrefs.setBool(false);
                    finish();
                }
            }
        });

    }

    private void initializeEverything(){
        getAllList();

        CustomAppListArrayAdapter adapter = new CustomAppListArrayAdapter(this, appLabel, icon);
        setListAdapter(adapter);
    }

    private void getAllList() {
        aPackage = new ApplicationPackage();
        appLabel = aPackage.getAppLabels();
        icon = aPackage.getIcons();
        appPackage = aPackage.getPackageNames();
    }
}
