package com.microape.easybt.common.common_interfacies.server;

import com.microape.easybt.core.iocore.interfaces.ISendable;
import com.microape.easybt.core.pojo.OriginalData;

public interface IClientIOCallback {

    void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool);

    void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool);

}
