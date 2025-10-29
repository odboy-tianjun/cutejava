package cn.odboy.system.dal.redis;

import cn.hutool.core.util.StrUtil;
import cn.odboy.base.CsPageResult;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.redis.CsRedisHelper;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.system.dal.model.SystemUserOnlineVo;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import cn.odboy.util.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@AllArgsConstructor
public class SystemUserOnlineInfoDAO {
    private final AppProperties properties;
    private final TokenProvider tokenProvider;
    private final CsRedisHelper redisHelper;

    /**
     * 保存在线用户信息
     *
     * @param userJwtVo /
     * @param token     /
     * @param request   /
     */

    public void saveUserJwtModelByToken(SystemUserJwtVo userJwtVo, String token, HttpServletRequest request) {
        String dept = userJwtVo.getUser().getDept().getName();
        String ip = CsBrowserUtil.getIp(request);
        String id = tokenProvider.getId(token);
        String version = CsBrowserUtil.getVersion(request);
        String address = CsIPUtil.getCityInfo(ip);
        SystemUserOnlineVo userOnlineVo = null;
        try {
            userOnlineVo = new SystemUserOnlineVo();
            userOnlineVo.setUid(id);
            userOnlineVo.setUserName(userJwtVo.getUsername());
            userOnlineVo.setNickName(userJwtVo.getUser().getNickName());
            userOnlineVo.setDept(dept);
            userOnlineVo.setBrowser(version);
            userOnlineVo.setIp(ip);
            userOnlineVo.setAddress(address);
            userOnlineVo.setKey(CsDesEncryptUtil.desEncrypt(token));
            userOnlineVo.setLoginTime(new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        String loginKey = tokenProvider.loginKey(token);
        redisHelper.set(loginKey, userOnlineVo, properties.getJwt().getTokenValidityInSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * 退出登录
     *
     * @param token /
     */

    public void logoutByToken(String token) {
        String loginKey = tokenProvider.loginKey(token);
        redisHelper.del(loginKey);
    }

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws IOException /
     */

    public void downloadUserOnlineModelExcel(List<SystemUserOnlineVo> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemUserOnlineVo user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("部门", user.getDept());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        CsFileUtil.downloadExcel(list, response);
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */

    public void kickOutByUsername(String username) {
        String loginKey = SystemRedisKey.ONLINE_USER + username + "*";
        redisHelper.scanDel(loginKey);
    }

    /**
     * 查询全部数据
     *
     * @param onlineVo /
     * @param pageable /
     * @return /
     */

    public CsPageResult<SystemUserOnlineVo> queryUserOnlineModelPage(SystemUserOnlineVo onlineVo, IPage<SystemUserOnlineVo> pageable) {
        String username = null;
        if (onlineVo != null) {
            username = onlineVo.getUserName();
        }
        List<SystemUserOnlineVo> onlineUserList = queryUserOnlineModelListByUsername(username);
        List<SystemUserOnlineVo> paging = CsPageUtil.softPaging(pageable.getCurrent(), pageable.getSize(), onlineUserList);
        return CsPageUtil.toPage(paging, onlineUserList.size());
    }

    /**
     * 查询全部数据，不分页
     *
     * @param username /
     * @return /
     */

    public List<SystemUserOnlineVo> queryUserOnlineModelListByUsername(String username) {
        String loginKey = SystemRedisKey.ONLINE_USER + (StrUtil.isBlank(username) ? "" : "*" + username);
        List<String> keys = redisHelper.scan(loginKey + "*");
        Collections.reverse(keys);
        List<SystemUserOnlineVo> onlineUserList = new ArrayList<>();
        for (String key : keys) {
            onlineUserList.add(redisHelper.get(key, SystemUserOnlineVo.class));
        }
        onlineUserList.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserList;
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */

    public SystemUserOnlineVo queryUserOnlineModelByKey(String key) {
        return redisHelper.get(key, SystemUserOnlineVo.class);
    }
}
