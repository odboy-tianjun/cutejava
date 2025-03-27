package cn.odboy.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static cn.odboy.util.DESEncryptUtil.desDecrypt;
import static cn.odboy.util.DESEncryptUtil.desEncrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DESEncryptUtilTest {

    @SneakyThrows
    public static void main(String[] args) {
        System.err.println(desEncrypt("abc12345678"));
    }

    /**
     * 对称加密
     */
    @Test
    public void testDesEncrypt() {
        try {
            assertEquals("7772841DC6099402", desEncrypt("abc12345678"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对称解密
     */
    @Test
    public void testDesDecrypt() {
        try {
            assertEquals("abc12345678", desDecrypt("7772841DC6099402"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
