package me.tool.ftp.internal;

import java.io.File;
import java.io.IOException;

import me.tool.ftp.entity.AuthUser;

public interface InternalWrapper {

    /**
     * 获取连接状态
     *
     * @return
     */
    int getReplyCode();

    /**
     * 连接服务器
     *
     * @throws IOException
     */
    int connect() throws IOException;

    /**
     * 登录
     *
     * @param authUser
     * @return
     * @throws IOException
     */
    int login(AuthUser authUser) throws IOException;

    /**
     * 是否已经连接服务器
     */
    boolean isConnected();

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
     * 关闭连接
     *
     * @throws IOException
     */
    void close() throws IOException;
}