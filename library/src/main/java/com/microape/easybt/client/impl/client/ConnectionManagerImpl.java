package com.microape.easybt.client.impl.client;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.microape.easybt.client.impl.client.action.ActionHandler;
import com.microape.easybt.client.impl.client.iothreads.IOThreadManager;
import com.microape.easybt.client.impl.exceptions.ManuallyDisconnectException;
import com.microape.easybt.client.impl.exceptions.UnConnectException;
import com.microape.easybt.client.sdk.client.ConnectionInfo;
import com.microape.easybt.client.sdk.client.OkSocketOptions;
import com.microape.easybt.client.sdk.client.OkSocketSSLConfig;
import com.microape.easybt.client.sdk.client.action.IAction;
import com.microape.easybt.client.sdk.client.connection.AbsReconnectionManager;
import com.microape.easybt.client.sdk.client.connection.IConnectionManager;
import com.microape.easybt.common.common_interfacies.IIOManager;
import com.microape.easybt.common.default_protocol.DefaultX509ProtocolTrustManager;
import com.microape.easybt.common.utils.TextUtils;
import com.microape.easybt.core.iocore.interfaces.ISendable;
import com.microape.easybt.core.utils.SLog;

import java.io.IOException;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Created by xuhao on 2017/5/16.
 */
public class ConnectionManagerImpl extends AbsConnectionManager {
    /**
     * 套接字
     */
    private volatile BluetoothSocket mSocket;
    /**
     * socket参配项
     */
    private volatile OkSocketOptions mOptions;
    /**
     * IO通讯管理器
     */
    private IIOManager mManager;
    /**
     * 连接线程
     */
    private Thread mConnectThread;
    /**
     * Socket行为监听器
     */
    private ActionHandler mActionHandler;
    /**
     * 脉搏管理器
     */
    private volatile PulseManager mPulseManager;
    /**
     * 重新连接管理器
     */
    private volatile AbsReconnectionManager mReconnectionManager;
    /**
     * 能否连接
     */
    private volatile boolean isConnectionPermitted = true;
    /**
     * 是否正在断开
     */
    private volatile boolean isDisconnecting = false;


    protected ConnectionManagerImpl(ConnectionInfo info) {
        this(info, null);
    }

    public ConnectionManagerImpl(ConnectionInfo remoteInfo, ConnectionInfo localInfo) {
        super(remoteInfo, localInfo);
        String ip = "";
        String port = "";
        if (remoteInfo != null) {
            ip = remoteInfo.getAddress();
            port = remoteInfo.getName() + "";
        }
        SLog.i("block connection init with:" + ip + ":" + port);

        if (localInfo != null) {
            SLog.i("binding local addr:" + localInfo.getAddress() + " port:" + localInfo.getName());
        }
    }

    @Override
    public synchronized void connect() {
        SLog.i("Thread name:" + Thread.currentThread().getName() + " id:" + Thread.currentThread().getId());
        if (!isConnectionPermitted) {
            return;
        }
        isConnectionPermitted = false;
        if (isConnect()) {
            return;
        }
        isDisconnecting = false;
        if (mRemoteConnectionInfo == null) {
            isConnectionPermitted = true;
            throw new UnConnectException("连接参数为空,检查连接参数");
        }
        if (mActionHandler != null) {
            mActionHandler.detach(this);
            SLog.i("mActionHandler is detached.");
        }
        mActionHandler = new ActionHandler();
        mActionHandler.attach(this, this);
        SLog.i("mActionHandler is attached.");

        if (mReconnectionManager != null) {
            mReconnectionManager.detach();
            SLog.i("ReconnectionManager is detached.");
        }
        mReconnectionManager = mOptions.getReconnectionManager();
        if (mReconnectionManager != null) {
            mReconnectionManager.attach(this);
            SLog.i("ReconnectionManager is attached.");
        }

        String info = mRemoteConnectionInfo.getAddress() + ":" + mRemoteConnectionInfo.getName();
        mConnectThread = new ConnectionThread(" Connect thread for " + info);
        mConnectThread.setDaemon(true);
        mConnectThread.start();
    }

