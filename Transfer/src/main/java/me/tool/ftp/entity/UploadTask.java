package me.tool.ftp.entity;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.IOException;

import me.tool.ftp.ConnectListener;
import me.tool.ftp.LoginListener;
import me.tool.ftp.TransferConfig;
import me.tool.ftp.UploadListener;
import me.tool.ftp.internal.InternalWrapper;
import me.tool.ftp.internal.TransferWrapper;

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