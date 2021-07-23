package cn.hnust.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密类
 * SHA-1
 */
public class CryptUtil {

    public static String cryptPwd(String password, String salt) {
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("密码不能为空");
        }
        if (StringUtils.isEmpty(salt)) {
            throw new RuntimeException("salt is null");
        }
        byte[] saltByte = Encodes.decodeHex(salt);
        byte[] hashPassword = Digests.sha1(password.getBytes(), saltByte, 1024);
        return Encodes.encodeHex(hashPassword);
    }

    public static String cryptPassword(String plain) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(plain.getBytes("UTF-8"));
            byte[] array = md.digest();
            String byte2Str = byte2Str(array);
            byte[] newArray = byte2Str.getBytes();
            result = new String(Base64.encodeBase64(newArray));
            if (result != null) {
                result = result.trim();
            }
        } catch (NoSuchAlgorithmException e) {
            //log.info(e);
        } catch (UnsupportedEncodingException e) {
            //log.info(e);
        }
        return result;

    }

    private static String byte2Str(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(
                    Integer.toHexString(
                            (0x000000ff & bytes[i]) | 0xffffff00).substring(
                            6));
        }
        return sb.toString().trim();
    }
}
