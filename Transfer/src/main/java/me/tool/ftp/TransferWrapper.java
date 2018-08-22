package me.tool.ftp;

import android.net.Uri;
import android.os.AsyncTask;

import me.tool.ftp.entity.AuthUser;

public interface TransferWrapper {

    /**
     * 连接并登录
     *
     * @return
     */
    AsyncTask login(LoginListener loginListener);

    /**
     * 连接并登录
     *
     * @return
     */
    AsyncTask login(AuthUser authUser, LoginListener loginListener);

    /**
     * 上传文件
     *
     * @param uri            需要上传的文件路径
     * @param uploadListener 文件上传状态监听
     * @return
     */
    AsyncTask uploadFile(Uri uri, UploadListener uploadListener);
}