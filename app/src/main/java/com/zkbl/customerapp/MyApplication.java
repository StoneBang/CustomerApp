package com.zkbl.customerapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;


import com.zkbl.customerapp.greenDao.DaoMaster;
import com.zkbl.customerapp.greenDao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Create by StoneBang at 2021/10/15
 */
public class MyApplication extends Application {
    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();
    }


    public void initDataBase(){
        DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(getApplicationContext(),"cassette");
        SQLiteDatabase dbDataBase = dbHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(dbDataBase);
        daoSession = daoMaster.newSession();
    }
    public static class DataBaseOpenHelper extends DaoMaster.OpenHelper {

        public DataBaseOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);


        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            super.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
