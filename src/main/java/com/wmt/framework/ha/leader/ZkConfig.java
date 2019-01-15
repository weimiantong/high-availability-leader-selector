package com.wmt.framework.ha.leader;

import lombok.Getter;

@Getter
public class ZkConfig {
    private final String connectString;
    private final int sessionTimeout;
    private final String znodeName;
    private final int connectionTimeout;

    public ZkConfig(String connectString, int sessionTimeout, int connectionTimeoutMs, String znodeName) {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
        this.znodeName = znodeName;
        this.connectionTimeout = connectionTimeoutMs;
    }

    public ZkConfig(String connectString, int sessionTimeout, String znodeName) {
        this.connectString = connectString;
        this.sessionTimeout = sessionTimeout;
        this.znodeName = znodeName;
        this.connectionTimeout = 3000;
    }
}
