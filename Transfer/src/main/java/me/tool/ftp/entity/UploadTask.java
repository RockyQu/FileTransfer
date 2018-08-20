package me.tool.ftp.entity;

import android.app.Fragment;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

import me.tool.ftp.UploadListener;
import me.tool.ftp.internal.TransferWrapper;

/**
 * 上传文件任务
 */
public class UploadTask extends AsyncTask<String, Integer, Boolean> {

    // 上传文件本地路径
    private String path;

    /**
     * {@link TransferWrapper}
     */
    private TransferWrapper wrapper;

    /**
     * 上传文件状态监听接口
     * <p>
     * {@link TransferWrapper}
     */
    private UploadListener uploadListener;

    public UploadTask(String path, TransferWrapper wrapper, UploadListener uploadListener) {
        this.path = path;
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

        result = wrapper.uploadFile(path, uploadListener);

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