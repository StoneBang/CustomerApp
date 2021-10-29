package com.zkbl.customerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void getDeviceInfo(View view) {
        Intent intent = new Intent();
        intent.setAction("com.cdgsafety.pms.rfid.command");
        Map<String,String> command = new HashMap<>();
        command.put("command","getDeviceInfo");
        command.put("commandId","xxxxxxxx");
        Map<String,Object> data = new HashMap<>();
        data.put("mode","单标签");
        data.put("power",20);
        command.put("data",data.toString());
        JSONObject jsonObject = new JSONObject(command);
        intent.putExtra("command",jsonObject.toString());
        sendBroadcast(intent);
    }

    public void setPower(View view) {
        Intent intent = new Intent();
        intent.setAction("com.cdgsafety.pms.rfid.command");
        Map<String,String> command = new HashMap<>();
        command.put("command","setPower");
        command.put("commandId","yyyyyyy");
        Map<String,Integer> data = new HashMap<>();
        data.put("power",20);
        command.put("data",data.toString());
        JSONObject jsonObject = new JSONObject(command);
        intent.putExtra("command",jsonObject.toString());
        sendBroadcast(intent);

    }
}