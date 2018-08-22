# FileTransfer
这是一个基于 Apache FTPClient 的轻量封装，帮助你轻松实现上传任何类型的文件

## Download
Gradle:
```
api 'me.tool:FileTransfer:0.1.2'
```

## Usage
* 初始化一些必要的参数
```
TransferConfig.getInstance()
                .setApplication(getApplication())
                // 设置IP 地址
                .setHost("192.168.1.22")
                // 端口
                .setPort(21)
                // 日志
                .setDebug(true)
```

* 连接并登录
```
Transfer.getInstance().login(new AuthUser("", ""), new LoginListener() {
            @Override
            public void connectState(int reply) {
                TransferLog.d(String.valueOf(reply));
            }

            @Override
            public void loginState(int reply) {
                TransferLog.d(String.valueOf(reply));
            }
        });
```

* 开始上传
```
Transfer.getInstance().uploadFile(Uri.parse(path), new UploadListener() {

                    @Override
                    public void uploaded(boolean result) {
                        Log.e(TAG, result ? "Success" : "Failure");
                    }
                });
```

## History
[UpdateLog](https://github.com/RockyQu/FileTransfer/releases)   

## Feedback
* Project  [Submit Bug or Idea](https://github.com/RockyQu/FileTransfer/issues)   

## About Me
* Email [250533855@qq.com](250533855@qq.com)  
* Home [https://rockyqu.github.io](https://rockyqu.github.io)  
* GitHub [https://github.com/RockyQu](https://github.com/RockyQu)  
