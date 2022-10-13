package com.chestnut.haoweilai.common;

/**
 * 线程用于设置获取当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录用户id
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前登录用户id
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
