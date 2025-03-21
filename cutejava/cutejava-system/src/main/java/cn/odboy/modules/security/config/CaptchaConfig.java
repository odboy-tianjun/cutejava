package cn.odboy.modules.security.config;

import cn.odboy.exception.BadRequestException;
import cn.odboy.modules.security.config.enums.LoginCodeEnum;
import cn.odboy.util.StringUtil;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * 登录验证码配置信息
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "login.code")
public class CaptchaConfig {

    /**
     * 验证码配置
     */
    @Getter
    private LoginCodeEnum codeType;

    /**
     * 验证码有效期 分钟
     */
    private Long expiration = 5L;

    /**
     * 验证码内容长度
     */
    private int length = 4;

    /**
     * 验证码宽度
     */
    private int width = 111;

    /**
     * 验证码高度
     */
    private int height = 36;

    /**
     * 验证码字体
     */
    private String fontName;

    /**
     * 字体大小
     */
    private int fontSize = 25;

    /**
     * 依据配置信息生产验证码
     *
     * @return /
     */
    public Captcha getCaptcha() {
        Captcha captcha;
        switch (codeType) {
            case ARITHMETIC:
                // 算术类型 https://gitee.com/whvse/EasyCaptcha
                captcha = new FixedArithmeticCaptcha(width, height);
                // 几位数运算，默认是两位
                captcha.setLen(length);
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(width, height);
                captcha.setLen(length);
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(width, height);
                captcha.setLen(length);
                break;
            case GIF:
                captcha = new GifCaptcha(width, height);
                captcha.setLen(length);
                break;
            case SPEC:
                captcha = new SpecCaptcha(width, height);
                captcha.setLen(length);
                break;
            default:
                throw new BadRequestException("验证码配置信息错误！正确配置查看 LoginCodeEnum ");
        }
        if (StringUtil.isNotBlank(fontName)) {
            captcha.setFont(new Font(fontName, Font.PLAIN, fontSize));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10), n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            char optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}
