package com.xiaonuo.find.utils;

import android.os.Handler;

import java.util.HashMap;

public class HandlerCollection {
    public static HashMap<Integer, Handler> handlers = new HashMap<>();

    public static void add(int key, Handler handler) {
        handlers.put(key, handler);
    }

    public static Handler get(int key) {
        return handlers.get(key);
    }

}
