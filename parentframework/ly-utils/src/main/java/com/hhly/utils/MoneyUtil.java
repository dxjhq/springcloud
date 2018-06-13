package com.hhly.utils;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

// 生成 setter getter toString equals hashcode
// @Data


// setter 返回 this
@Accessors(chain = true)
// 生成无参数的构造
@NoArgsConstructor
@Deprecated
public class MoneyUtil {

    private static final String ERROR = "金额无效";

    /** 元分换算的比例. 左移右移的位数 */
    private static final int SCALE = 2;

    /** 输出大写中文 */
    public String toChinese() {
        return toChinese(toString());
    }

    // ====================== 以下是静态方法 ======================

    /** 元转换成分. 右移两位 */
    public static Long yuanToCent(String yuan) {
        if (ValueUtil.isBlank(yuan)) return null;

        double cent = 0;
        try {
            cent = Double.parseDouble(yuan.trim());
        } catch (NumberFormatException e) {
           // U.assertException(true, String.format("%s(%s)", ERROR, yuan));
        }
       // U.assertException(cent < 0, String.format("金额(%s)不能为负数", yuan));
        return new BigDecimal(cent).movePointRight(SCALE).longValue();
    }
    /** 分转换为元. 左移两位 */
    public static String centToYuan(Long cent) {
        return (cent == null || cent < 0) ? ValueUtil.EMPTY : new BigDecimal(cent).movePointLeft(SCALE).toString();
    }

    /** 元转换成中文大写 */
    public static String toChinese(String yuan) {
        return ChineseConvert.upperCase(yuan);
    }

    /** 转换中文大写 */
    private static final class ChineseConvert {
        /** 整数和小数位之间的分隔 */
        private static final String SPLIT = " ";
        private static final String WHOLE = "整";
        private static final String NEGATIVE = "负";

        private static final String[] INTEGER = {
                "圆", "拾", "佰", "仟",
                "万", "拾", "佰", "仟",
                "亿", "拾", "佰", "仟"
        };
        private static final String[] DECIMAL = {/*"厘", */"分", "角"};
        private static final String[] NUM = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

        /**
         * 转换大写
         *
         * @param money 金额
         * @return 大写
         */
        public static String upperCase(String money) {
            // 必要的检查. 如果是 0 直接返回
            if (ValueUtil.isBlank(money)) return ERROR;
            try {
                if (Double.parseDouble(money) == 0) return NUM[0] + INTEGER[0] + WHOLE;
            } catch (NumberFormatException nfe) {
                return String.format("%s(%s)", ERROR, money);
            }

            // 基本的位数检查, 按小数拆分, 分别处理
            String left = money.contains(".") ? money.substring(0, money.indexOf(".")) : money;
            long leftLong = NumberUtils.toLong(left);
            if (leftLong < 0) left = left.substring(1);
            if (left.length() > INTEGER.length) {
                return "最大只能转换到小数位前 " + INTEGER.length + " 位";
            }
            String right = money.contains(".") ? money.substring(money.indexOf(".") + 1) : "";
            if (right.length() > DECIMAL.length) {
                right = right.substring(0, DECIMAL.length);
                // return "最小只能转换到小数位后 " + DECIMAL.length + " 位(" + DECIMAL[0] + ")";
            }

            StringBuilder sbd = new StringBuilder();
            // 处理小数位前面的数
            if (leftLong != 0) {
                if (leftLong < 0) sbd.append(NEGATIVE);
                for (int i = 0; i < left.length(); i++) {
                    int number = NumberUtils.toInt(String.valueOf(left.charAt(i)));
                    sbd.append(NUM[number]).append(INTEGER[left.length() - i - 1]);
                }
            }

            // 处理小数位后面的值
            long rightLong = NumberUtils.toLong(right);
            if (rightLong > 0) {
                sbd.append(SPLIT);
                for (int i = 0; i < right.length(); i++) {
                    int number = NumberUtils.toInt(String.valueOf(right.charAt(i)));
                    sbd.append(NUM[number]).append(DECIMAL[right.length() - i - 1]);
                }
            } else if (rightLong == 0) {
                sbd.append(WHOLE);
            }
            // 最后的收尾工作, 替换成可读性更强的.
            return sbd.toString().replaceAll("零仟", "零").replaceAll("零佰", "零").replaceAll("零拾", "零")
                    .replaceAll("零零零", "零").replaceAll("零零", "零")
                    .replaceAll("零亿", "亿").replaceAll("零万", "万").replaceAll("亿万", "亿")
                    .replaceAll("壹拾", "拾").replaceAll("零圆", "圆")
                    .replaceAll("零角", "").replaceAll("零分", "");
        }
    }
}