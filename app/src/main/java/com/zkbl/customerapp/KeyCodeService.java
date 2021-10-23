package com.zkbl.customerapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
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
        if(command.getCommand().contains("startReceive")){
            ModuleManager.newInstance().setUHFStatus(true);
            Log.e(TAG, "excuteCommand: 上电成功" );
        }else{
            ModuleManager.newInstance().setUHFStatus(false);
            Log.e(TAG, "excuteCommand: 上电失败" );
        }
    }
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
    public void sendEpc(){
        Map<String,String> data = new HashMap<>();
        data.put("status","success");
        data.put("message","ok");
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
        JSONObject jsonObject = new JSONObject(data);
        Intent callBack = new Intent();
        callBack.setAction("com.cdgsafety.pms.rfid.data");
        callBack.putExtra("data",jsonObject.toString());
        sendBroadcast(callBack);
        Log.e(TAG, "我方发送RFID给他了");
    }
}
