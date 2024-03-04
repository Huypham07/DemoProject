package com.example.demoproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OverviewAdapter extends ArrayAdapter<ItemOverview> {
    public OverviewAdapter(@NonNull Context context, ArrayList<ItemOverview> itemOverviewArrayList) {
        super(context, 0, itemOverviewArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_overview, parent, false);
        }

        ItemOverview itemOverview = getItem(position);
        TextView textView1 = listitemView.findViewById(R.id.tv);
        TextView textView2 = listitemView.findViewById(R.id.nt);
        ImageView imageView = listitemView.findViewById(R.id.iv);
        textView1.setText(itemOverview.getName());
        textView2.setText(String.valueOf(itemOverview.getNotice()));
        imageView.setImageResource(itemOverview.getImgid());
        return listitemView;
    }
}
