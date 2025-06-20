package cn.odboy.core.dal.redis.system;

import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import cn.odboy.core.dal.model.system.SystemUserOnlineVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface SystemUserOnlineService {
    /**
     * 保存在线用户信息
     *
     * @param userJwtVo /
     * @param token     /
     * @param request   /
     */
    void saveUserJwtModelByToken(SystemUserJwtVo userJwtVo, String token, HttpServletRequest request);


    /**
     * 退出登录
     *
     * @param token /
     */
    void logoutByToken(String token);

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws IOException /
     */
    void downloadUserOnlineModelExcel(List<SystemUserOnlineVo> all, HttpServletResponse response) throws IOException;


    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    void kickOutByUsername(String username);
}
