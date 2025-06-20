package cn.odboy.core.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.service.system.SystemMenuApi;
import cn.odboy.core.dal.dataobject.system.SystemMenuTb;
import cn.odboy.core.dal.model.system.SystemMenuVo;
import cn.odboy.core.framework.permission.core.SecurityHelper;
import cn.odboy.core.service.system.SystemMenuService;
import cn.odboy.core.dal.model.system.QuerySystemMenuArgs;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@RequestMapping("/api/menus")
public class SystemMenuController {
    private static final String ENTITY_NAME = "menu";
    private final SystemMenuApi systemMenuApi;
    private final SystemMenuService systemMenuService;

    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void exportMenu(HttpServletResponse response, QuerySystemMenuArgs criteria) throws Exception {
        systemMenuService.downloadMenuExcel(systemMenuApi.describeMenuList(criteria, false), response);
    }

    @PostMapping(value = "/buildMenus")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<List<SystemMenuVo>> buildMenus() {
        List<SystemMenuTb> menuList = systemMenuApi.describeMenuListByUserId(SecurityHelper.getCurrentUserId());
        List<SystemMenuTb> menus = systemMenuApi.buildMenuTree(menuList);
        return new ResponseEntity<>(systemMenuApi.buildMenuVo(menus), HttpStatus.OK);
    }

    @ApiOperation("返回全部的菜单")
    @PostMapping(value = "/describeMenuListByPid")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<List<SystemMenuTb>> describeMenuListByPid(@RequestParam Long pid) {
        return new ResponseEntity<>(systemMenuApi.describeMenuListByPid(pid), HttpStatus.OK);
    }

    @ApiOperation("根据菜单ID返回所有子节点ID，包含自身ID")
    @PostMapping(value = "/describeChildMenuSet")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> describeChildMenuSet(@RequestParam Long id) {
        Set<SystemMenuTb> menuSet = new HashSet<>();
        List<SystemMenuTb> menuList = systemMenuApi.describeMenuListByPid(id);
        menuSet.add(systemMenuApi.describeMenuById(id));
        menuSet = systemMenuApi.describeChildMenuSet(menuList, menuSet);
        Set<Long> ids = menuSet.stream().map(SystemMenuTb::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("查询菜单")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<CsResultVo<List<SystemMenuTb>>> queryMenu(QuerySystemMenuArgs criteria) throws Exception {
        List<SystemMenuTb> menuList = systemMenuApi.describeMenuList(criteria, true);
        return new ResponseEntity<>(PageUtil.toPage(menuList), HttpStatus.OK);
    }

    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/describeMenuSuperior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<List<SystemMenuTb>> describeMenuSuperior(@RequestBody List<Long> ids) {
        Set<SystemMenuTb> menus = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                SystemMenuTb menu = systemMenuApi.describeMenuById(id);
                List<SystemMenuTb> menuList = systemMenuApi.describeSuperiorMenuList(menu, new ArrayList<>());
                for (SystemMenuTb data : menuList) {
                    if (data.getId().equals(menu.getPid())) {
                        data.setSubCount(data.getSubCount() - 1);
                    }
                }
                menus.addAll(menuList);
            }
            // 编辑菜单时不显示自己以及自己下级的数据，避免出现PID数据环形问题
            menus = menus.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
            return new ResponseEntity<>(systemMenuApi.buildMenuTree(new ArrayList<>(menus)), HttpStatus.OK);
        }
        return new ResponseEntity<>(systemMenuApi.describeMenuListByPid(null), HttpStatus.OK);
    }

    @ApiOperation("新增菜单")
    @PostMapping(value = "/saveMenu")
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<Object> saveMenu(@Validated @RequestBody SystemMenuTb resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        systemMenuService.saveMenu(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改菜单")
    @PostMapping(value = "/modifyMenuById")
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<Object> modifyMenuById(@Validated(SystemMenuTb.Update.class) @RequestBody SystemMenuTb resources) {
        systemMenuService.modifyMenuById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除菜单")
    @PostMapping(value = "/removeMenuByIds")
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<Object> removeMenuByIds(@RequestBody Set<Long> ids) {
        Set<SystemMenuTb> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<SystemMenuTb> menuList = systemMenuApi.describeMenuListByPid(id);
            menuSet.add(systemMenuApi.describeMenuById(id));
            menuSet = systemMenuApi.describeChildMenuSet(menuList, menuSet);
        }
        systemMenuService.removeMenuByIds(menuSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
