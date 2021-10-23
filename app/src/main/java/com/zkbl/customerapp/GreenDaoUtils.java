package com.zkbl.customerapp;

import android.content.Context;
import android.database.Cursor;


import com.zkbl.customerapp.greenDao.DaoSession;
import com.zkbl.customerapp.greenDao.RFIDBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by StoneBang at 2021/10/15
 */
public class GreenDaoUtils {
    private static GreenDaoUtils instance;
    private static Context context;
    private static DaoSession daoSession;
    private final RFIDBeanDao RFIDDao;

    private GreenDaoUtils(Context context){
        this.context = context;
        this.daoSession = ((MyApplication)context.getApplicationContext()).getDaoSession();
        this.RFIDDao = daoSession.getRFIDBeanDao();
    }
    public static GreenDaoUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (GreenDaoUtils.class) {
                if (instance == null) {
                    instance = new GreenDaoUtils(context);
                }
            }
        }
        return instance;
    }
    public void insertRFID(List<RFIDBean> list){
        RFIDDao.insertInTx(list);
    }

    public void insertRFID(String epc){
        RFIDBean rfidBean = new RFIDBean();
        rfidBean.setRFID(epc);
        RFIDDao.insert(rfidBean);
    }

    public List<String> queryRFID( ){
        List<String> res = new ArrayList<>();
        Cursor cursor = daoSession.getDatabase().rawQuery("select RFID from RFIDBean",null);
        while (cursor.moveToNext()) {
            res.add(cursor.getString(cursor.getColumnIndex("RFID")));
        }
        return res;
    }

    public void deleteRFID(){
        RFIDDao.deleteAll();
    }
}