    private synchronized Socket getSocketByConfig() throws Exception {
        //自定义socket操作
        if (mOptions.getOkSocketFactory() != null) {
            return mOptions.getOkSocketFactory().createSocket(mRemoteConnectionInfo, mOptions);
        }

        //默认操作
        OkSocketSSLConfig config = mOptions.getSSLConfig();
        if (config == null) {
            return new Socket();
        }

        SSLSocketFactory factory = config.getCustomSSLFactory();
        if (factory == null) {
            String protocol = "SSL";
            if (!TextUtils.isEmpty(config.getProtocol())) {
                protocol = config.getProtocol();
            }

            TrustManager[] trustManagers = config.getTrustManagers();
            if (trustManagers == null || trustManagers.length == 0) {
                //缺省信任所有证书
                trustManagers = new TrustManager[]{new DefaultX509ProtocolTrustManager()};
            }

            try {
                SSLContext sslContext = SSLContext.getInstance(protocol);
                sslContext.init(config.getKeyManagers(), trustManagers, new SecureRandom());
                return sslContext.getSocketFactory().createSocket();
            } catch (Exception e) {
                if (mOptions.isDebug()) {
                    e.printStackTrace();
                }
                SLog.e(e.getMessage());
                return new Socket();
            }

        } else {
            try {
                return factory.createSocket();
            } catch (IOException e) {
                if (mOptions.isDebug()) {
                    e.printStackTrace();
                }
                SLog.e(e.getMessage());
                return new Socket();
            }
        }
    }

    private class ConnectionThread extends Thread {

        private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        public ConnectionThread(String info) {
            super(info);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void run() {
            try {
                if (mSocket != null) {
                    mSocket.close();
                    Thread.sleep(10);
                }

                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice btDevice = null;

                if (btDevice == null){
                    btDevice = btAdapter.getRemoteDevice(mLocalConnectionInfo.getAddress());
                }
                if (mSocket == null){
                    mSocket = btDevice.createRfcommSocketToServiceRecord(uuid);
                }
                mSocket.connect();
                Thread.sleep(10);

                resolveManager();
                sendBroadcast(IAction.ACTION_CONNECTION_SUCCESS);
                SLog.i("Socket server: " + mRemoteConnectionInfo.getAddress() + ":" + mRemoteConnectionInfo.getName() + " connect successful!");
            } catch (Exception e) {
                if (mOptions.isDebug()) {
                    e.printStackTrace();
                }
                Exception exception = new UnConnectException(e);
                SLog.e("Socket server " + mRemoteConnectionInfo.getAddress() + ":" + mRemoteConnectionInfo.getName() + " connect failed! error msg:" + e.getMessage());
                sendBroadcast(IAction.ACTION_CONNECTION_FAILED, exception);
            } finally {
                isConnectionPermitted = true;
            }
        }
    }

    private void resolveManager() throws IOException {
        mPulseManager = new PulseManager(this, mOptions);

        mManager = new IOThreadManager(
                mSocket.getInputStream(),
                mSocket.getOutputStream(),
                mOptions,
                mActionDispatcher);
        mManager.startEngine();
    }

