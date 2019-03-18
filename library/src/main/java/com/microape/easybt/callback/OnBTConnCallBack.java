package com.microape.easybt.callback;

/**
 * Created by pengle on 2018-06-28.
 * email:pengle609@163.com
 */

public interface OnBTConnCallBack {

    void onBTConnectFail();

    //设备
    void onBTConnected();

    void onBTDisConnected();

}
