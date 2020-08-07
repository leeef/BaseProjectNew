package com.leeef.tkstore.base.util;

/**
 * @ClassName: LoginUtil
 * @Description:
 * @Author: leeeeef
 * @CreateDate: 2019/7/9 9:04
 */
public class LoginUtil {

    //返回当前登录状态
    public static boolean isLogin() {
        return SharedPreUtils.getInstance().getBoolean("login", false);
    }


    public static void setLogin(boolean login) {
        SharedPreUtils.getInstance().putBoolean("login", login);
    }

    public static void setUserID(int uid) {
        SharedPreUtils.getInstance().putInt("uid", uid);
    }

    public static void setNickName(String nickName) {
        SharedPreUtils.getInstance().putString("nickName", nickName);
    }

    public static void setHeadImage(String headImage) {
        SharedPreUtils.getInstance().putString("headImage", headImage);
    }

    public static String getHeadImage() {
        String image = SharedPreUtils.getInstance().getString("headImage");
        if (StringUtils.isNotEmpty(image)) {
            if (!image.startsWith("http"))
                return Constant.CLIENT_URL + image;
        }
        return image;
    }

    public static boolean isFirstLogin() {
        return SharedPreUtils.getInstance().getBoolean("first_login", false);
    }

    //当前账号是否是刚注册的
    public static void setFirstLogin(boolean first_login) {
        SharedPreUtils.getInstance().putBoolean("first_login", first_login);
    }

    public static String getNickName() {
        return SharedPreUtils.getInstance().getString("nickName");
    }

    public static int getUserID() {
        return SharedPreUtils.getInstance().getInt("uid", 1);
    }

    public static String getUserIDString() {
        return SharedPreUtils.getInstance().getInt("uid", -1) + "";
    }


    public static void logout() {

        setLogin(false);
        setUserID(-1);
        setHeadImage("");
        setNickName("");
        setFirstLogin(false);
    }

}
