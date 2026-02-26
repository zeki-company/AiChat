package com.cdsy.aichat.manager.api.base;

public class ApiError {

    public static final int ERROR_CODE_NOT_FOUND = 404;//404
    public static final int ERROR_CODE_NO_DATA = 406;//无数据
    public static final int ERROR_CODE_ACCOUNT_NOT_EXIST = 406;//账号不存在
    public static final int ERROR_CODE_OBJECT_NOT_EXIST = 406;//对象不存在
    public static final int ERROR_CODE_USER_NAME_HAS_EXISTED = 409;//用户名已存在
    public static final int ERROR_CODE_HAS_EXISTED = 409;//已存在
    public static final int ERROR_CODE_USER_BUSY = 409;//用户正忙
    public static final int ERROR_CODE_CLASH = 409;//数据冲突
    public static final int ERROR_CODE_HAS_LIKED = 409;//已经like过了
    public static final int ERROR_CODE_HAS_BLOCKED = 409;//已经block过了
    public static final int ERROR_CODE_VERCODE_ERROR = 422;//验证码错误
    public static final int ERROR_CODE_LOCKED = 423;//匹配中，匹配成功，但是一方可能返回的code，
    public static final int ERROR_CODE_INSUFFICIENT_BALANCE = 402;//余额不足
    public static final int ERROR_CODE_ANSWER_OVERTIME = 408;//接听超时
    public static final int ERROR_CODE_CONNECT_CLASH = 409;//待建立连接的双方条件冲突

    public static final int ERROR_CODE_SIGN_ERROR = 1005;//签名错误
    public static final int ERROR_CODE_TOKEN_ERROR = 1000;//token 错误退出重新登录
    public static final int ERROR_CODE_SSO_LOGIN = 1001;//sso登陆
    public static final int ERROR_CODE_TOKEN_EXPIRE = 1002;//token过期
    public static final int ERROR_CODE_CAPTCHA_INVALID = 1301;//无效
    public static final int ERROR_CODE_CAPTCHA_EXPIRED = 1302;//过期
    public static final int ERROR_CODE_COIN_NOT_ENOUGH = 1300;//金币不足

    public static final int ERROR_CODE_DEVICE_BANNED = 1003;//设备被ban
    public static final int ERROR_CODE_USER_BANNED = 1004;//用户被ban

    public static final int ERROR_CODE_AGE_BANNED = 1007;//年龄被ban

    public static final int ERROR_CODE_ACCOUNT_DELETE = 1400;//账号被删除
    public static final int ERROR_CODE_BLOCK_USER = 1401;//发起方拉黑了账号

    public static final int ERROR_CODE_BE_BLOCK = 1402;//发起方被拉黑了账号

    public static final int ERROR_CODE_HIGH_RISK_LIMIT_MATCH = 1202;//用户被标记风险，被限制匹配
}
