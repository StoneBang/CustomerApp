package com.zkbl.customerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by StoneBang at 2021/10/15
 */
public class ReceiveReceiveBrodcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String originCommand = intent.getStringExtra("command");//z指令
        String data = getDataFromData(originCommand);//附加数据
        String commandId = getCommandIdFromData(originCommand);//"指令唯一标识"
        String command = getCommandFromData(originCommand);
        String mode = getModeFromData(data);
        int power = getPowerFromData(data);
        System.out.println("我方接收到命令成功" + data);
        CommandMessage commandMessage = new CommandMessage();
        commandMessage.setCommand(command);
        commandMessage.setCommandId(commandId);
        commandMessage.setMode(mode);
        commandMessage.setPower(power);
        EventBus.getDefault().post(commandMessage);

    }
    public String getDataFromData(String data){
        try {
            return new JSONObject(data).getString("data").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getModeFromData(String data){
        try {
            return new JSONObject(data).getString("mode").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "多标签";
    }

    public int getPowerFromData(String data){
        try {
            return new JSONObject(data).getInt("power");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public String getCommandFromData(String data){
        try {
            return new JSONObject(data).getString("command");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCommandIdFromData(String data){
        try {
            return new JSONObject(data).getString("commandId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
