package me.tool.ftp;

/**
 * 登录状态监听接口
 */
public interface LoginListener {

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