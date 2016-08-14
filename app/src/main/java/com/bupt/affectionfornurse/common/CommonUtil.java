package com.bupt.affectionfornurse.common;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by John on 15/12/24.
 */
public class CommonUtil {
    /**
     * 手机号正则表达式校验
     */
    public static boolean isMobileNumber(String mobile) {
        Pattern p = Pattern.compile("^0?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0678])[0-9]{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * Show Toast-short
     */
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断文件是否存在
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public static boolean loginStatus(Context context){
        if (null != PreferencesUtil.getString(context,UserConfig.MOBILE))
            return true;
        else
            return false;
    }

}
