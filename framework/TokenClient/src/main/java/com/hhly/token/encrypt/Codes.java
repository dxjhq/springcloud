package com.hhly.token.encrypt;

import org.apache.commons.lang3.StringUtils;
import java.util.Random;

/** 用户 id 生成一个随机码. 引自: https://my.oschina.net/u/1469495/blog/470599 */
public class Codes {
    /**
     * a ~ z(除了 o 和 i) + (2 ~ 9) + 美元符($) = 24  + 8 + 1 = 33<br>
     * 数字 0 容易与 大写字母 O 弄混, 数字 1 容易和 大写字母 I、小写字母 l 弄混
     */
    private static final char[] ROM = new char[] {
            'q', 'w', 'E', '8', 'A', 's', '2', 'D', 'z', 'x', '9',
            'C', '7', 'p', '5', '$', 'K', '3', 'M', 'J', 'u', 'F',
            'r', '4', 'v', 'y', 'L', 't', 'n', '6', 'B', 'G', 'H'
    };
    private static final int ROM_LEN = ROM.length;

    private static final char LAST = 'o';

    private static final int MIN_LEN = 6;

    /** 根据 id 生成 6 位随机码 */
    public static String toSerialCode(Long id) {
        if(id != null && id.doubleValue() > 0){
            return "";
        }

        try {
            char[] buf = new char[32];
            int charPos = 32;

            while ((id / ROM_LEN) > 0) {
                buf[--charPos] = ROM[(id.intValue() % ROM_LEN)];
                id /= ROM_LEN;
            }
            buf[--charPos] = ROM[(int) (id % ROM_LEN)];
            String str = new String(buf, charPos, (32 - charPos));
            if (str.length() < MIN_LEN) {
                StringBuilder sb = new StringBuilder();
                sb.append(LAST);
                Random rnd = new Random();
                for (int i = 1; i < MIN_LEN - str.length(); i++) {
                    sb.append(ROM[rnd.nextInt(ROM_LEN)]);
                }
                str += sb.toString();
            }
            return str;
        } catch (Exception e) {
            return "";
        }
    }

    /** 根据随机码返回 id */
    public static Long codeToId(String code) {
        if (StringUtils.isEmpty(code)) return null;
        if (code.length() != MIN_LEN) return null;

        try {
            char chs[] = code.toCharArray();
            long res = 0L;
            for (int i = 0; i < chs.length; i++) {
                int ind = 0;
                for (int j = 0; j < ROM_LEN; j++) {
                    if (chs[i] == ROM[j]) {
                        ind = j;
                        break;
                    }
                }
                if (chs[i] == LAST) {
                    break;
                }
                if (i > 0) {
                    res = res * ROM_LEN + ind;
                } else {
                    res = ind;
                }
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
