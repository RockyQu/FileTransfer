package me.tool.ftp.listener;

/**
 * 登录状态监听接口
 */
public interface LoginListener {

    int CONNECT_STATE_FAILURE = 0;
    int CONNECT_STATE_SUCCESS = 220;

    /**
     * 连接状态
     * <p>
     * {@link LoginListener#CONNECT_STATE_FAILURE}
     * {@link LoginListener#CONNECT_STATE_SUCCESS}
     *
     * @param reply 状态码
     */
    void connectState(int reply);

    int LOGIN_STATE_SUCCESS = 220;

    /**
     * 登录状态
     * <p>
     * {@link LoginListener#LOGIN_STATE_SUCCESS}
     *
     * @param reply 状态码
     */
    void loginState(int reply);
}