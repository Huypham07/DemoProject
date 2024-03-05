package com.example.demoproject;

import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabHost tabHost = (TabHost) (findViewById(R.id.tabHost));
        tabHost.setup();
        TabHost.TabSpec spec;
        spec = tabHost.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Overview");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Reminder list");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("New reminder");
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);

        gv = findViewById(R.id.gridView);
        ArrayList<ItemOverview> itemOverviewArrayList = new ArrayList<>();


        itemOverviewArrayList.add(new ItemOverview("Today", R.drawable.calendar_today, 3));
        itemOverviewArrayList.add(new ItemOverview("Scheduled", R.drawable.calendar, 10));
        itemOverviewArrayList.add(new ItemOverview("All", R.drawable.tray, 10));
        itemOverviewArrayList.add(new ItemOverview("Completed", R.drawable.check, 15));
        itemOverviewArrayList.add(new ItemOverview("Flagged", R.drawable.flagged, 2));

        OverviewAdapter overviewAdapter = new OverviewAdapter(this, itemOverviewArrayList);
        gv.setAdapter(overviewAdapter);
    }
}