package me.tool.ftp.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.tool.ftp.Transfer;
import me.tool.ftp.TransferConfig;
import me.tool.ftp.user.AuthUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransferConfig config = TransferConfig.getInstance()
                .setApplication(getApplication())
                .setHost("192.168.1.22")
                .setPort(21)
                .setAuthUser(new AuthUser("", ""));

        Transfer.getInstance().uploadFile("", "");


    }
}
