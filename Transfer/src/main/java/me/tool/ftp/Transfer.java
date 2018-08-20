package me.tool.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.tool.ftp.internal.TransferWrapper;
import me.tool.ftp.entity.AuthUser;

public class Transfer implements TransferWrapper {

    /**
     * {@link FTPClient}
     */
    private FTPClient client = new FTPClient();

    private Transfer() {
        this.initDefaults();
    }

    private static class Singleton {
        private static TransferWrapper transfer = new Transfer();
    }

    public static TransferWrapper getInstance() {
        return Singleton.transfer;
    }

    private void initDefaults() {
        client.setDefaultPort(TransferConfig.getInstance().getPort());
    }

    /**
     * FTP当前目录.
     */
    private String currentPath = "/";

    @Override
    public boolean uploadFile(String path, UploadListener uploadListener) {
        boolean flag = false;

        return flag;
    }

    @Override
    public void connect() throws IOException {
// 中文转码
        client.setControlEncoding("UTF-8");
        int reply; // 服务器响应值
        // 连接至服务器
        client.connect(TransferConfig.getInstance().getHost());
        // 获取响应值
        reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            client.disconnect();
            throw new IOException("connect fail: " + reply);
        }
        // 登录到服务器
        AuthUser user = TransferConfig.getInstance().getAuthUser();
        if (user != null && !user.getUsername().equals("")) {
            client.login(user.getUsername(), user.getPassword());
        } else {
            //无用户名登录时
            client.login("anonymous", "123456");
        }
        // 获取响应值，判断登陆结果
        reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            client.disconnect();
            throw new IOException("connect fail: " + reply);
        } else {
            // 获取登录信息
            FTPClientConfig config = new FTPClientConfig(client.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            client.configure(config);
            // 使用被动模式设为默认
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.enterLocalActiveMode();
//            ftpClient.enterRemotePassiveMode();
//            // 二进制文件支持
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
        }
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public boolean createFolder(String path) throws IOException {
        return client.makeDirectory(path);
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.logout();
            client.disconnect();
        }
    }

    /**
     * 上传.
     *
     * @param localFolderPath 需要上传的本地文件夹路径
     * @param remotePath      FTP目录
     * @return 上传结果
     * @throws IOException
     */
    public int uploadFolder(String localFolderPath, String remotePath) throws IOException {
        //
        int count = 0;
        boolean flag = false;
        // 初始化FTP当前目录
        currentPath = remotePath;
        // 二进制文件支持
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        // 设置模式
        client.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
        // 改变FTP目录
        client.changeWorkingDirectory(currentPath);
        File localFolder = new File(localFolderPath);
        if (localFolder.exists() && localFolder.isDirectory()) {
            //先在ftp上创建对应的文件夹
            String ftpFolder = remotePath + "/" + localFolder.getName();
            createFolder(ftpFolder);
            // 改变FTP目录
            client.changeWorkingDirectory(ftpFolder);
            //遍历文件夹
            File[] files = localFolder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    //如果是文件夹
                    int result = uploadFolder(file.getAbsolutePath(), ftpFolder + "/" + file.getName());
                    count += result;
                } else if (file.isFile()) {
                    flag = uploadingSingle(file);
                    if (flag) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * 上传单个文件.
     *
     * @param localFile 本地文件
     * @return true上传成功, false上传失败
     * @throws IOException
     */
    private boolean uploadingSingle(File localFile) throws IOException {
        boolean flag;
        // 创建输入流
        InputStream inputStream = new FileInputStream(localFile);
        // 上传单个文件
        flag = client.storeFile(localFile.getName(), inputStream);
        // 关闭文件流
        inputStream.close();
        return flag;
    }
}