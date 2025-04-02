package cn.odboy.application.core.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.model.UserJwtModel;
import cn.odboy.model.system.model.UserOnlineModel;
import org.springframework.data.domain.Pageable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserOnlineService {
    /**
     * 保存在线用户信息
     *
     * @param userJwtModel /
     * @param token      /
     * @param request    /
     */
    void save(UserJwtModel userJwtModel, String token, HttpServletRequest request);

    /**
     * 查询全部数据
     *
     * @param username /
     * @param pageable /
     * @return /
     */
    PageResult<UserOnlineModel> queryOnlineUserPage(String username, Pageable pageable);

    /**
     * 查询全部数据，不分页
     *
     * @param username /
     * @return /
     */
    List<UserOnlineModel> selectOnlineUserByUsername(String username);

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
    void downloadExcel(List<UserOnlineModel> all, HttpServletResponse response) throws IOException;

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    UserOnlineModel getOnlineUserByKey(String key);

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    void kickOutByUsername(String username);
}
