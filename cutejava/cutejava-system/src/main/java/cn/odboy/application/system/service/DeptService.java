package cn.odboy.application.system.service;

import cn.odboy.base.BaseResult;
import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.dto.DeptQueryCriteria;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeptService extends IService<Dept> {

    /**
     * 查询所有数据
     *
     * @param criteria 条件
     * @param isQuery  /
     * @return /
     * @throws Exception /
     */
    List<Dept> selectDeptByCriteria(DeptQueryCriteria criteria, Boolean isQuery) throws Exception;

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    Dept getDeptById(Long id);

    /**
     * 创建
     *
     * @param resources /
     */
    void createDept(Dept resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateDept(Dept resources);

    /**
     * 删除
     *
     * @param depts /
     */
    void deleteDeptByIds(Set<Dept> depts);

    /**
     * 根据PID查询
     *
     * @param pid /
     * @return /
     */
    List<Dept> selectDeptByPid(long pid);

    /**
     * 根据角色ID查询
     *
     * @param id /
     * @return /
     */
    Set<Dept> selectDeptByRoleId(Long id);

    /**
     * 导出数据
     *
     * @param depts    待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<Dept> depts, HttpServletResponse response) throws IOException;

    /**
     * 获取部门下所有关联的部门
     *
     * @param deptList /
     * @param depts    /
     * @return /
     */
    Set<Dept> selectRelationDept(List<Dept> deptList, Set<Dept> depts);

    /**
     * 根据ID获取同级与上级数据
     *
     * @param dept  /
     * @param depts /
     * @return /
     */
    List<Dept> selectSuperiorDeptByPid(Dept dept, List<Dept> depts);

    /**
     * 构建树形数据
     *
     * @param depts /
     * @return /
     */
    BaseResult<Object> buildTree(List<Dept> depts);

    /**
     * 获取
     *
     * @param deptList 、
     * @return 、
     */
    List<Long> selectChildDeptIdByDeptIds(List<Dept> deptList);

    /**
     * 验证是否被角色或用户关联
     *
     * @param depts /
     */
    void verifyBindRelationByIds(Set<Dept> depts);

    /**
     * 遍历所有部门和子部门
     *
     * @param ids   /
     * @param depts /
     */
    void traverseDeptByIdWithPids(Set<Long> ids, Set<Dept> depts);
}