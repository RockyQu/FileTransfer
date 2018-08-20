package me.tool.ftp.internal;

public interface TransferWrapper {

    /**
     * 上传文件
     *
     * @param filePath 需要上传的文件路径
     * @param ftpPath  FTP 路径
     * @return
     */
    boolean uploadFile(String filePath, String ftpPath);

    /**
     * 下载文件
     *
     * @param downPath 需要下载的文件路径
     * @param savePath 保存路径
     * @return
     */
    boolean downloadFile(String downPath, String savePath);
}