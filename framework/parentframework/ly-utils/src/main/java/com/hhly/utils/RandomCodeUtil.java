package com.hhly.utils;

import java.util.Random;
import java.util.UUID;

/**
 * Created by wangxianchen on 2017/8/8.
 * 生成随机码
 */
public class RandomCodeUtil {

    public static Integer RANDOM_CODE_LENGTH = 6;

    private static String[] chars = new String[] {"2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };


    private static String[] numChars = new String[] {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9"};

    public static String generateRandomCode(Integer length) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < length; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % chars.length]);
        }
        return shortBuffer.toString();
    }

    public static String generateRandomNum(Integer length) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < length; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(numChars[x % numChars.length]);
        }
        return shortBuffer.toString();
    }

    /**
     * 生成规定位数的随机码（由0-9和a-z组成）
     * @param length
     * @return
     */
    public static String generateCode(int length) {
        String val = "";
        Random random = new Random();

        // 参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                // 输出小写字母
                int temp = 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

}
