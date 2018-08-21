package me.tool.ftp.listener;

/**
 * 连接状态监听接口
 */
public interface ConnectListener {

    int CONNECT_STATE_FAILURE = 0;
    int CONNECT_STATE_SUCCESS = 220;

    /**
     * 连接状态
     * <p>
     * {@link ConnectListener#CONNECT_STATE_FAILURE}
     * {@link ConnectListener#CONNECT_STATE_SUCCESS}
     *
     * @param reply 状态码
     */
    void connectState(int reply);
}