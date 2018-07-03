package com.jolly.upms.facade.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * AES加密和解密
 *
 * @author chenjc
 * @since 2017-06-26
 */
public class EncryptAndDecryptUtil {

    /**
     * 加密
     */
    public static String encrypt(String key, String data) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bytes = cipher.doFinal(data.getBytes("utf-8"));//加密，使用UTF-8编码
        String base64Encode = Base64.encodeBase64String(bytes);//将字节流数据编码成字符串，以方便传输
        return URLEncoder.encode(base64Encode, "utf-8");//由于BASE64编码可能会产生“+”、“/”、“=”等符号，不利于HTTP传输，所以对BASE64编码再进行一次URL编码
    }

    /**
     * 解密
     */
    public static boolean decrypt(String key, String data, String encryptedData) throws Exception {
        SecretKey secretKey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String urlDecode = URLDecoder.decode(encryptedData, "utf-8");
        byte[] base64Decode = Base64.decodeBase64(urlDecode);
        byte[] bytes = cipher.doFinal(base64Decode);
        String decryptedData = new String(bytes, "utf-8");//使用UTF-8编码
        return decryptedData.equals(data);
    }

}