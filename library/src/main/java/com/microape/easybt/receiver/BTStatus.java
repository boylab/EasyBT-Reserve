package com.microape.easybt.receiver;

/**
 * Created by pengle on 2018-11-23.
 * email:pengle609@163.com
 */
public class BTStatus {

    private boolean isEnabled = false;

    private boolean isDiscovery = false;

    private boolean isConn = false;

    private BTStatus() {

    }

    private static class SingletonInstance {
        private static final BTStatus INSTANCE = new BTStatus();
    }

    public static BTStatus newInstance() {
        return BTStatus.SingletonInstance.INSTANCE;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        if (!enabled){
            setDiscovery(false);
            setConn(false);
        }
        isEnabled = enabled;
    }

    public boolean isDiscovery() {
        return isDiscovery;
    }

    public void setDiscovery(boolean discovery) {
        if (discovery){
            setConn(false);
        }
        isDiscovery = discovery;
    }

    public boolean isConn() {
        return isConn;
    }

    public void setConn(boolean conn) {
        if (conn){
            setDiscovery(false);
        }
        isConn = conn;
    }

}
