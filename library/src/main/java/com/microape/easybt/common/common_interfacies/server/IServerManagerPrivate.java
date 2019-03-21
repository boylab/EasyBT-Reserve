package com.microape.easybt.common.common_interfacies.server;

import com.microape.easybt.core.iocore.interfaces.IIOCoreOptions;

public interface IServerManagerPrivate<E extends IIOCoreOptions> extends IServerManager<E> {
    void initServerPrivate(int serverPort);
}
