package me.tool.ftp.internal;

import org.apache.commons.net.ftp.FTPClientConfig;

import java.io.File;
import java.io.IOException;

public interface InternalWrapper {

    /**
     * 连接服务器
     *
     * @throws IOException
     */
    int connect() throws IOException;

    /**
     * 是否已经连接服务器
     */
    boolean isConnected();

    /**
     * 登录
     *
     * @return
     * @throws IOException
     */
    int login() throws IOException;

    /**
     * 在服务器上创建文件夹
     *
     * @param path
     * @return
     */
    boolean createFolder(String path) throws IOException;

    /**
     * 开始上传文件
     *
     * @return
     */
    boolean uploadInputStream(File file) throws IOException;

    /**
     * 配置客户端配置参数，一些情况下不需要此配置
     *
     * @return
     */
    FTPClientConfig getClientConfig(String code);

    /**
     * 关闭连接
     *
     * @throws IOException
     */
    void close() throws IOException;
}