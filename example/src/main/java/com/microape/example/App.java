package com.microape.example;

import android.app.Application;

import com.microape.easybt.EasyBT;

/**
 *  * Author：pengl on 2019/3/13 09:42
 *  * Email ：pengle609@163.com
 *  
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EasyBT.newInstance().init(this);
    }
}
