package me.tool.ftp;

import me.tool.ftp.entity.ReplyCode;

/**
 * 登录状态监听接口
 */
public interface LoginListener {

    /**
     * 连接状态
     * <p>
     * {@link ReplyCode#CONNECT_STATE_FAILURE}
     * {@link ReplyCode#CONNECT_STATE_SUCCESS}
     *
     * @param reply 状态码
     */
    void connectState(int reply);


    /**
     * 登录状态
     * <p>
     * {@link ReplyCode#LOGIN_STATE_SUCCESS}
     *
     * @param reply 状态码
     */
    void loginState(int reply);
}