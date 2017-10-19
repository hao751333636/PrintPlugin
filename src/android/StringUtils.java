package com.print.printer.hardware.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private final static String MOBLIE_PHONE_PATTERN = "^1[0-9]\\d{9}$";// 手机号码

    private final static String CARD_NUMBER_PATTERN = ".{"
            + Constants.MEMBER_CARD_ALL_LENGTH + "}+(?=\n)";
    private final static Pattern mobile = Pattern.compile(MOBLIE_PHONE_PATTERN);
    private final static Pattern card = Pattern.compile(CARD_NUMBER_PATTERN);

    public static String getUrl(String src) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        Pattern p = Pattern.compile("http://[\\w\\.\\-/:]+",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(src);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    public static boolean isMobileNumber(String number) {
        Matcher m = mobile.matcher(number);
        return m.matches();
    }

    public static String getCardAllNumber(String cardNumber) {
        Matcher m = card.matcher(cardNumber);
        return m.find() ? m.group() : Constants.EMPTY_STR;
    }

    public static String formatMemberName(String memberName) {
        if (!TextUtils.isEmpty(memberName)) {
            StringBuilder sb = new StringBuilder();
            sb.append(memberName.charAt(0));
            int length = memberName.length();
            if (length == 2) {
                sb.append("*");
            } else if (length > 2) {
                for (int i = 1; i < length - 1; i++) {
                    sb.append("*");
                }
                sb.append(memberName.charAt(length - 1));
            }
            return sb.toString();
        } else {
            return memberName;
        }
    }

    public static String formatCardNo(String cardNo) {
        StringBuilder builder = new StringBuilder();
        if (TextUtils.isEmpty(cardNo)) {
            return builder.toString();
        }

        int count = 0;
        int length = cardNo.length();
        for (int index = 0; index < length; ++index) {
            if (++count > 4) {
                builder.append(" ");
                count = 1;
            }
            if (index == length - 3 || index == length - 4
                    || index == length - 5 || index == length - 6) {
                builder.append("*");
            } else {
                builder.append(cardNo.charAt(index));
            }
        }
        return builder.toString();
    }

    public static String formatMobile(String mobile) {
        if (isMobileNumber(mobile)) {
            StringBuilder builder = new StringBuilder();
            builder.append(mobile.substring(0, 3));
            builder.append(" **** ");
            builder.append(mobile.substring(7, 11));
            return builder.toString();
        }
        return mobile;
    }

    public static String getHiddenKeyword(String content, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("the start index " + start
                    + " is bigger than the end index " + end);
        }

        int length = content.length();
        if (length <= start) {
            return content;
        } else {
            String newStr = content.substring(0, start);
            int addStarCount = (length < end ? length : end) - start;
            for (int i = 0; i < addStarCount; i++) {
                newStr += Constants.STAR_CHAR;
            }
            if (length > end) {
                newStr += content.substring(end, length);
            }
            return newStr;
        }
    }
}
