package me.tool.ftp.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import me.tool.ftp.UploadListener;
import me.tool.ftp.internal.InternalWrapper;

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