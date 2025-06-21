package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.QuerySystemUserArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SystemUserService extends IService<SystemUserTb> {
    /**
     * 新增用户
     *
     * @param resources /
     */
    void saveUser(SystemUserTb resources);

    /**
     * 编辑用户
     *
     * @param resources /
     * @throws Exception /
     */
    void modifyUserById(SystemUserTb resources) throws Exception;

    /**
     * 删除用户
     *
     * @param ids /
     */
    void removeUserByIds(Set<Long> ids);

    /**
     * 修改密码
     *
     * @param username        用户名
     * @param encryptPassword 密码
     */
    void modifyUserPasswordByUsername(String username, String encryptPassword);

    /**
     * 修改头像
     *
     * @param file 文件
     * @return /
     */
    Map<String, String> modifyUserAvatar(MultipartFile file);

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */
    void modifyUserEmailByUsername(String username, String email);

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadUserExcel(List<SystemUserTb> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 用户自助修改资料
     *
     * @param resources /
     */
    void modifyUserCenterInfoById(SystemUserTb resources);

    /**
     * 重置密码
     *
     * @param ids      用户id
     * @param password 密码
     */
    void resetUserPasswordByIds(Set<Long> ids, String password);


    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    SystemUserTb queryUserById(long id);

    /**
     * 根据用户名查询
     *
     * @param username /
     * @return /
     */
    SystemUserTb queryUserByUsername(String username);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemUserTb>> queryUserPage(QuerySystemUserArgs criteria, Page<Object> page);

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemUserTb> queryUserList(QuerySystemUserArgs criteria);
}
