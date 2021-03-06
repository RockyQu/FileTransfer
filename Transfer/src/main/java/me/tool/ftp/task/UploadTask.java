package me.tool.ftp.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import me.tool.ftp.TransferConfig;
import me.tool.ftp.UploadListener;
import me.tool.ftp.entity.ReplyCode;
import me.tool.ftp.internal.InternalWrapper;
import me.tool.ftp.log.TransferLog;

/**
 * 上传文件任务
 */
public class UploadTask extends AsyncTask<String, Integer, Boolean> {

    private Uri uri;
    private InternalWrapper wrapper;
    private UploadListener uploadListener;

    public UploadTask(Uri uri, InternalWrapper wrapper, UploadListener uploadListener) {
        this.uri = uri;
        this.wrapper = wrapper;
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
            TransferLog.d(String.format(Locale.ENGLISH, "Info: Connect %b ReplyCode %d",
                    wrapper.isConnected(), wrapper.getReplyCode()));

            // 如果连接失效或登录超时，则重新连接登录
            if (!wrapper.isConnected() || !(wrapper.getReplyCode() == ReplyCode.COMMAND_DETERMINE | wrapper.getReplyCode() == ReplyCode.SERVICE_READY)) {

                TransferLog.d("This connection is closed or logout timeout, attempting to reconnect to login.");

                int connectReply = wrapper.connect();// 开始连接服务器
                if (connectReply == ReplyCode.SERVICE_READY) {
                    wrapper.login(TransferConfig.getInstance().getAuthUser());
                }
            }

            TransferLog.d(String.format(Locale.ENGLISH, "Info: Connect %b ReplyCode %d",
                    wrapper.isConnected(), wrapper.getReplyCode()));

            if (uri != null) {
                result = wrapper.uploadInputStream(new File(uri.getPath()));
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