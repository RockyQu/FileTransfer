package me.tool.ftp.task;

import android.os.AsyncTask;
import java.io.IOException;

import me.tool.ftp.entity.AuthUser;
import me.tool.ftp.entity.ReplyCode;
import me.tool.ftp.internal.InternalWrapper;
import me.tool.ftp.LoginListener;

/**
 * 连接并登录
 */
public class LoginTask extends AsyncTask<Void, Void, Void> {

    private AuthUser authUser;
    private InternalWrapper wrapper;
    private LoginListener loginListener;

    public LoginTask(AuthUser authUser, InternalWrapper wrapper, LoginListener loginListener) {
        this.authUser = authUser;
        this.loginListener = loginListener;
        this.wrapper = wrapper;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            int connectReply = wrapper.connect();// 开始连接服务器
            if (loginListener != null) {
                loginListener.connectState(connectReply);
            }

            if (connectReply == ReplyCode.SERVICE_READY) {
                int loginReply = wrapper.login(authUser);// 开始登录
                if (loginListener != null) {
                    loginListener.loginState(loginReply);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}