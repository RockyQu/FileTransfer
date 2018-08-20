package me.tool.ftp;

import android.app.Application;

import me.tool.ftp.entity.AuthUser;

/**
 * 一些配置参数，具体请查看 {@link TransferConfig}
 */
public class TransferConfig {

    private Application application;
    private static final int DEFAULT_CONFIG_PORT = 21;

    /**
     * IP 地址
     */
    private String host;

    /**
     * 端口，默认 {@link TransferConfig#DEFAULT_CONFIG_PORT}
     */
    private int port = TransferConfig.DEFAULT_CONFIG_PORT;

    /**
     * 授权登录用户，具体请看 {@link AuthUser}
     */
    private AuthUser authUser;

    private TransferConfig() {

    }

    private static class Singleton {
        private static TransferConfig config = new TransferConfig();
    }

    public static TransferConfig getInstance() {
        return Singleton.config;
    }

    public void init(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }

    public TransferConfig setApplication(Application application) {
        this.application = application;
        return this;
    }

    public String getHost() {
        return host;
    }

    public TransferConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public TransferConfig setPort(int port) {
        this.port = port;
        return this;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public TransferConfig setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
        return this;
    }
}
