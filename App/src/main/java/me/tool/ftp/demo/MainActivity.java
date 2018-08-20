package me.tool.ftp.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.tool.ftp.Transfer;
import me.tool.ftp.TransferConfig;
import me.tool.ftp.UploadListener;
import me.tool.ftp.entity.AuthUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransferConfig.getInstance()
                .setApplication(getApplication())
                // 设置IP 地址
                .setHost("192.168.1.22")
                // 端口
                .setPort(21)
                // 登录用户
                .setAuthUser(new AuthUser("", ""));

        Transfer.getInstance().uploadFile("", new UploadListener() {

            @Override
            public void uploaded(boolean result) {

            }
        });


    }
}
