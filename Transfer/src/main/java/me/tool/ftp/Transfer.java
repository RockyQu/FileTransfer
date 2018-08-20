package me.tool.ftp;

import me.tool.ftp.internal.TransferWrapper;

public class Transfer implements TransferWrapper {

    private Transfer() {

    }

    private static class Singleton {
        private static TransferWrapper transfer = new Transfer();
    }

    public static TransferWrapper get() {
        return Singleton.transfer;
    }

    @Override
    public boolean uploadFile(String filePath, String ftpPath) {
        return false;
    }

    @Override
    public boolean downloadFile(String downPath, String savePath) {
        return false;
    }
}