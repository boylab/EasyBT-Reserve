package com.microape.easybt.callback;

import android.bluetooth.BluetoothDevice;

import com.microape.easybt.receiver.BTStatus;

/**
 *  * Author：pengl on 2019/3/13 15:18
 *  * Email ：pengle609@163.com
 *  
 */
public class BTAdapter {

    private BTStatus btStatus;
    private OnBTOpenCallBack onBTOpenCallBack;
    private OnBTScanCallBack onBTScanCallBack;
    private OnBTConnCallBack onBTConnCallBack;

    public BTAdapter(BTStatus btStatus) {
        this.btStatus = btStatus;
    }

    public void setOnBTOpenCallBack(OnBTOpenCallBack onBTOpenCallBack) {
        this.onBTOpenCallBack = onBTOpenCallBack;
    }

    public void setOnBTScanCallBack(OnBTScanCallBack onBTScanCallBack) {
        this.onBTScanCallBack = onBTScanCallBack;
    }

    public void setOnBTConnCallBack(OnBTConnCallBack onBTConnCallBack) {
        this.onBTConnCallBack = onBTConnCallBack;
    }

    public void btStateOpen(){
        if (onBTOpenCallBack != null){
            onBTOpenCallBack.onBTEnable();
        }
        btStatus.setEnabled(true);
    }

    public void btStateClose(){
        if (onBTOpenCallBack != null){
            onBTOpenCallBack.onBTDisable();
        }
        btStatus.setEnabled(false);
    }

    public void btScanStarted(){
        if (onBTScanCallBack != null){
            onBTScanCallBack.onBTDiscoveryStarted();
        }
        btStatus.setDiscovery(true);
    }

    public void btScanFound(BluetoothDevice device){
        if (onBTScanCallBack != null){
            onBTScanCallBack.onBTFound(device);
        }
    }

    public void btScanFinished(){
        if (onBTScanCallBack != null){
            onBTScanCallBack.onBTDiscoveryFinished();
        }
        btStatus.setDiscovery(true);
    }

    public void btConnectFail(){
        if (onBTConnCallBack != null){
            onBTConnCallBack.onBTConnectFail();
        }
        btStatus.setConn(false);
        //BTAction.setIsConnecting(false);
    }

    //设备
    public void btConnected(){
        if (onBTConnCallBack != null) {
            onBTConnCallBack.onBTConnected();
        }
        btStatus.setConn(true);
        //BTAction.setIsConnecting(false);
    }

    public void btDisConnected(){
        if (onBTConnCallBack != null) {
            onBTConnCallBack.onBTDisConnected();
        }
        btStatus.setConn(false);
        //BTAction.setIsConnecting(false);
    }

}
