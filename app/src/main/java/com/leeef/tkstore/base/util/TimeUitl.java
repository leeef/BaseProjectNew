package com.leeef.tkstore.base.util;

import java.text.DecimalFormat;

/**
 * @ClassName: TimeUitl
 * @Description:
 * @Author: leeeeef
 * @CreateDate: 2019/9/25 12:08
 */
public class TimeUitl {

    public static String getSecond(int time) {
        return new DecimalFormat("00").format((time % 3600) % 60);
    }

    public static String getMinute(int time) {
        return new DecimalFormat("00").format((time % 3600) / 60);
    }

    public static String getHour(int time) {
        return time / 3600 + "";
    }
}
