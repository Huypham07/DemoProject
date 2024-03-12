package com.example.demoproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<ItemReminder> {
    public ReminderAdapter(Context context, ArrayList<ItemReminder> itemReminderArrayList) {
        super(context, 0, itemReminderArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_reminder, parent, false);
        }

        ItemReminder itemReminder = getItem(position);
        TextView textView1 = listItemView.findViewById(R.id.titleitem);
        TextView textView2 = listItemView.findViewById(R.id.dateitem);
        TextView textView3 = listItemView.findViewById(R.id.timeitem);
        textView1.setText(itemReminder.getTitle());
        textView2.setText(itemReminder.getDate());
        textView3.setText(itemReminder.getTime());
        return listItemView;
    }
}
