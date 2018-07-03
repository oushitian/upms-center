package com.jolly.upms.manager.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xml.security.utils.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mengyi
 * on 2016/3/17.
 */
public class SignUtil {
    private static Logger logger = LogManager.getLogger(SignUtil.class);

    /**
     * 用指定方式加密
     *
     * @param method      加密方式
     * @param originalStr 要加密的字符串
     * @return 加密后字符串
     */
    public static String sign(String method, String originalStr) {
        if (StringUtils.isEmpty(method)) {
            return null;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(correctMethod(method));
            md.reset();
            md.update(originalStr.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            logger.error("不存在指定加密方法：" + method,e);
            return null;
        } catch (UnsupportedEncodingException e1) {
            //该异常不处理
            e1.printStackTrace();
            return null;
        }

        return Hex.encodeHexString(md.digest());
    }

    /**
     * 修正签名方法
     * @param method
     * @return
     */
    public static String correctMethod(String method) {
        if (method.equals("SHA256")) {
            method = "SHA-256";
        }
        return method.toUpperCase();
    }

    public static String base64Encode(String originalStr) {
        try {
            return Base64.encode(originalStr.getBytes(Constant.CHARSET));
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
        return null;
    }
}
