package com.hhly.utils.encrypt;

import com.hhly.utils.LogUtil;

import java.security.MessageDigest;

/**
 * MD5工具类
 * @author pengchao
 * @create 2017-09-05
 * @desc
 */
public class MD5Util {
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String encode(String SourceString) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(SourceString.getBytes());
            byte messageDigest[] = digest.digest();
            result = toHexString(messageDigest);
        } catch (Exception e) {
            LogUtil.ROOT_LOG.error("MD5转码失败！", e);
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
