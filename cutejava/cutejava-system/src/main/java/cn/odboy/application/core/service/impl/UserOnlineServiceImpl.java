package cn.odboy.application.core.service.impl;

import cn.odboy.application.core.config.SecurityProperties;
import cn.odboy.application.core.context.TokenProvider;
import cn.odboy.application.core.service.UserOnlineService;
import cn.odboy.base.PageResult;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.model.system.dto.UserJwtDto;
import cn.odboy.model.system.dto.UserOnlineDto;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.BrowserUtil;
import cn.odboy.util.DesEncryptUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.IpUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@AllArgsConstructor
public class UserOnlineServiceImpl implements UserOnlineService {
    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;
    private final RedisHelper redisHelper;

    @Override
    public void save(UserJwtDto userJwtDto, String token, HttpServletRequest request) {
        String dept = userJwtDto.getUser().getDept().getName();
        String ip = BrowserUtil.getIp(request);
        String id = tokenProvider.getId(token);
        String version = BrowserUtil.getVersion(request);
        String address = IpUtil.getCityInfo(ip);
        UserOnlineDto userOnlineDto = null;
        try {
            userOnlineDto = new UserOnlineDto();
            userOnlineDto.setUid(id);
            userOnlineDto.setUserName(userJwtDto.getUsername());
            userOnlineDto.setNickName(userJwtDto.getUser().getNickName());
            userOnlineDto.setDept(dept);
            userOnlineDto.setBrowser(version);
            userOnlineDto.setIp(ip);
            userOnlineDto.setAddress(address);
            userOnlineDto.setKey(DesEncryptUtil.desEncrypt(token));
            userOnlineDto.setLoginTime(new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String loginKey = tokenProvider.loginKey(token);
        redisHelper.set(loginKey, userOnlineDto, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    @Override
    public PageResult<UserOnlineDto> queryOnlineUserPage(String username, Pageable pageable) {
        List<UserOnlineDto> onlineUserList = selectOnlineUserByUsername(username);
        List<UserOnlineDto> paging = PageUtil.softPaging(pageable.getPageNumber(), pageable.getPageSize(), onlineUserList);
        return PageUtil.toPage(paging, onlineUserList.size());
    }

    @Override
    public List<UserOnlineDto> selectOnlineUserByUsername(String username) {
        String loginKey = SystemRedisKey.ONLINE_USER + (StringUtil.isBlank(username) ? "" : "*" + username);
        List<String> keys = redisHelper.scan(loginKey + "*");
        Collections.reverse(keys);
        List<UserOnlineDto> onlineUserList = new ArrayList<>();
        for (String key : keys) {
            onlineUserList.add(redisHelper.get(key, UserOnlineDto.class));
        }
        onlineUserList.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserList;
    }

    @Override
    public void logoutByToken(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisHelper.del(loginKey);
    }

    @Override
    public void downloadExcel(List<UserOnlineDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserOnlineDto user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("部门", user.getDept());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public UserOnlineDto getOnlineUserByKey(String key) {
        return redisHelper.get(key, UserOnlineDto.class);
    }

    @Override
    public void kickOutByUsername(String username) {
        String loginKey = SystemRedisKey.ONLINE_USER + username + "*";
        redisHelper.scanDel(loginKey);
    }
}
