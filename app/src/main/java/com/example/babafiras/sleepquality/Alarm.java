package com.example.babafiras.sleepquality;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Wake up",Toast.LENGTH_LONG).show();
        Vibrator v= (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(15000);
    }
}
