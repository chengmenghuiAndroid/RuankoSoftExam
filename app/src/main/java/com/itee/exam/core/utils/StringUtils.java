/**
 *
 */
package com.itee.exam.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author moxin
 */
public final class StringUtils {

    private static final String HEX_STRING = "0123456789ABCDEF";

    private StringUtils() {

    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param str
     * @return boolean
     */
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 当前数据长度不足时补零
     *
     * @param data
     * @param dataLen data有效长度
     * @return
     */
    public static String fillZero(String data, int dataLen) {
        while (data.length() < dataLen) {
            data = "0" + data;
        }
        return data;
    }

    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStr2Bytes(String hex) {
        String upperHex = hex.toUpperCase();
        int len = (upperHex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = upperHex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    public static final String bytes2HexStr(byte[] bArray) {
        return bytes2HexStr(bArray, null);
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @param intervalStr 无需间隔则输入null
     * @return
     */
    public static final String bytes2HexStr(byte[] bArray, String intervalStr) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String temp;
        for (byte b : bArray) {
            temp = Integer.toHexString(0xFF & b);
            if (temp.length() < 2) {
                sb.append(0);
            }
            sb.append(temp.toUpperCase());
            if (intervalStr != null && !intervalStr.equals("")) {
                sb.append(intervalStr);
            }
        }
        return sb.toString();
    }

    /**
     * 10进制串转为BCD码
     *
     * @param asc 10进制串
     * @return BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        if (len >= 2) {
            len = len / 2;
        }

        byte[] bbt = new byte[len];
        byte[] abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    private static byte toByte(char c) {
        return (byte) HEX_STRING.indexOf(c);
    }

    public static boolean checkNormalWord(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char codePoint = str.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return false;
            }
        }
//        String reg = "[\u4e00-\u9fa5\\s\\w[,.:;?!，。：？！，。：？！\" ” “、、、]]+"; //匹配中文、空白、字母数字、标点符号
//        return str.matches(reg);
        return true;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public static boolean checkChineseOnly(String str) {
        if (str == null|| str.trim().equals("")){
            return true;
        }

        String reg = "[\u4e00-\u9fa5]+"; //匹配中文、空白、字母数字、标点符号
        return str.matches(reg);
    }

    /**
     * 是否包含特殊字符判断
     * @param str
     * @return
     */
    public static boolean isContainSpecialStr(String str) {
        String regEx="[^A-Za-z0-9\\u4E00-\\u9FA5]";
        Pattern pattern = Pattern.compile(regEx);
        Matcher isNum = pattern.matcher(str);
        return  isNum.find();
    }

}