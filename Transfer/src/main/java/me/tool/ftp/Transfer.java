package me.tool.ftp;

public class Transfer {

    private TransferConfig config = null;

    private Transfer() {

    }

    private static class Singleton {
        private static Transfer transfer = new Transfer();
    }

    public static Transfer get() {
        return Singleton.transfer;
    }

    public void init(TransferConfig config) {
        this.config = config;
    }



}