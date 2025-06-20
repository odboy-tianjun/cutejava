package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.model.system.CreateSystemRoleArgs;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SystemRoleService extends IService<SystemRoleTb> {


    /**
     * 创建
     *
     * @param resources /
     */
    void saveRole(CreateSystemRoleArgs resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void modifyRoleById(SystemRoleTb resources);

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     */
    void modifyBindMenuById(SystemRoleTb resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void removeRoleByIds(Set<Long> ids);


    /**
     * 导出数据
     *
     * @param roles    待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadRoleExcel(List<SystemRoleTb> roles, HttpServletResponse response) throws IOException;

}
