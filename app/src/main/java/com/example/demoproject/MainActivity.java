package com.example.demoproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR, i);
            myCalendar.set(Calendar.MONTH, i1);
            myCalendar.set(Calendar.DAY_OF_MONTH, i2);
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            myCalendar.set(Calendar.HOUR_OF_DAY, i);
            myCalendar.set(Calendar.MINUTE, i1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        customActionBar(actionBar);

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

        GridView gv;
        gv = findViewById(R.id.gridView);
        ArrayList<ItemOverview> itemOverviewArrayList = new ArrayList<>();

        itemOverviewArrayList.add(new ItemOverview("Today", R.drawable.calendar_today, 3));
        itemOverviewArrayList.add(new ItemOverview("Scheduled", R.drawable.calendar, 10));
        itemOverviewArrayList.add(new ItemOverview("All", R.drawable.tray, 10));
        itemOverviewArrayList.add(new ItemOverview("Completed", R.drawable.check, 15));
        itemOverviewArrayList.add(new ItemOverview("Flagged", R.drawable.flagged, 2));

        OverviewAdapter overviewAdapter = new OverviewAdapter(this, itemOverviewArrayList);
        gv.setAdapter(overviewAdapter);

        registerForContextMenu(gv);

        TextView datetxt = (TextView) findViewById(R.id.textView4);
        ImageButton dateBtn = (ImageButton) findViewById(R.id.date);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                datetxt.setText(String.format("%02d/%02d/%04d", i2, i1, i));
                            }
                        },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TextView timetxt = (TextView) findViewById(R.id.textView5);
        ImageButton timeBtn = (ImageButton) findViewById(R.id.time);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                timetxt.setText(String.format("%02d:%02d", i, i1));
                            }
                        },
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        ImageButton popupMenuBtn = (ImageButton) findViewById(R.id.popupMenu);
        popupMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, popupMenuBtn);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Share");
        menu.add(0, v.getId(), 0, "Clear all");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "Share") {
            Toast.makeText(MainActivity.this, "You have shared"
                    , Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "Clear all") {
            Toast.makeText(MainActivity.this, "You have cleared all", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void customActionBar(ActionBar actionBar) {
        actionBar.setTitle("  " + actionBar.getTitle());
        actionBar.setSubtitle("   This app is demo for slide");
        actionBar.setLogo(R.drawable.logo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.optItem1) {
            Intent intent = new Intent(this, MainActivity2.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.optItem2) {
            Toast.makeText(MainActivity.this, "Pham Huu Huy", Toast.LENGTH_SHORT).show();
        }
        return (super.onOptionsItemSelected(item));
    }
}