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
import com.microape.easybt.client.sdk.client.ConnectionInfo;
import com.microape.easybt.client.sdk.client.action.SocketActionAdapter;
import com.microape.easybt.client.sdk.client.connection.IConnectionManager;
import com.microape.easybt.common.callback.OnBTOpenCallBack;
import com.microape.easybt.common.callback.OnBTScanCallBack;
import com.microape.easybt.core.iocore.interfaces.IPulseSendable;
import com.microape.easybt.core.iocore.interfaces.ISendable;
import com.microape.easybt.core.pojo.OriginalData;

import java.util.ArrayList;

public class BTFuncActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, OnBTOpenCallBack, OnBTScanCallBack {

    private Button btn_OpenBT, btn_CloseBT, btn_DiscoveryBT;
    private ListView lv_BTList;
    private BlueToothListAdapter blueToothListAdapter;
    private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();

    private EasyBT easyBT = EasyBT.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btfunc);

        btn_OpenBT = findViewById(R.id.btn_OpenBT);
        btn_CloseBT = findViewById(R.id.btn_CloseBT);
        btn_DiscoveryBT = findViewById(R.id.btn_DiscoveryBT);
        lv_BTList = findViewById(R.id.lv_BTList);
        blueToothListAdapter = new BlueToothListAdapter(this, deviceList);
        lv_BTList.setAdapter(blueToothListAdapter);

        btn_OpenBT.setOnClickListener(this);
        btn_CloseBT.setOnClickListener(this);
        btn_DiscoveryBT.setOnClickListener(this);
        lv_BTList.setOnItemClickListener(this);

        easyBT.setOpenCallBack(this);
        easyBT.setScanCallBack(this);

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
        BluetoothDevice device = deviceList.get(position);
        final IConnectionManager manager = EasyBT.open(device.getAddress(), device.getName());

        manager.registerReceiver(socketActionAdapter);

        manager.connect();

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
        deviceList.clear();
        blueToothListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBTFound(BluetoothDevice device) {
        deviceList.add(device);
        blueToothListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBTDiscoveryFinished() {
        if (deviceList.isEmpty()){
            Toast.makeText(this, "蓝牙结束，未扫到！", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "蓝牙结束！", Toast.LENGTH_SHORT).show();
        }
    }

    SocketActionAdapter socketActionAdapter = new SocketActionAdapter(){
        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            super.onSocketDisconnection(info, action, e);
            Toast.makeText(BTFuncActivity.this, "蓝牙连接失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            super.onSocketConnectionSuccess(info, action);
            Toast.makeText(BTFuncActivity.this, "蓝牙连接成功！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            super.onSocketConnectionFailed(info, action, e);
            Toast.makeText(BTFuncActivity.this, "蓝牙连接失败！", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(info, action, data);
        }

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(info, action, data);
        }

        @Override
        public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(info, data);
        }
    };

}
