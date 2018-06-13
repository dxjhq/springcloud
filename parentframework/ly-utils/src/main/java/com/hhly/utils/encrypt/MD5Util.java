package com.hhly.utils.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * @author pengchao
 * @create 2017-09-05
 * @desc update by wangxianchen 2018-03-07
 */
public class MD5Util {
    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    protected static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            logger.error("初始化失败，MessageDigest不支持MD5Util。",ex);
        }
    }

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }




    public static String encode(String SourceString) {
        return encode(SourceString.getBytes());
    }

    public static String encode(byte[] sourceBytes) {
        String result = null;
        try {
            messagedigest.update(sourceBytes);
            byte messageDigest[] = messagedigest.digest();
            result = toHexString(messageDigest);
        } catch (Exception e) {
            logger.error("MD5转码失败！", e);
        }

        return result;
    }

    public static String encode16(String SourceString) {
        String result = encode(SourceString);
        if (result != null) {
            result = result.substring(8, 24);
        } else {
            result = SourceString;
        }

        return result;
    }
}
