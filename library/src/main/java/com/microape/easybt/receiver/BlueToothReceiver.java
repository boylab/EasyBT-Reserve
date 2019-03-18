package com.microape.easybt.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.microape.easybt.callback.BTAdapter;

/**
 * Created by pengle on 2018-09-13.
 * email:pengle609@163.com
 *
 */

public class BlueToothReceiver extends BroadcastReceiver {

    private BTAdapter btAdapter;

    public BlueToothReceiver(BTAdapter BTAdapter) {
        this.btAdapter = BTAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BTAction.ACTION_STATE_CHANGED.equals(action)) {
            int state = intent.getIntExtra(BTAction.EXTRA_STATE, -1);
            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    if (btAdapter != null ){
                        btAdapter.btStateOpen();
                    }
                    break;
                case BluetoothAdapter.STATE_OFF:
                    if (btAdapter != null ){
                        btAdapter.btStateClose();
                    }
                    break;
            }
        } else  if (BTAction.ACTION_DISCOVERY_STARTED.equals(action)) {
            if (btAdapter != null ){
                btAdapter.btScanStarted();
            }
        } else if (BTAction.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BTAction.EXTRA_DEVICE);
            if (btAdapter != null ){
                btAdapter.btScanFound(device);
            }
        }  else if (BTAction.ACTION_DISCOVERY_FINISHED.equals(action)) {
            if (btAdapter != null ){
                btAdapter.btScanFinished();
            }
        } else if (BTAction.ACTION_ACL_CONNECTFAIL.equals(action)) {
            if (btAdapter != null ){
                btAdapter.btConnectFail();
            }
        } else  if (BTAction.ACTION_ACL_CONNECTED.equals(action)) {
            if (btAdapter != null ){
                btAdapter.btConnected();
            }
        }else if (BTAction.ACTION_ACL_DISCONNECTED.equals(action)) {
            if (btAdapter != null ){
                btAdapter.btDisConnected();
            }
        }
    }

}
