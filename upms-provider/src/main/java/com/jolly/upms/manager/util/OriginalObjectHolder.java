package com.jolly.upms.manager.util;

/**
 * berkeley 删除 修改的时候保留原来的对象
 */
public class OriginalObjectHolder<T> {

    //老的对象
    private static ThreadLocal<Object> originalObject = new ThreadLocal<Object>();

    //新的对象
    private static ThreadLocal<Object> newObject = new ThreadLocal<Object>();

    public static void putOriginal(Object s) {
        originalObject.set(s);
    }

    public static Object getOriginal() {
        return originalObject.get();
    }

    public static void removeOriginal() {
        originalObject.remove();
    }

    public static void putNew(Object s) {
        newObject.set(s);
    }

    public static Object getNew() {
        return newObject.get();
    }

    public static void removeNew() {
        newObject.remove();
    }
}
