package me.tool.ftp.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

import me.tool.ftp.entity.AuthUser;
import me.tool.ftp.LoginListener;
import me.tool.ftp.Transfer;
import me.tool.ftp.TransferConfig;
import me.tool.ftp.UploadListener;
import me.tool.ftp.log.TransferLog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 一个测试文件
        path = this.saveBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        // 配置参数
        TransferConfig.getInstance()
                .setApplication(getApplication())
                // 设置IP 地址
                .setHost("192.168.1.22")
                // 端口
                .setPort(21)
                // 日志
                .setDebug(true)
        ;

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Transfer.getInstance().uploadFile(Uri.parse(path), new UploadListener() {

                    @Override
                    public void uploaded(boolean result) {
                        Log.e(TAG, result ? "Success" : "Failure");
                    }
                });
            }
        });

        // 连接并登录，必须在上传文件之前完成
        Transfer.getInstance().login(new AuthUser("", ""), new LoginListener() {
            @Override
            public void connectState(int reply) {
//                TransferLog.d(String.valueOf(reply));
            }

            @Override
            public void loginState(int reply) {
//                TransferLog.d(String.valueOf(reply));
            }
        });
    }

    /**
     * 保存 Bitmap
     *
     * @param bitmap
     * @return
     */
    public String saveBitmap(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getPath();
    }
}