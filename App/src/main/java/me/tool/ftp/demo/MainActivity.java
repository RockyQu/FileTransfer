package me.tool.ftp.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.tool.ftp.Transfer;
import me.tool.ftp.TransferConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TransferConfig config = TransferConfig.builder()
                .setHost("192.168.1.22")
                .setPort(21)
                .build();
        Transfer.get().init(config);


    }
}
