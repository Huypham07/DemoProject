package com.example.demoproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.demoproject.sqlite.DBHelper;
import com.example.demoproject.sqlite.DBManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // Database
    private DBManager dbManager;

    // user name
    private String userName;
    private long userID;

    private int numberReminder = -1;
    private GridView gv;
    private ArrayList<ItemOverview> itemOverviewArrayList = new ArrayList<>();
    private OverviewAdapter overviewAdapter;

    private ListView lv;
    private ArrayList<ItemReminder> itemReminderArrayList = new ArrayList<>();
    private ReminderAdapter reminderAdapter;

    //----DATE----
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

    //---REQUEST CODE INTENT------
    private static final int REQUEST_CODE = 1;

    //----RECEIVER-------
    AirplainModeReceiver airplainModeReceiver = new AirplainModeReceiver();
    IntentFilter airplainIntentFilter;
    MailSendingReceiver mailSendingReceiver = new MailSendingReceiver();
    IntentFilter mailIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        dbManager.open();
        userName = getIntent().getStringExtra("userName");
        userID = getIntent().getLongExtra("userID", -1);

        // create action bar
        {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            customActionBar(actionBar);
        }

        // create tab host
        {
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
        }

        // create more components

        gv = findViewById(R.id.gridView);
        itemOverviewArrayList.add(new ItemOverview("Today", R.drawable.calendar_today, 0));
        itemOverviewArrayList.add(new ItemOverview("Scheduled", R.drawable.calendar, 0));
        itemOverviewArrayList.add(new ItemOverview("All", R.drawable.tray, 0));
        itemOverviewArrayList.add(new ItemOverview("Completed", R.drawable.check, 0));
        itemOverviewArrayList.add(new ItemOverview("Flagged", R.drawable.flagged, 0));
        overviewAdapter = new OverviewAdapter(this, itemOverviewArrayList);
        updateNumberReminder();

        registerForContextMenu(gv);

        lv = findViewById(R.id.listview);
        reminderAdapter = new ReminderAdapter(this, itemReminderArrayList);
        updateReminderList();

        // handle event
        TextView datetxt = (TextView) findViewById(R.id.textView4);
        Date date = myCalendar.getTime();
        datetxt.setText(String.format("%02d/%02d/%04d", date.getDay(), date.getMonth(), date.getYear()));
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

        //----POPUP MENU-------
        ImageButton popupMenuBtn = (ImageButton) findViewById(R.id.popupMenu);
        popupMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, popupMenuBtn);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.save) {
                            EditText title = (EditText) findViewById(R.id.titlereminder);
                            try {
                                dbManager.insertReminder(userID, title.getText().toString(), datetxt.getText().toString(), timetxt.getText().toString());
                                updateNumberReminder();
                                addNewReminder(new ItemReminder(title.getText().toString(), datetxt.getText().toString(), timetxt.getText().toString()));
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        airplainIntentFilter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplainModeReceiver, airplainIntentFilter);
        mailIntentFilter = new IntentFilter(Intent.ACTION_SEND);
        mailIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            mailIntentFilter.addDataType("message/rfc822");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException(e);
        }
        registerReceiver(mailSendingReceiver, mailIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mailSendingReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(airplainModeReceiver);
    }

    //--------CONTEXT MENU---------
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
            dbManager.clearAllReminder(userID);
            updateReminderList();
            updateNumberReminder();
            Toast.makeText(MainActivity.this, "You have cleared all", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    //------OPTIONS MENU---------
    private void customActionBar(ActionBar actionBar) {
        actionBar.setTitle("  " + actionBar.getTitle());
        actionBar.setSubtitle("   " + userName);
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
            intent.putExtra("request", "Show Settings");
            startActivityForResult(intent, REQUEST_CODE);
        } else if (item.getItemId() == R.id.optItem2) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"huyhochoi541@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about the app");
            // chạy trực tiếp intent
//            startActivity(Intent.createChooser(intent, "Choose a mail app"));
            //gửi thông báo qua receiver và chạy khi receiver nhận được
            sendBroadcast(intent);
        } else if (item.getItemId() == R.id.optItem3) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("íslogout", true);
            startActivity(intent);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra("result");
            Toast.makeText(MainActivity.this, resultData, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateNumberReminder() {
        try {
            numberReminder = dbManager.getNumberOfRemider(userID);
            for (int i = 0; i < itemOverviewArrayList.size(); i++) {
                itemOverviewArrayList.get(i).setNotice(numberReminder);
            }
            gv.setAdapter(overviewAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateReminderList() {
        try {
            itemReminderArrayList.clear();
            Cursor cursor = dbManager.getAllReminder(userID);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                    @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                    ItemReminder reminder = new ItemReminder(title, date, time);
                    itemReminderArrayList.add(reminder);
                } while (cursor.moveToNext());
            }
            cursor.close();
            lv.setAdapter(reminderAdapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewReminder(ItemReminder reminder) {
        itemReminderArrayList.add(reminder);
        lv.setAdapter(reminderAdapter);
    }
}
