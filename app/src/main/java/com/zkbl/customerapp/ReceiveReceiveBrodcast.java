package com.zkbl.customerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by StoneBang at 2021/10/15
 */
public class ReceiveReceiveBrodcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String res = intent.getStringExtra("command");
        System.out.println("我方接收到命令成功" + res);
        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setCommand(res);
        EventBus.getDefault().post(commandMessage);
    }
}
