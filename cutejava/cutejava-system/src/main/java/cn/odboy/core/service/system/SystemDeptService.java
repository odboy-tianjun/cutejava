package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import cn.odboy.core.dal.model.system.CreateSystemDeptArgs;
import cn.odboy.core.dal.model.system.QuerySystemDeptArgs;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SystemDeptService extends IService<SystemDeptTb> {

    /**
     * 创建
     *
     * @param resources /
     */
    void saveDept(CreateSystemDeptArgs resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void modifyDept(SystemDeptTb resources);

    /**
     * 删除
     *
     * @param depts /
     */
    void removeDeptByIds(Set<SystemDeptTb> depts);

    /**
     * 导出数据
     *
     * @param depts    待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadDeptExcel(List<SystemDeptTb> depts, HttpServletResponse response) throws IOException;
    /**
     * 查询所有数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */
    List<SystemDeptTb> queryDeptList(QuerySystemDeptArgs criteria, Boolean isQuery) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    SystemDeptTb queryDeptById(Long id);

    /**
     * 根据PID查询
     *
     * @param pid /
     * @return /
     */
    List<SystemDeptTb> queryDeptListByPid(long pid);

    /**
     * 根据角色ID查询
     *
     * @param id /
     * @return /
     */
    Set<SystemDeptTb> queryDeptByRoleId(Long id);

    /**
     * 获取部门下所有关联的部门
     *
     * @param deptList /
     * @param depts    /
     * @return /
     */
    Set<SystemDeptTb> queryRelationDeptSet(List<SystemDeptTb> deptList, Set<SystemDeptTb> depts);

    /**
     * 根据ID获取同级与上级数据
     *
     * @param dept  /
     * @param depts /
     * @return /
     */
    List<SystemDeptTb> querySuperiorDeptListByPid(SystemDeptTb dept, List<SystemDeptTb> depts);

    /**
     * 构建树形数据
     *
     * @param depts /
     * @return /
     */
    CsResultVo<Object> buildDeptTree(List<SystemDeptTb> depts);

    /**
     * 获取
     *
     * @param deptList 、
     * @return 、
     */
    List<Long> queryChildDeptIdListByDeptIds(List<SystemDeptTb> deptList);

    /**
     * 验证是否被角色或用户关联
     *
     * @param depts /
     */
    void verifyBindRelationByIds(Set<SystemDeptTb> depts);

    /**
     * 遍历所有部门和子部门
     *
     * @param ids   /
     */
    Set<SystemDeptTb> traverseDeptByIdWithPids(Set<Long> ids);
}
