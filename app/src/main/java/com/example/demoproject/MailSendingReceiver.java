package com.example.demoproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MailSendingReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEND)) {
            Toast.makeText(context, "use receiver", Toast.LENGTH_SHORT).show();
            context.startActivity(Intent.createChooser(intent, "Choose a mail app"));
        }
    }
}