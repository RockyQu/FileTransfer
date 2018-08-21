package me.tool.ftp.listener;

import android.net.Uri;

import me.tool.ftp.listener.ConnectListener;
import me.tool.ftp.listener.LoginListener;
import me.tool.ftp.listener.UploadListener;

public interface TransferWrapper {

    /**
     * 上传文件
     *
     * @param uri           需要上传的文件路径
     * @param uploadListener 文件上传状态监听
     * @return
     */
    void uploadFile(Uri uri, UploadListener uploadListener);

    /**
     * 上传文件
     *
     * @param uri            需要上传的文件路径
     * @param connectListener 连接状态监听接口
     * @param uploadListener  文件上传状态监听
     * @return
     */
    void uploadFile(Uri uri, ConnectListener connectListener, UploadListener uploadListener);

    /**
     * 上传文件
     *
     * @param uri            需要上传的文件路径
     * @param connectListener 连接状态监听接口
     * @param loginListener   登录状态监听接口
     * @param uploadListener  文件上传状态监听
     * @return
     */
    void uploadFile(Uri uri, ConnectListener connectListener, LoginListener loginListener, UploadListener uploadListener);
}