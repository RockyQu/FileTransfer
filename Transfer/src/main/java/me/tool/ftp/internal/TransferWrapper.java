package me.tool.ftp.internal;

import java.io.IOException;

import me.tool.ftp.UploadListener;

public interface TransferWrapper {

    /**
     * 连接服务器
     *
     * @throws IOException
     */
    void connect() throws IOException;

    /**
     * 是否已经连接服务器
     */
    boolean isConnected();

    /**
     * 上传文件
     *
     * @param path           需要上传的文件路径
     * @param uploadListener 文件上传状态监听
     * @return
     */
    boolean uploadFile(String path, UploadListener uploadListener);

    /**
     * 在服务器上创建文件夹
     *
     * @param path
     * @return
     */
    boolean createFolder(String path) throws IOException;

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    void close() throws IOException;
}