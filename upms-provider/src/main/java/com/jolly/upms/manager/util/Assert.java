package com.jolly.upms.manager.util;

import com.jolly.upms.manager.result.RRException;
import org.apache.commons.lang.StringUtils;


public abstract class Assert {

    public static void notBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new RRException(message);
        }
    }

}
