package com.xiaonuo.find.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


public class Utils {


    private static SharedPreferences sp;


    public static boolean cleanSp(Context context) {
        if (sp == null) {
            sp = context
                    .getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
        }

        return sp.edit().clear().commit();
    }


    /**
     * @param key   键
     * @param value 值
     * @return true or false
     */
    public static boolean putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context
                    .getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
        }

        return sp.edit().putString(key, value).commit();
    }


    /**
     * @param key          键
     * @param defaultValue 默认值
     * @return 取出值
     */
    public static String getString(Context context, String key, String defaultValue) {
        if (sp == null) {
            sp = context
                    .getSharedPreferences("config", Context.MODE_MULTI_PROCESS);
        }

        return sp.getString(key, defaultValue);
    }

}
