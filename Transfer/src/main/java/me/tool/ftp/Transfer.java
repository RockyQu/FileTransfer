package me.tool.ftp;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.tool.ftp.task.LoginTask;
import me.tool.ftp.task.UploadTask;
import me.tool.ftp.internal.InternalWrapper;
import me.tool.ftp.entity.AuthUser;

/**
 * @see <a href="https://github.com/RockyQu/FileTransfer"></a>
 */
public class Transfer implements TransferWrapper, InternalWrapper {

    private static final int DEFAULT_TIMEOUT_SECOND = 10;

    private FTPClient client = new FTPClient();

    private Transfer() {
        client.setDefaultTimeout(DEFAULT_TIMEOUT_SECOND * 1000);
        client.setConnectTimeout(DEFAULT_TIMEOUT_SECOND * 1000);
        client.setDataTimeout(DEFAULT_TIMEOUT_SECOND * 1000);
        client.setControlKeepAliveReplyTimeout(DEFAULT_TIMEOUT_SECOND * 1000);
        client.setControlKeepAliveTimeout(DEFAULT_TIMEOUT_SECOND * 1000);
    }

    private static class Singleton {
        private static TransferWrapper transfer = new Transfer();
    }

    public static TransferWrapper getInstance() {
        return Singleton.transfer;
    }

    /**
     * 对外开放登录接口 {@link TransferWrapper#login(AuthUser, LoginListener)} }
     *
     * @param authUser
     * @param loginListener
     * @return
     */
    @Override
    public AsyncTask login(AuthUser authUser, LoginListener loginListener) {
        return new LoginTask(authUser, this, loginListener).execute();
    }

    /**
     * 对外开放上传文件接口 {@link TransferWrapper#uploadFile(Uri, UploadListener)}
     *
     * @param uri            需要上传的文件路径
     * @param uploadListener 文件上传状态监听
     * @return
     */
    @Override
    public AsyncTask uploadFile(Uri uri, UploadListener uploadListener) {
        return new UploadTask(uri, this, uploadListener).execute();
    }

    @Override
    public int connect() throws IOException {

        // 编码
        client.setAutodetectUTF8(true);
        client.setControlEncoding("UTF-8");

        // 连接服务器
        client.connect(TransferConfig.getInstance().getHost(), TransferConfig.getInstance().getPort());

        // 获取响应值
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            client.disconnect();
            return reply;
        }

        return reply;
    }

    @Override
    public boolean isConnected() {
        return client.isConnected();
    }

    @Override
    public int login(AuthUser authUser) throws IOException {
        AuthUser user = authUser != null ? authUser : TransferConfig.getInstance().getAuthUser();
        if (user != null) {
            client.login(!TextUtils.isEmpty(user.getUsername()) ? user.getUsername() : "anonymous", user.getPassword());
        }

        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {// 是否是非法状态码
            client.disconnect();
        }

        // 设置文本类型，必须在 Login 以后
        client.setFileType(FTP.BINARY_FILE_TYPE);
        return reply;
    }

    @Override
    public boolean createFolder(String path) throws IOException {
        return client.makeDirectory(path);
    }

    @Override
    public boolean uploadInputStream(File file) throws IOException {
        boolean uploadResult = false;

        // 设置模式
        client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
        // 改变FTP目录
        client.changeWorkingDirectory("/");

        if (file.exists() && file.isFile()) {
            InputStream inputStream = new FileInputStream(file);
            uploadResult = client.storeFile(file.getName(), inputStream);
            inputStream.close();
        }

        return uploadResult;
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.logout();
            client.disconnect();
        }
    }
}