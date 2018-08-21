package me.tool.ftp.listener;

/**
 * 上传文件状态监听接口
 */
public interface UploadListener {

    /**
     * 上传完成
     *
     * @param result 是否上传成功
     */
    void uploaded(boolean result);
}