package com.microape.example;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.microape.easybt.EasyBT;
import com.microape.easybt.common.callback.OnBTConnCallBack;
import com.microape.easybt.common.callback.OnBTOpenCallBack;
import com.microape.easybt.common.callback.OnBTScanCallBack;

import java.util.ArrayList;

public class BTFuncActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnBTOpenCallBack, OnBTScanCallBack, OnBTConnCallBack {

    private Button btn_OpenBT, btn_CloseBT, btn_DiscoveryBT;
    private ListView lv_BTList;
    private BlueToothListAdapter blueToothListAdapter;
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();

    private EasyBT easyBT = EasyBT.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btfunc);

        btn_OpenBT = findViewById(R.id.btn_OpenBT);
        btn_CloseBT = findViewById(R.id.btn_CloseBT);
        btn_DiscoveryBT = findViewById(R.id.btn_DiscoveryBT);
        lv_BTList = findViewById(R.id.lv_BTList);
        blueToothListAdapter = new BlueToothListAdapter(this, bluetoothDevices);
        lv_BTList.setAdapter(blueToothListAdapter);

        btn_OpenBT.setOnClickListener(this);
        btn_CloseBT.setOnClickListener(this);
        btn_DiscoveryBT.setOnClickListener(this);
        lv_BTList.setOnItemClickListener(this);

        easyBT.setOpenCallBack(this);
        easyBT.setScanCallBack(this);
        easyBT.setConnCallBack(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btn_OpenBT:
                easyBT.enable();
                break;
            case  R.id.btn_CloseBT:
                easyBT.disable();
                break;
            case  R.id.btn_DiscoveryBT:
                easyBT.startDiscovery();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onBTEnable() {
        Toast.makeText(this, "蓝牙打开！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBTDisable() {
        Toast.makeText(this, "蓝牙关闭！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBTDiscoveryStarted() {
        Toast.makeText(this, "蓝牙扫描开始！", Toast.LENGTH_SHORT).show();
        bluetoothDevices.clear();
        blueToothListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBTFound(BluetoothDevice device) {
        bluetoothDevices.add(device);
        blueToothListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBTDiscoveryFinished() {
        if (bluetoothDevices.isEmpty()){
            Toast.makeText(this, "蓝牙结束，未扫到！", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "蓝牙结束！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBTConnectFail() {
        Toast.makeText(this, "蓝牙连接失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBTConnected() {
        Toast.makeText(this, "蓝牙连接成功！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBTDisConnected() {
        Toast.makeText(this, "蓝牙断开连接！", Toast.LENGTH_SHORT).show();
    }
}
