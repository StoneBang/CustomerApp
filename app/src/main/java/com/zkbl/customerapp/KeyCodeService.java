package com.zkbl.customerapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.module.interaction.ModuleConnector;
import com.nativec.tools.ModuleManager;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.ReaderSetting;
import com.rfid.rxobserver.bean.RXInventoryTag;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by StoneBang at 2021/10/21
 */
public class KeyCodeService extends AccessibilityService {

    private SoundPool mSoundPool;
    private ModuleConnector connector;
    private int baud = 115200;
    private RFIDReaderHelper mReaderHelper;
    private ReaderSetting mReaderSetting = ReaderSetting.newInstance();
    private String mode;
    private static final String TAG = "COONECTRS232";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.e("TAG", "keyEvent"+ event.getKeyCode() );
        if(event.getKeyCode() == 134){
            GreenDaoUtils.getInstance(getApplicationContext()).deleteRFID();
            mReaderHelper.customizedSessionTargetInventory(ReaderSetting.newInstance().btReadId,
                    (byte) 1, (byte) 0, Byte.parseByte("1"));
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onServiceConnected()
    {
        Log.v("TAG", "***** onServiceConnected");
        initRFID();
        EventBus.getDefault().register(this);

//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
//        info.notificationTimeout = 100;
//        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
//        setServiceInfo(info);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void excuteCommand(CommandMessage command){
        String cmd = command.getCommand();
        String cmdId = command.getCommandId();
        mode = command.getMode();
        int power = command.getPower();
        if(cmd.contains("startReceive")){
            ModuleManager.newInstance().setUHFStatus(true);
            if("单标签".equals(mode)){

            }else if("多标签".equals(mode)){

            }else{

            }
            setPower(power);
            Log.e(TAG, "excuteCommand: 上电成功" );
        }else if(cmd.contains("stopReceive")){
            ModuleManager.newInstance().setUHFStatus(false);
            Log.e(TAG, "excuteCommand: 上电失败" );
        }else if(cmd.contains("setPower")){
            setPower(power);
        }else if(cmd.contains("getDeviceInfo")){
            sendDeviceInfo(cmdId);
            //打印设备信息
            //System.out.println(Build.MANUFACTURER +"\n"+Build.MODEL+"\n"+Tools.getSerialNumber());
        }else{ }
    }

    //设置发射功率
    public void setPower(int power){
        //在指定范围内才让你设置
        if(power>=15 && power<=30){
            byte btOutputPower = 0x00;
            try {
                btOutputPower = (byte)power;
                mReaderHelper.setOutputPower(mReaderSetting.btReadId, btOutputPower);
                mReaderSetting.btAryOutputPower = new byte[]{btOutputPower};
                Log.e(TAG, "setPower: success");
            } catch (Exception e) {
                Log.e(TAG, "setPower: fail");
            }
        }
    }


    //初始化RFID
    public void initRFID() {
        try {
            connector = new ReaderConnector();
            if (connector.connectCom("dev/ttyS4", baud)) {
                try {
                    ModuleManager.newInstance().setUHFStatus(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "RFID上电失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    mReaderHelper = RFIDReaderHelper.getDefaultHelper();
                    mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
                    mSoundPool.load(this, R.raw.beeper, 1);
                    mSoundPool.load(this, R.raw.beeper_short, 2);
                    mSoundPool.load(this, R.raw.operatesuccessful, 3);
                    mSoundPool.load(this, R.raw.operationfailed, 4);
                    Thread.sleep(500);
//                    int result = mReaderHelper.setTrigger(false);
//                    if (result == -1) {
//                        Toast.makeText(this, "禁止默认操作失败", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    mReaderHelper.registerObserver(new RXObserver(){
                        @Override
                        protected void onExeCMDStatus(byte cmd, byte status) {
                            super.onExeCMDStatus(cmd, status);
                        }

                        @Override
                        protected void onInventoryTagEnd(RXInventoryTag.RXInventoryTagEnd tagEnd) {
                            super.onInventoryTagEnd(tagEnd);
//                            BeeperUtils.beep(BeeperUtils.BEEPER);
                            mSoundPool.play(2, 1, 1, 0, 0, 1);
                            if("单标签".equals(mode)){

                            }else if("多标签".equals(mode)){

                            }
                            sendEpc();
                            Log.e(TAG, "盘存结束" );
                        }

                        @Override
                        protected void onInventoryTag(RXInventoryTag tag) {
                            super.onInventoryTag(tag);
                            String epc = tag.strEPC.replaceAll(" ", "");
                            GreenDaoUtils.getInstance(getApplicationContext()).insertRFID(epc);
                            Log.e(TAG, "onInventoryTag: "+epc );
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "错误错误"+e.getMessage() );
                    return;
                }


            } else {
                Toast.makeText(this, "连接ttyS4失败", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //发送多扫描数据
    public void sendEpc(){
        Map<String,String> data = new HashMap<>();
        data.put("status","success");
        data.put("commandId","rfid-data");
        List<String> epcList =  GreenDaoUtils.getInstance(getApplicationContext()).queryRFID();
        StringBuilder resBuild = new StringBuilder();
        for (int i=0;i<epcList.size();i++){
            if(i==epcList.size()-1){

                resBuild.append(epcList.get(i));
            }else{
                resBuild.append(epcList.get(i)+",");
            }
        }
        data.put("data",resBuild.toString());
        data.put("message","scan-ok");

        JSONObject jsonObject = new JSONObject(data);
        Intent callBack = new Intent();
        callBack.setAction("com.cdgsafety.pms.rfid.data");
        callBack.putExtra("data",jsonObject.toString());
        sendBroadcast(callBack);
        Log.e(TAG, "我方发送RFID给他了");
    }


    //发送单扫描数据
    public void sendSingleEpc(){
        Map<String,String> data = new HashMap<>();
        data.put("status","success");
        data.put("commandId","rfid-data");
        List<String> epcList =  GreenDaoUtils.getInstance(getApplicationContext()).queryRFID();
        StringBuilder resBuild = new StringBuilder();
        for (int i=0;i<1;i++){
                resBuild.append(epcList.get(i));

        }
        data.put("data",resBuild.toString());
        data.put("message","scan-ok");

        JSONObject jsonObject = new JSONObject(data);
        Intent callBack = new Intent();
        callBack.setAction("com.cdgsafety.pms.rfid.data");
        callBack.putExtra("data",jsonObject.toString());
        sendBroadcast(callBack);
        Log.e(TAG, "我方发送RFID给他了");
    }



    //发送扫描数据
    public void sendDeviceInfo(String commandId){
        Map<String,String> data = new HashMap<>();
        data.put("status","success");
        data.put("commandId",commandId);
        data.put("data",getDeviceJsonString());
        data.put("message","deviceInfo-ok");
        JSONObject jsonObject = new JSONObject(data);
        Intent callBack = new Intent();
        callBack.setAction("com.cdgsafety.pms.rfid.data");
        callBack.putExtra("data",jsonObject.toString());
        sendBroadcast(callBack);
        Log.e(TAG, "设备信息发送成功给他了"+jsonObject.toString());
    }

    public String getDeviceJsonString(){
        Map<String,String> deviceInfo = new HashMap<>();
        deviceInfo.put("deviceName",Build.MODEL);
        deviceInfo.put("factroyName",Build.MANUFACTURER);
        deviceInfo.put("deviceSN",Tools.getSerialNumber());
        JSONObject deviceJsonObject = new JSONObject(deviceInfo);
        return deviceJsonObject.toString();
    }
}
