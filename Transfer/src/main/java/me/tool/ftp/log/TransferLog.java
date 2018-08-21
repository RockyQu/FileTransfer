package me.tool.ftp.log;

import android.util.Log;

public class TransferLog {

    private static final String TAG = "FileTransfer";
    private static boolean debug;

    private TransferLog() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        TransferLog.debug = debug;
    }

    public static void d(String message) {
        if (debug) {
            Log.d(TAG, message);
        }
    }
}