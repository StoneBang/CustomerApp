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

    public void sendClick(View view) {
        Intent intent = new Intent();
        intent.setAction("com.cdgsafety.pms.rfid.command");

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("{");
//        stringBuilder.append("\"");
//        stringBuilder.append("command");
//        stringBuilder.append(":");
//        stringBuilder.append("\"");
//        stringBuilder.append("startReceive");
//        stringBuilder.append("\"");
//        stringBuilder.append(",");
//        stringBuilder.append("\"");
//        stringBuilder.append("data");
//        stringBuilder.append("\"");
//        stringBuilder.append(":");
//        stringBuilder.append("\"\"");
//        stringBuilder.append("}");
//        intent.putExtra("command",stringBuilder.toString());

//        Command command = new Command("startReceive","");
        Map<String,String> command = new HashMap<>();
        command.put("command","startReceive");
        command.put("data","");
        JSONObject jsonObject = new JSONObject(command);
        intent.putExtra("command",jsonObject.toString());
        sendBroadcast(intent);
    }

    public void cancelClick(View view) {
        Intent intent = new Intent();
        intent.setAction("com.cdgsafety.pms.rfid.command");
        Map<String,String> command = new HashMap<>();
        command.put("command","stopReceive");
        command.put("data","");
        JSONObject jsonObject = new JSONObject(command);
        intent.putExtra("command",jsonObject.toString());
        sendBroadcast(intent);

    }
}