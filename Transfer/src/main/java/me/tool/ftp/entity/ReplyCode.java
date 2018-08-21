package me.tool.ftp.entity;

/**
 * 一些常用状态码
 */
public class ReplyCode {

    // 服务就绪，可以执行新用户的请求
    public static final int SERVICE_READY = 220;
    // 用户已登录
    public static final int USER_LOGGED = 230;
    // 用户未登录
    public static final int USER_NOT_LOGGED = 530;
}