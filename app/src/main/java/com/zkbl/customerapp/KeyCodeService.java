package com.zkbl.customerapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Create by StoneBang at 2021/10/21
 */
public class KeyCodeService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.e("TAG", "keyEvent"+ event.getKeyCode() );
        return super.onKeyEvent(event);
    }

    @Override
    public void onServiceConnected()
    {
        Log.v("TAG", "***** onServiceConnected");


//        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
//        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
//        info.notificationTimeout = 100;
//        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
//        setServiceInfo(info);

    }
}
