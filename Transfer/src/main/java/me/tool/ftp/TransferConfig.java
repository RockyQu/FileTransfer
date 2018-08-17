package me.tool.ftp;

/**
 * 配置一些必要的参数
 */
public class TransferConfig {

    private String host;
    private int port;

    private TransferConfig(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        // IP 地址
        private String host;
        // 端口，默认 21
        private int port = 21;

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public TransferConfig build() {
            return new TransferConfig(this);
        }
    }
}
