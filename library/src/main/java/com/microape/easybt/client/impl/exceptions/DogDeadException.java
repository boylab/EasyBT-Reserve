package com.microape.easybt.client.impl.exceptions;

import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by xuhao on 2017/6/5.
 */

public class DogDeadException extends RuntimeException {
    public DogDeadException() {
        super();
    }

    public DogDeadException(String message) {
        super(message);
    }

    public DogDeadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DogDeadException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected DogDeadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
