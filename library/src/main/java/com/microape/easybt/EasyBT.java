package com.microape.easybt;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import com.microape.easybt.callback.OnBTConnCallBack;
import com.microape.easybt.callback.OnBTOpenCallBack;
import com.microape.easybt.callback.OnBTScanCallBack;
import com.microape.easybt.receiver.BTReceiverManager;
import com.microape.easybt.receiver.BTStatus;

public class EasyBT {

    private static volatile EasyBT instance = null;
    private Context context;
    private BluetoothAdapter btAdapter;
    private BTReceiverManager btReceiverManager = new BTReceiverManager();

    //private BlueToothUtil blueToothUtil;
    private EasyBT() {

    }

    private static class SingletonInstance {
        private static final EasyBT INSTANCE = new EasyBT();
    }

    public static EasyBT newInstance() {
        return EasyBT.SingletonInstance.INSTANCE;
    }

    @SuppressLint("MissingPermission")
    public EasyBT init(Application context){
        this.context = context.getApplicationContext();
        btReceiverManager.registerReceiver(context);

        if (btAdapter == null){
            btAdapter = BluetoothAdapter.getDefaultAdapter();

            BTStatus.newInstance().setEnabled(btAdapter.isEnabled());
            BTStatus.newInstance().setDiscovery(btAdapter.isDiscovering());
        }
        return this;
    }

    public BTStatus btStatus() {
        return btReceiverManager.getBTStatus();
    }

    public void setOpenCallBack(OnBTOpenCallBack onBTOpenCallBack) {
        btReceiverManager.setOpenCallBack(onBTOpenCallBack);
    }

    public void setScanCallBack(OnBTScanCallBack onBTScanCallBack) {
        btReceiverManager.setSearchCallBack(onBTScanCallBack);
    }

    public void setConnCallBack(OnBTConnCallBack onBTConnCallBack) {
        btReceiverManager.setConnCallBack(onBTConnCallBack);
    }

    @SuppressLint("MissingPermission")
    public boolean isEnabled(){
        return btAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public boolean isScaning(){
        return btAdapter.isDiscovering();
    }

    @SuppressLint("MissingPermission")
    public void enable(){
        if (!btAdapter.isEnabled()) {
            btAdapter.enable();
        }
    }

    @SuppressLint("MissingPermission")
    public void disable(){
        if (btAdapter.isEnabled()) {
            btAdapter.disable();
        }
    }

    @SuppressLint("MissingPermission")
    public void startDiscovery(){
        if (!btAdapter.isEnabled()){
            Toast.makeText(context, "请先打开WiFi！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (btAdapter.isDiscovering()){
            Toast.makeText(context,"扫描已开启，勿重复开启！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!btAdapter.isDiscovering()){
            boolean startScan = btAdapter.startDiscovery();
        }
    }

    @SuppressLint("MissingPermission")
    public void cancelDiscovery(){
        if (!btAdapter.isEnabled()){
            Toast.makeText(context, "请先打开WiFi！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (btAdapter.isDiscovering()){
            btAdapter.cancelDiscovery();
        }

    }

    public boolean connDevice(BluetoothDevice bluetoothDevice){
        boolean isWiFiConn = false;
        try {
            // TODO: 2019/3/18 开启蓝牙连接，并建立通讯
            // TODO: 2019/3/18 开启蓝牙连接，并建立通讯
            // TODO: 2019/3/18 开启蓝牙连接，并建立通讯


            throw new InterruptedException();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isWiFiConn;
    }
    
}
