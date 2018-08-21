package me.tool.ftp.entity;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;

import me.tool.ftp.listener.ConnectListener;
import me.tool.ftp.listener.LoginListener;
import me.tool.ftp.listener.UploadListener;
import me.tool.ftp.internal.InternalWrapper;

/**
 * 上传文件任务
 */
public class UploadTask extends AsyncTask<String, Integer, Boolean> {

    private Uri uri;

    private InternalWrapper wrapper;

    private ConnectListener connectListener;
    private LoginListener loginListener;
    private UploadListener uploadListener;

    public UploadTask(Uri uri, InternalWrapper wrapper, ConnectListener connectListener, LoginListener loginListener, UploadListener uploadListener) {
        this.uri = uri;
        this.wrapper = wrapper;
        this.connectListener = connectListener;
        this.loginListener = loginListener;
        this.uploadListener = uploadListener;
    }

    /**
     * 运行在 UI 线程，在 {@link AsyncTask#doInBackground(Object[])} 之前执行
     */
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... strings) {
        boolean result = false;

        try {
            // 开始连接服务器
            int connectReply = wrapper.connect();
            if (connectListener != null) {
                connectListener.connectState(connectReply);
            }

            if (connectReply == ConnectListener.CONNECT_STATE_SUCCESS) {
                int loginReply = wrapper.login();
                if (loginListener != null) {
                    loginListener.loginState(loginReply);
                }

                if (loginReply == LoginListener.LOGIN_STATE_SUCCESS) {

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 运行在 UI 线程，在 {@link AsyncTask#doInBackground(Object[])} 之后执行
     */
    @Override
    protected void onPostExecute(Boolean result) {
        uploadListener.uploaded(result);
    }
}