package cn.odboy.util;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

/**
 * DES加密
 */
@UtilityClass
public final class DesEncryptUtil {

    private static final String STR_PARAM = "Passw0rd";
    private static final IvParameterSpec IV = new IvParameterSpec(STR_PARAM.getBytes(StandardCharsets.UTF_8));

    private static DESKeySpec getDesKeySpec(String source) throws Exception {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return new DESKeySpec(STR_PARAM.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 对称加密
     */
    public static String desEncrypt(String source) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = getDesKeySpec(source);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);
        return byte2hex(cipher.doFinal(source.getBytes(StandardCharsets.UTF_8))).toUpperCase();
    }

    /**
     * 对称解密
     */
    public static String desDecrypt(String source) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        byte[] src = hex2byte(source.getBytes(StandardCharsets.UTF_8));
        DESKeySpec desKeySpec = getDesKeySpec(source);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);
        byte[] retByte = cipher.doFinal(src);
        return new String(retByte);
    }

    private static String byte2hex(byte[] inStr) {
        String stmp;
        StringBuilder out = new StringBuilder(inStr.length * 2);
        for (byte b : inStr) {
            stmp = Integer.toHexString(b & 0xFF);
            if (stmp.length() == 1) {
                out.append("0").append(stmp);
            } else {
                out.append(stmp);
            }
        }
        return out.toString();
    }

    private static byte[] hex2byte(byte[] b) {
        int size = 2;
        if ((b.length % size) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += size) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
}

