package com.microape.easybt.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

/**
 * 蓝牙广播系统分类
 */
public class BTAction {

    /**
     * 蓝牙开关变化广播
     * 蓝牙状态广播
     */
    public static final String ACTION_STATE_CHANGED = BluetoothAdapter.ACTION_STATE_CHANGED;
    public static final String EXTRA_STATE = BluetoothAdapter.EXTRA_STATE;

    /**
     *蓝牙开始、扫到、扫描结束
     */
    public static final String ACTION_DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
    public static final String ACTION_FOUND = BluetoothDevice.ACTION_FOUND;
    public static final String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
    public static final String EXTRA_DEVICE = BluetoothDevice.EXTRA_DEVICE;

    /**
     * 蓝牙连接失败、连接、断开
     */
    public static final String ACTION_ACL_CONNECTFAIL = "android.bluetooth.device.action.ACL_CONNECTFAIL";
    public static final String ACTION_ACL_CONNECTED = BluetoothDevice.ACTION_ACL_CONNECTED;
    public static final String ACTION_ACL_DISCONNECTED = BluetoothDevice.ACTION_ACL_DISCONNECTED;

    /**
     * 开始连接指定蓝牙
     * 连接指定蓝牙超时
     */
    public static final String ACTION_CONNECT_STARTED = "android.net.bluetooth.STATE_CONNECT_STARTED";
    public static final String ACTION_CONNECT_TIMEOUT = "android.net.bluetooth.STATE_CONNECT_TIMEOUT";
    public static final long timeDelay = 1000*5;
    private static boolean isConnecting = false;

    /*private static boolean willConnect = false;
    private static boolean willDisConnect = false;

    public static boolean isConnecting() {
        return isConnecting;
    }

    public static void setIsConnecting(boolean isConnecting) {
        isConnecting = isConnecting;
    }

    public static boolean isWillConnect() {
        return willConnect;
    }

    public static void setWillConnect(boolean willConnect) {
        willConnect = willConnect;
    }

    public static boolean isWillDisConnect() {
        return willDisConnect;
    }

    public static void setWillDisConnect(boolean willDisConnect) {
        willDisConnect = willDisConnect;
    }*/
}
