package cn.odboy.modules.security.service.impl;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.dto.JwtUserDto;
import cn.odboy.model.system.dto.OnlineUserDto;
import cn.odboy.modules.security.config.SecurityProperties;
import cn.odboy.modules.security.context.TokenProvider;
import cn.odboy.modules.security.service.OnlineUserService;
import cn.odboy.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@AllArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {

    private final SecurityProperties properties;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    @Override
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request) {
        String dept = jwtUserDto.getUser().getDept().getName();
        String ip = StringUtil.getIp(request);
        String id = tokenProvider.getId(token);
        String browser = StringUtil.getBrowser(request);
        String address = StringUtil.getCityInfo(ip);
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(id, jwtUserDto.getUsername(), jwtUserDto.getUser().getNickName(), dept, browser, ip, address, EncryptUtil.desEncrypt(token), new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String loginKey = tokenProvider.loginKey(token);
        redisUtil.set(loginKey, onlineUserDto, properties.getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    @Override
    public PageResult<OnlineUserDto> getAll(String username, Pageable pageable) {
        List<OnlineUserDto> onlineUserDtos = getAll(username);
        List<OnlineUserDto> paging = PageUtil.paging(pageable.getPageNumber(), pageable.getPageSize(), onlineUserDtos);
        return PageUtil.toPage(paging, onlineUserDtos.size());
    }

    @Override
    public List<OnlineUserDto> getAll(String username) {
        String loginKey = properties.getOnlineKey() +
                (StringUtil.isBlank(username) ? "" : "*" + username);
        List<String> keys = redisUtil.scan(loginKey + "*");
        Collections.reverse(keys);
        List<OnlineUserDto> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            onlineUserDtos.add(redisUtil.get(key, OnlineUserDto.class));
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserDtos;
    }

    @Override
    public void logout(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisUtil.del(loginKey);
    }

    @Override
    public void download(List<OnlineUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUserDto user : all) {
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
    public OnlineUserDto getOne(String key) {
        return redisUtil.get(key, OnlineUserDto.class);
    }

    @Override
    public void kickOutForUsername(String username) {
        String loginKey = properties.getOnlineKey() + username + "*";
        redisUtil.scanDel(loginKey);
    }
}
