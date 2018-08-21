package me.tool.ftp.demo;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.tool.ftp.entity.AuthUser;
import me.tool.ftp.listener.LoginListener;
import me.tool.ftp.Transfer;
import me.tool.ftp.TransferConfig;
import me.tool.ftp.listener.TransferWrapper;
import me.tool.ftp.listener.UploadListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                getResources().getResourcePackageName(R.mipmap.ic_launcher) + "/" +
                getResources().getResourceTypeName(R.mipmap.ic_launcher) + "/" +
                getResources().getResourceEntryName(R.mipmap.ic_launcher));
        Log.e(TAG, String.valueOf(uri.getPath()));

        TransferConfig.getInstance()
                .setApplication(getApplication())
                // 设置IP 地址
                .setHost("192.168.1.22")
                // 端口
                .setPort(21)
        ;


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Transfer.getInstance().uploadFile(uri, new UploadListener() {

                    @Override
                    public void uploaded(boolean result) {
                        Log.e("uploaded", result ? "Success" : "Failure");
                    }
                });
            }
        });

        // 连接并登录，必须在上传文件之前完成
        Transfer.getInstance().login(new AuthUser("", ""), new LoginListener() {
            @Override
            public void connectState(int reply) {
                Log.e("connectState", String.valueOf(reply));
            }

            @Override
            public void loginState(int reply) {
                Log.e("loginState", String.valueOf(reply));
            }
        });
    }
}