    @Override
    public void disconnect(Exception exception) {
        synchronized (this) {
            if (isDisconnecting) {
                return;
            }
            isDisconnecting = true;

            if (mPulseManager != null) {
                mPulseManager.dead();
                mPulseManager = null;
            }
        }

        if (exception instanceof ManuallyDisconnectException) {
            if (mReconnectionManager != null) {
                mReconnectionManager.detach();
                SLog.i("ReconnectionManager is detached.");
            }
        }

        synchronized (this) {
            String info = mRemoteConnectionInfo.getAddress() + ":" + mRemoteConnectionInfo.getName();
            DisconnectThread thread = new DisconnectThread(exception, "Disconnect Thread for " + info);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private class DisconnectThread extends Thread {
        private Exception mException;

        public DisconnectThread(Exception exception, String name) {
            super(name);
            mException = exception;
        }

        @Override
        public void run() {
            try {
                if (mManager != null) {
                    mManager.close(mException);
                }

                if (mConnectThread != null && mConnectThread.isAlive()) {
                    mConnectThread.interrupt();
                    try {
                        SLog.i("disconnect thread need waiting for connection thread done.");
                        mConnectThread.join();
                    } catch (InterruptedException e) {
                    }
                    SLog.i("connection thread is done. disconnection thread going on");
                    mConnectThread = null;
                }

                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                    }
                }

                if (mActionHandler != null) {
                    mActionHandler.detach(ConnectionManagerImpl.this);
                    SLog.i("mActionHandler is detached.");
                    mActionHandler = null;
                }

            } finally {
                isDisconnecting = false;
                isConnectionPermitted = true;
                if (!(mException instanceof UnConnectException) && mSocket != null) {
                    mException = mException instanceof ManuallyDisconnectException ? null : mException;
                    sendBroadcast(IAction.ACTION_DISCONNECTION, mException);
                }
                mSocket = null;

                if (mException != null) {
                    SLog.e("socket is disconnecting because: " + mException.getMessage());
                    if (mOptions.isDebug()) {
                        mException.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public void disconnect() {
        disconnect(new ManuallyDisconnectException());
    }

    @Override
    public IConnectionManager send(ISendable sendable) {
        if (mManager != null && sendable != null && isConnect()) {
            mManager.send(sendable);
        }
        return this;
    }

    @Override
    public IConnectionManager option(OkSocketOptions okOptions) {
        if (okOptions == null) {
            return this;
        }
        mOptions = okOptions;
        if (mManager != null) {
            mManager.setOkOptions(mOptions);
        }

        if (mPulseManager != null) {
            mPulseManager.setOkOptions(mOptions);
        }
        if (mReconnectionManager != null && !mReconnectionManager.equals(mOptions.getReconnectionManager())) {
            if (mReconnectionManager != null) {
                mReconnectionManager.detach();
            }
            SLog.i("reconnection manager is replaced");
            mReconnectionManager = mOptions.getReconnectionManager();
            mReconnectionManager.attach(this);
        }
        return this;
    }

    @Override
    public OkSocketOptions getOption() {
        return mOptions;
    }

    @Override
    public boolean isConnect() {
        if (mSocket == null) {
            return false;
        }

        return mSocket.isConnected();
    }

    @Override
    public boolean isDisconnecting() {
        return isDisconnecting;
    }

    @Override
    public PulseManager getPulseManager() {
        return mPulseManager;
    }

    @Override
    public void setIsConnectionHolder(boolean isHold) {
        mOptions = new OkSocketOptions.Builder(mOptions).setConnectionHolden(isHold).build();
    }

    @Override
    public AbsReconnectionManager getReconnectionManager() {
        return mOptions.getReconnectionManager();
    }

    @SuppressLint("MissingPermission")
    @Override
    public ConnectionInfo getLocalConnectionInfo() {
        ConnectionInfo local = super.getLocalConnectionInfo();
        if (local == null) {
            if (isConnect()) {
                final BluetoothDevice remoteDevice = mSocket.getRemoteDevice();
                if (remoteDevice != null){
                    local = new ConnectionInfo(remoteDevice.getAddress(), remoteDevice.getName());
                }
            }
        }
        return local;
    }

    @Override
    public void setLocalConnectionInfo(ConnectionInfo localConnectionInfo) {
        if (isConnect()) {
            throw new IllegalStateException("Socket is connected, can't set local info after connect.");
        }
        mLocalConnectionInfo = localConnectionInfo;
    }
}
