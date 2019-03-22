package com.microape.easybt;

import android.annotation.SuppressLint;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

import com.microape.easybt.client.impl.client.ManagerHolder;
import com.microape.easybt.client.sdk.client.ConnectionInfo;
import com.microape.easybt.client.sdk.client.OkSocketOptions;
import com.microape.easybt.client.sdk.client.connection.IConnectionManager;
import com.microape.easybt.common.callback.OnBTConnCallBack;
import com.microape.easybt.common.callback.OnBTOpenCallBack;
import com.microape.easybt.common.callback.OnBTScanCallBack;
import com.microape.easybt.common.common_interfacies.dispatcher.IRegister;
import com.microape.easybt.common.common_interfacies.server.IServerActionListener;
import com.microape.easybt.common.common_interfacies.server.IServerManager;
import com.microape.easybt.receiver.BTReceiverManager;
import com.microape.easybt.receiver.BTStatus;

/**
 * Author：pengle on 2019/3/22 16:29
 * Email ：pengle609@163.com
 */

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

    private static ManagerHolder holder = ManagerHolder.getInstance();

    /**
     * 获得一个SocketServer服务器.
     *
     * @param serverPort
     * @return
     */
    public static IRegister<IServerActionListener, IServerManager> server(int serverPort) {
        return (IRegister<IServerActionListener, IServerManager>) holder.getServer(serverPort);
    }

    /**
     * 开启一个socket通讯通道,参配为默认参配
     *
     * @param connectInfo 连接信息{@link ConnectionInfo}
     * @return 该参数的连接管理器 {@link IConnectionManager} 连接参数仅作为配置该通道的参配,不影响全局参配
     */
    public static IConnectionManager open(ConnectionInfo connectInfo) {
        return holder.getConnection(connectInfo);
    }

    /**
     * 开启一个socket通讯通道,参配为默认参配
     *
     * @param ssid   需要连接的主机IPV4地址
     * @param port 需要连接的主机开放的Socket端口号
     * @return 该参数的连接管理器 {@link IConnectionManager} 连接参数仅作为配置该通道的参配,不影响全局参配
     */
    public static IConnectionManager open(String ssid, String port) {
        ConnectionInfo info = new ConnectionInfo(ssid, port);
        return holder.getConnection(info);
    }

    /**
     * 开启一个socket通讯通道
     * Deprecated please use {@link EasyBT#open(ConnectionInfo)}@{@link IConnectionManager#option(OkSocketOptions)}
     *
     * @param connectInfo 连接信息{@link ConnectionInfo}
     * @param okOptions   连接参配{@link OkSocketOptions}
     * @return 该参数的连接管理器 {@link IConnectionManager} 连接参数仅作为配置该通道的参配,不影响全局参配
     * @deprecated
     */
    public static IConnectionManager open(ConnectionInfo connectInfo, OkSocketOptions okOptions) {
        return holder.getConnection(connectInfo, okOptions);
    }

    /**
     * 开启一个socket通讯通道
     * Deprecated please use {@link EasyBT#open(String, String)}@{@link IConnectionManager#option(OkSocketOptions)}
     *
     * @param ssid        需要连接的主机IPV4地址
     * @param bssid      需要连接的主机开放的Socket端口号
     * @param okOptions 连接参配{@link OkSocketOptions}
     * @return 该参数的连接管理器 {@link IConnectionManager}
     * @deprecated
     */
    public static IConnectionManager open(String ssid, String bssid, OkSocketOptions okOptions) {
        ConnectionInfo info = new ConnectionInfo(ssid, bssid);
        return holder.getConnection(info, okOptions);
    }
    
}
