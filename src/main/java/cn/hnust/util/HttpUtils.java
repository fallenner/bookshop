package cn.hnust.util;

import java.io.UnsupportedEncodingException;

public class HttpUtils {
    public static String convert(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入参数是null");
        }

        StringBuffer sb = new StringBuffer(input.length());
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);

            if ((c >= 0) && (c <= 255))
                sb.append(c);
            else {
                try {
                    byte[] bytes = Character.toString(c).getBytes("utf-8");

                    for (int j = 0; j < bytes.length; ++j) {
                        int tmp = bytes[j];
                        if (tmp < 0) {
                            tmp += 256;
                        }

                        sb.append("%").append(Integer.toHexString(tmp).toUpperCase());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
