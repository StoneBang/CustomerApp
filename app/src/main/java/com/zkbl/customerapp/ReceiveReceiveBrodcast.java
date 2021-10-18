package com.zkbl.customerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by StoneBang at 2021/10/15
 */
public class ReceiveReceiveBrodcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String res = intent.getStringExtra("data");
        System.out.println("客户接收成功" + res);
    }
}
