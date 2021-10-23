package com.zkbl.customerapp;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by StoneBang at 2021/10/15
 */
@Entity
public class RFIDBean {
    public String RFID;

    @Generated(hash = 1528084323)
    public RFIDBean(String RFID) {
        this.RFID = RFID;
    }

    @Generated(hash = 782077272)
    public RFIDBean() {
    }

    public String getRFID() {
        return this.RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }


}
