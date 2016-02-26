package com.durbinsoft.amarlauncher;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PreferenceScreen extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_screen);

        String[] menuItems = {"hello","new menu"};

        listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter items = new ArrayAdapter<String>(this,R.layout.custom_prefs_listview_items,menuItems);
        listView.setAdapter(items);
    }
}
