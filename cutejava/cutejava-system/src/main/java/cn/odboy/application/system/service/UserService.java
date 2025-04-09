package cn.odboy.application.system.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.request.QueryUserRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService extends IService<User> {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    User getUserById(long id);

    /**
     * 新增用户
     *
     * @param resources /
     */
    void createUser(User resources);

    /**
     * 编辑用户
     *
     * @param resources /
     * @throws Exception /
     */
    void updateUserById(User resources) throws Exception;

    /**
     * 删除用户
     *
     * @param ids /
     */
    void deleteUserByIds(Set<Long> ids);

    /**
     * 根据用户名查询
     *
     * @param username /
     * @return /
     */
    User getUserByUsername(String username);

    /**
     * 修改密码
     *
     * @param username        用户名
     * @param encryptPassword 密码
     */
    void updatePasswordByUsername(String username, String encryptPassword);

    /**
     * 修改头像
     *
     * @param file 文件
     * @return /
     */
    Map<String, String> updateAvatarFile(MultipartFile file);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    void updateEmailByUsername(String username, String email);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<User> queryUserPage(QueryUserRequest criteria, Page<Object> page);

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */
    List<User> selectUserByCriteria(QueryUserRequest criteria);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<User> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 用户自助修改资料
     *
     * @param resources /
     */
    void updateCenterInfoById(User resources);

    /**
     * 重置密码
     *
     * @param ids      用户id
     * @param password 密码
     */
    void resetPasswordByIds(Set<Long> ids, String password);
}
