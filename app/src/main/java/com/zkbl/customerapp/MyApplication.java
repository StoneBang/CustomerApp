package com.zkbl.customerapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;


import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.zkbl.customerapp.greenDao.DaoMaster;
import com.zkbl.customerapp.greenDao.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        initBugly();
    }

    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        Bugly.init(getApplicationContext(), "6877214ecd", false);
    }


    public void initDataBase(){
        DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(getApplicationContext(),"cassette");
        SQLiteDatabase dbDataBase = dbHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(dbDataBase);
        daoSession = daoMaster.newSession();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
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
