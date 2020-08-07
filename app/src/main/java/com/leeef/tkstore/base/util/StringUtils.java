package com.leeef.tkstore.base.util;

import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.leeef.tkstore.base.BaseApplication;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by newbiechen on 17-4-22.
 * 对文字操作的工具类
 */

public class StringUtils {
    private static final String TAG = "StringUtils";
    private static final int HOUR_OF_DAY = 24;
    private static final int DAY_OF_YESTERDAY = 2;
    private static final int TIME_UNIT = 60;


    /**
     * according to date and date format string convert to timestamp //将时间转换成日期
     *
     * @param str    date str
     * @param format time format
     * @return timestamp
     */
    public static String dateConvert(long str, String format) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.format(new Date(str * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //将时间转换成日期
    public static String dateConvert(String str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            Long strLong = Long.valueOf(str);
            return sdf.format(new Date(strLong * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //将字符串转为时间戳
    public static long getStringToDate(String dateString, String pattern) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
            return sdf.parse(dateString).getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    //将日期转换成昨天、今天、明天
    public static String dateConvert2(String source) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(source);
            long curTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            //将MISC 转换成 sec
            long difSec = Math.abs((curTime - date.getTime()) / 1000);
            long difMin = difSec / 60;
            long difHour = difMin / 60;
            long difDate = difHour / 60;
            int oldHour = calendar.get(Calendar.HOUR);
            //如果没有时间
            if (oldHour == 0) {
                //比日期:昨天今天和明天
                if (difDate == 0) {

                    DateFormat convertFormat = new SimpleDateFormat("HH:mm");
                    String value = convertFormat.format(date);
                    return "今天" + value;
                } else if (difDate < DAY_OF_YESTERDAY) {
                    DateFormat convertFormat = new SimpleDateFormat("HH:mm");
                    String value = convertFormat.format(date);
                    return "昨天" + value;
                } else {
                    DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String value = convertFormat.format(date);
                    return value;
                }
            }

            if (difSec < TIME_UNIT) {
                return difSec + "秒前";
            } else if (difMin < TIME_UNIT) {
                return difMin + "分钟前";
            } else if (difHour < HOUR_OF_DAY) {
                return difHour + "小时前";
            } else if (difDate < DAY_OF_YESTERDAY) {
                DateFormat convertFormat = new SimpleDateFormat("HH:mm");
                String value = convertFormat.format(date);
                return "昨天" + value;
            } else {
                DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String value = convertFormat.format(date);
                return value;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toFirstCapital(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getString(@StringRes int id) {
        return BaseApplication.getContext().getResources().getString(id);
    }

    public static String getString(@StringRes int id, Object... formatArgs) {
        return BaseApplication.getContext().getResources().getString(id, formatArgs);
    }

    public static Drawable getColorDrawable(int id, @DrawableRes int resId) {
        Drawable drawable = getDrawable(resId).mutate();
        drawable.setColorFilter(getColor(id), PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }

    public static List<String> getStringArray(@ArrayRes int id) {
        return Arrays.asList(BaseApplication.getContext().getResources().getStringArray(id));
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return BaseApplication.getContext().getResources().getDrawable(id);
    }

    public static int getColor(int color) {
        return BaseApplication.getContext().getResources().getColor(color);
    }

    /**
     * 将文本中的半角字符，转换成全角字符
     *
     * @param input
     * @return
     */
    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i] > 32 && c[i] < 127)    //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    //功能：字符串全角转换为半角
    public static String fullToHalf(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) //全角空格
            {
                c[i] = (char) 32;
                continue;
            }

            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 判断字符串是否为null或全为空白字符或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isTrimEmpty(String s) {
        if (s == null || s.trim().length() == 0) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为null或全为空白字符或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isNotEmpty(String s) {
        if (s == null || s.trim().length() == 0) return false;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(char c) {
        return isTrimEmpty(String.valueOf(c));
    }

    /**
     * str1 中是否包含str2
     *
     * @param str1
     * @param str2
     * @return {@code true}: 有<br> {@code false}: 没有
     */
    public static boolean isHaveString(String str1, String str2) {
        return str1.contains(str2);
    }

    /**
     * 判断两字符串是否相等-精确
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * 字符串首字母转成大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 字符串首字母转成小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }


    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(final String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 半角转换为全角
     * SBC全角；DBC半角
     *
     * @param s
     * @return
     */
    public static String toSBC(String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String removeSpecialStr(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String replaceEpub(String str, String chapter, String bookName) {
        bookName = bookName.replaceAll("-", " ");
        str = str.replaceAll("&#8217;", "'").replaceAll("&#8212;", "——")
                .replaceAll("&#8220;", "\"").replaceAll("&#8221;", "\"")
                .replaceAll("&#160;", " ").replaceAll("&nbsp;", " ");// 替换中文标号
        str = str.trim();
        if (str.startsWith(bookName)) str = str.replace(bookName, "");
        if (str.startsWith(chapter)) str = str.replace(chapter, "");
        return str;
    }

    /**
     * 处理TextView中英文导致自动换行问题
     *
     * @param textView
     * @return
     */
    public static String adaptiveText(final TextView textView) {
        final String originalText = textView.getText().toString(); //原始文本
        final Paint tvPaint = textView.getPaint();//获取TextView的Paint
        final float tvWidth = textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight(); //TextView的可用宽度
        //将原始文本按行拆分
        String[] originalTextLines = originalText.replaceAll("\r", "").split("\n");
        StringBuilder newTextBuilder = new StringBuilder();
        for (String originalTextLine : originalTextLines) {
            //文本内容小于TextView宽度，即不换行，不作处理
            if (tvPaint.measureText(originalTextLine) <= tvWidth) {
                newTextBuilder.append(originalTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int i = 0; i != originalTextLine.length(); ++i) {
                    char charAt = originalTextLine.charAt(i);
                    lineWidth += tvPaint.measureText(String.valueOf(charAt));
                    if (lineWidth <= tvWidth) {
                        newTextBuilder.append(charAt);
                    } else {
                        //单行超过TextView可用宽度，换行
                        newTextBuilder.append("\n");
                        lineWidth = 0;
                        --i;//该代码作用是将本轮循环回滚，在新的一行重新循环判断该字符
                    }
                }
            }
        }
        //把结尾多余的\n去掉
        if (!originalText.endsWith("\n")) {
            newTextBuilder.deleteCharAt(newTextBuilder.length() - 1);
        }
        return newTextBuilder.toString();
    }


    /**
     * 将秒转化为分钟
     *
     * @param time
     * @return
     */
    public static String getTime(int time) {
        String timeStr;
        timeStr = new DecimalFormat("00").format(time / 60) + "分";
        timeStr += new DecimalFormat("00").format(time % 60) + "秒";
        return timeStr;
    }


    public static InputFilter getFilter(final int maxLen) {
        InputFilter filter = (src, start, end, dest, dstart, dend) -> {
            int dindex = 0;
            int count = 0;

            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return dest.subSequence(0, dindex - 2) + "...";
            }

            int sindex = 0;
            while (count <= maxLen && sindex < src.length()) {
                char c = src.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLen) {
                return src.subSequence(0, sindex - 2) + "...";
            } else {
                return src.subSequence(0, sindex);
            }


        };
        return filter;

    }

    public static String getRealUrl(String url) {
        if (StringUtils.isNotEmpty(url)) {
            if (!url.startsWith(Constant.HTTP_HEAD)) {
                return Constant.CLIENT_URL + url;
            } else return url;
        }
        return "";
    }

    /**
     * 富文本适配
     */
    public static String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:100%; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

}
