package cn.odboy.core.framework.permission.core.handler;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.odboy.constant.SystemConst;
import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.framework.properties.AppProperties;
import cn.odboy.redis.RedisHelper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;
    private final RedisHelper redisHelper;
    private final AppProperties properties;
    public static final String AUTHORITIES_UUID_KEY = "uid";
    public static final String AUTHORITIES_UID_KEY = "userId";

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwt().getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512);
    }

    /**
     * 创建Token 设置永不过期，
     * Token 的时间有效性转到Redis 维护
     *
     * @param user /
     * @return /
     */
    public String createToken(SystemUserJwtVo user) {
        // 设置参数
        Map<String, Object> claims = new HashMap<>(6);
        // 设置用户ID
        claims.put(AUTHORITIES_UID_KEY, user.getUser().getId());
        // 设置UUID，确保每次Token不一样
        claims.put(AUTHORITIES_UUID_KEY, IdUtil.simpleUUID());
        return jwtBuilder
                .setClaims(claims)
                .setSubject(user.getUsername())
                .compact();
    }

    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
    Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        User principal = new User(claims.getSubject(), "******", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
    }

    public Claims getClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * @param token 需要检查的token
     */
    public void checkRenewal(String token) {
        // 判断是否续期token,计算token的过期时间
        String loginKey = loginKey(token);
        long time = redisHelper.getExpire(loginKey) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= properties.getJwt().getDetect()) {
            long renew = time + properties.getJwt().getRenew();
            redisHelper.expire(loginKey, renew, TimeUnit.MILLISECONDS);
        }
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(SystemConst.HEADER_NAME);
        if (requestHeader != null && requestHeader.startsWith(SystemConst.TOKEN_PREFIX)) {
            return requestHeader.substring(7);
        }
        return null;
    }

    /**
     * 获取登录用户RedisKey
     *
     * @param token /
     * @return key
     */
    public String loginKey(String token) {
        Claims claims = getClaims(token);
        return SystemRedisKey.ONLINE_USER + claims.getSubject() + ":" + getId(token);
    }

    /**
     * 获取会话编号
     *
     * @param token /
     * @return /
     */
    public String getId(String token) {
        Claims claims = getClaims(token);
        return claims.get(AUTHORITIES_UUID_KEY, String.class);
    }
}
