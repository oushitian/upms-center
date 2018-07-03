package com.jolly.upms.manager.util;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dengjunbo
 * on 16/5/23.
 */
public class MD5Util {
    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };

    public static String getMD5(String source) {
        if (StringUtils.isEmpty(source)) return "";

        try {
            byte[] strTemp = source.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
    }
}
