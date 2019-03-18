package com.microape.easybt.receiver;

import android.content.Context;
import android.content.IntentFilter;

import com.microape.easybt.callback.BTAdapter;
import com.microape.easybt.callback.OnBTConnCallBack;
import com.microape.easybt.callback.OnBTOpenCallBack;
import com.microape.easybt.callback.OnBTScanCallBack;

/**
 * Created by pengle on 2018-11-23.
 * email:pengle609@163.com
 */
public class BTReceiverManager {

    private boolean registerTag = false;
    private BTStatus btStatus = BTStatus.newInstance();
    private BTAdapter btAdapter = new BTAdapter(btStatus);
    private BlueToothReceiver blueToothReceiver = new BlueToothReceiver(btAdapter);

    public BTReceiverManager() {

    }

    public void registerReceiver(Context context){
        if (registerTag){
            return;
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BTAction.EXTRA_STATE);
        filter.addAction(BTAction.ACTION_STATE_CHANGED);

        filter.addAction(BTAction.ACTION_DISCOVERY_STARTED);
        filter.addAction(BTAction.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BTAction.ACTION_FOUND);
        filter.addAction(BTAction.EXTRA_DEVICE);

        filter.addAction(BTAction.ACTION_ACL_CONNECTFAIL);
        filter.addAction(BTAction.ACTION_ACL_CONNECTED);
        filter.addAction(BTAction.ACTION_ACL_DISCONNECTED);
        context.registerReceiver(blueToothReceiver, filter);
        registerTag = true;
    }

    public BTStatus getBTStatus() {
        return btStatus;
    }

    public void setOpenCallBack(OnBTOpenCallBack onBTOpenCallBack) {
        btAdapter.setOnBTOpenCallBack(onBTOpenCallBack);
    }

    public void setSearchCallBack(OnBTScanCallBack onBTScanCallBack) {
        btAdapter.setOnBTScanCallBack(onBTScanCallBack);
    }

    public void setConnCallBack(OnBTConnCallBack onBTConnCallBack) {
        btAdapter.setOnBTConnCallBack(onBTConnCallBack);
    }

    public void unRegisterReceiver(){
        try {
            if (registerTag){
                // TODO: 2018-11-23 似乎不需要注销
                //unregisterReceiver(blueToothReceiver);
                registerTag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
