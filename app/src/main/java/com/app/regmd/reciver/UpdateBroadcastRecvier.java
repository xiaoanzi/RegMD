package com.app.regmd.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.regmd.service.UpdateService;

/**
 * Created by 王海 on 2015/4/8.
 */
public class UpdateBroadcastRecvier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context, UpdateService.class);
        context.startService(intent2);
    }
}