package com.microape.easybt.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Created by pengle on 2018-09-13.
 * email:pengle609@163.com
 */

public interface OnBTScanCallBack {

    void onBTDiscoveryStarted();

    void onBTFound(BluetoothDevice device);

    /*void onSearchStarted();

    void onSearchFound(BluetoothDevice device);*/

    void onBTDiscoveryFinished();

}
