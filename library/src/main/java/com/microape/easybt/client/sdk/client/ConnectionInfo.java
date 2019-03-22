package com.microape.easybt.client.sdk.client;

import android.net.wifi.ScanResult;

import java.io.Serializable;

/**
 * 连接信息服务类
 * Created by xuhao on 2017/5/16.
 */
public final class ConnectionInfo implements Serializable, Cloneable {

    /**
     * The network name.
     */
    private String mAddress;

    /**
     * The address of the access point.
     */
    private String mName;

    //private String uuid;

    private ConnectionInfo mBackupInfo;

    public ConnectionInfo(String mAddress, String mName) {
        this.mAddress = mAddress;
        this.mName = mName;
    }

    public ConnectionInfo(ScanResult mScanResult) {
        if (mScanResult != null){
            this.mAddress = mScanResult.SSID;
            this.mName = mScanResult.BSSID;
        }
    }

    public String getAddress() {
        return mAddress;
    }

    /**
     * 获取传入的端口号
     *
     * @return 端口号
     */
    public String getName() {
        return mName;
    }

    public ConnectionInfo getBackupInfo() {
        return mBackupInfo;
    }

    public void setBackupInfo(ConnectionInfo mBackupInfo) {
        this.mBackupInfo = mBackupInfo;
    }

    @Override
    public ConnectionInfo clone() {
        ConnectionInfo connectionInfo = new ConnectionInfo(mAddress, mName);
        if (mBackupInfo != null) {
            connectionInfo.setBackupInfo(mBackupInfo.clone());
        }
        return connectionInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ConnectionInfo)) { return false; }
        ConnectionInfo connectInfo = (ConnectionInfo) o;
        if (mAddress.equals(connectInfo.getAddress()) && mName.equals(connectInfo.getName())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = mAddress.hashCode();
        result = 31 * result + mName.hashCode();
        return result;
    }
}
