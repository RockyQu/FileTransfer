package me.tool.ftp;

import android.util.Log;

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
import me.tool.ftp.user.AuthUser;

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
     * FTP根目录.
     */
    public static final String REMOTE_PATH = "/";

    /**
     * FTP当前目录.
     */
    private String currentPath = "";

    @Override
    public boolean uploadFile(String filePath, String ftpPath) {
        boolean flag = false;
        // 初始化FTP当前目录
        currentPath = ftpPath;

        try {
            // 二进制文件支持
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置模式
            client.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
            // 改变FTP目录
            client.changeWorkingDirectory(currentPath);
            File localFile = new File(filePath);
            if (localFile.exists() && localFile.isFile()) {
                flag = uploadingSingle(localFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("uploadFile", flag + "");
        return flag;
    }

    @Override
    public boolean downloadFile(String downPath, String savePath) {
        return false;
    }

    /**
     * 打开FTP服务.
     *
     * @throws IOException
     */
    public void openConnect() throws IOException {
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

    /**
     * 关闭FTP服务.
     *
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        if (client != null) {
            // 登出FTP
            client.logout();
            // 断开连接
            client.disconnect();
        }
    }

    /**
     * 获取ftp连接状态
     *
     * @throws IOException
     */
    public boolean isConnect() {
        return client.isConnected();
    }

    /**
     * 列出FTP指定文件夹下下所有文件.
     *
     * @param remotePath ftp文件夹路径
     * @return FTPFile集合
     * @throws IOException
     */
    public List<FTPFile> listFiles(String remotePath) throws IOException {
        List<FTPFile> list = new ArrayList<>();
        // 获取文件
        client.changeWorkingDirectory(remotePath);
        FTPFile[] files = client.listFiles();
        // 遍历并且添加到集合
        Collections.addAll(list, files);
        return list;
    }

    /**
     * 下载整个目录
     *
     * @param remotePath FTP目录
     * @param fileName   需要下载的文件名
     * @param localPath  本地目录
     * @return Result
     * @throws IOException
     */
    public boolean downloadFile(String remotePath, String fileName, String localPath) throws IOException {
        boolean result = false;
        // 初始化FTP当前目录
        currentPath = remotePath;
        // 更改FTP目录
        client.changeWorkingDirectory(remotePath);
        // 得到FTP当前目录下所有文件
        FTPFile[] ftpFiles = client.listFiles();
        //在本地创建对应文件夹目录
        File localFolder = new File(localPath);
        if (!localFolder.exists()) {
            localFolder.mkdirs();
        }
        // 循环遍历,找到匹配的文件
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.getName().equals(fileName)) {
                // 下载单个文件
                result = downloadSingle(new File(localPath + "/" + ftpFile.getName()), ftpFile);
            }
        }
        return result;
    }

    /**
     * 下载整个目录
     *
     * @param remotePath FTP目录
     * @param localPath  本地目录
     * @return Result 成功下载的文件数量
     * @throws IOException
     */
    public int downloadFolder(String remotePath, String localPath) throws IOException {
        //下载的数量
        int fileCount = 0;
        // 初始化FTP当前目录
        currentPath = remotePath;
        // 更改FTP目录
        client.changeWorkingDirectory(remotePath);
        // 得到FTP当前目录下所有文件
        FTPFile[] ftpFiles = client.listFiles();
        //在本地创建对应文件夹目录
        localPath = localPath + "/" + remotePath.substring(remotePath.lastIndexOf("/"));
        File localFolder = new File(localPath);
        if (!localFolder.exists()) {
            localFolder.mkdirs();
        }
        // 循环遍历
        for (FTPFile ftpFile : ftpFiles) {
            if (!ftpFile.getName().equals("..")
                    && !ftpFile.getName().equals(".")) {
                if (ftpFile.isDirectory()) {
                    //下载文件夹
                    int count = downloadFolder(currentPath + "/" + ftpFile.getName(), localPath);
                    fileCount += count;
                } else if (ftpFile.isFile()) {
                    // 下载单个文件
                    boolean flag = downloadSingle(new File(localPath + "/" + ftpFile.getName()), ftpFile);
                    if (flag) {
                        fileCount++;
                    }
                }
            }
        }
        return fileCount;
    }

    /**
     * 下载单个文件
     *
     * @param localFile 本地目录
     * @param ftpFile   FTP文件
     * @return true下载成功, false下载失败
     * @throws IOException
     */
    private boolean downloadSingle(File localFile, FTPFile ftpFile) throws IOException {
        boolean flag;
        // 创建输出流
        OutputStream outputStream = new FileOutputStream(localFile);
        // 下载单个文件
        flag = client.retrieveFile(ftpFile.getName(), outputStream);
        // 关闭文件流
        outputStream.close();
        return flag;
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

    /**
     * 创建文件夹
     *
     * @param path 文件夹名
     * @return
     */

    public boolean createFolder(String path) {
        boolean result = false;
        try {
            result = client.makeDirectory(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}