package cn.odboy.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResultVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.SystemMenuVo;
import cn.odboy.system.dal.model.SystemQueryMenuArgs;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.system.service.SystemMenuService;
import cn.odboy.util.CsPageUtil;
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
@RequestMapping("/api/menu")
public class SystemMenuController {
    private final SystemMenuService systemMenuService;

    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void exportMenu(HttpServletResponse response, SystemQueryMenuArgs criteria) throws Exception {
        systemMenuService.exportMenuExcel(systemMenuService.queryAllMenu(criteria, false), response);
    }

    @PostMapping(value = "/buildMenus")
    @ApiOperation("获取前端所需菜单")
    public ResponseEntity<List<SystemMenuVo>> buildMenus() {
        List<SystemMenuTb> menuList = systemMenuService.queryMenuListByUserId(CsSecurityHelper.getCurrentUserId());
        List<SystemMenuTb> menus = systemMenuService.buildMenuTree(menuList);
        return new ResponseEntity<>(systemMenuService.buildMenuVo(menus), HttpStatus.OK);
    }

    @ApiOperation("返回全部的菜单")
    @PostMapping(value = "/queryMenuListByPid")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<List<SystemMenuTb>> queryMenuListByPid(@RequestParam Long pid) {
        return new ResponseEntity<>(systemMenuService.queryMenuByPid(pid), HttpStatus.OK);
    }

    @ApiOperation("根据菜单ID返回所有子节点ID, 包含自身ID")
    @PostMapping(value = "/queryChildMenuSet")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Set<Long>> queryChildMenuSet(@RequestParam Long id) {
        Set<SystemMenuTb> menuSet = new HashSet<>();
        List<SystemMenuTb> menuList = systemMenuService.queryMenuByPid(id);
        menuSet.add(systemMenuService.getMenuById(id));
        menuSet = systemMenuService.queryChildMenu(menuList, menuSet);
        Set<Long> ids = menuSet.stream().map(SystemMenuTb::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("查询菜单")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<CsPageResultVo<List<SystemMenuTb>>> queryAllMenuByCrud(@Validated @RequestBody CsPageArgs<SystemQueryMenuArgs> args) throws Exception {
        return queryAllMenu(args);
    }

    @PostMapping(value = "/queryMenuByArgs")
    @ApiOperation("查询菜单")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<CsPageResultVo<List<SystemMenuTb>>> queryAllMenu(@Validated @RequestBody CsPageArgs<SystemQueryMenuArgs> args) throws Exception {
        SystemQueryMenuArgs criteria = args.getArgs();
        List<SystemMenuTb> menuList = systemMenuService.queryAllMenu(criteria, true);
        return new ResponseEntity<>(CsPageUtil.toPage(menuList), HttpStatus.OK);
    }

    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/queryMenuSuperior")
    @PreAuthorize("@el.check('menu:list')")
    public ResponseEntity<List<SystemMenuTb>> queryMenuSuperior(@RequestBody List<Long> ids) {
        Set<SystemMenuTb> menus = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                SystemMenuTb menu = systemMenuService.getMenuById(id);
                List<SystemMenuTb> menuList = systemMenuService.querySuperiorMenuList(menu, new ArrayList<>());
                for (SystemMenuTb data : menuList) {
                    if (data.getId().equals(menu.getPid())) {
                        data.setSubCount(data.getSubCount() - 1);
                    }
                }
                menus.addAll(menuList);
            }
            // 编辑菜单时不显示自己以及自己下级的数据, 避免出现PID数据环形问题
            menus = menus.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toSet());
            return new ResponseEntity<>(systemMenuService.buildMenuTree(new ArrayList<>(menus)), HttpStatus.OK);
        }
        return new ResponseEntity<>(systemMenuService.queryMenuByPid(null), HttpStatus.OK);
    }

    @ApiOperation("新增菜单")
    @PostMapping(value = "/saveMenu")
    @PreAuthorize("@el.check('menu:add')")
    public ResponseEntity<Void> saveMenu(@Validated @RequestBody SystemMenuTb resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("无效参数id");
        }
        systemMenuService.saveMenu(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改菜单")
    @PostMapping(value = "/modifyMenuById")
    @PreAuthorize("@el.check('menu:edit')")
    public ResponseEntity<Void> modifyMenuById(@Validated(SystemMenuTb.Update.class) @RequestBody SystemMenuTb resources) {
        systemMenuService.modifyMenuById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除菜单")
    @PostMapping(value = "/removeMenuByIds")
    @PreAuthorize("@el.check('menu:del')")
    public ResponseEntity<Void> removeMenuByIds(@RequestBody Set<Long> ids) {
        Set<SystemMenuTb> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<SystemMenuTb> menuList = systemMenuService.queryMenuByPid(id);
            menuSet.add(systemMenuService.getMenuById(id));
            menuSet = systemMenuService.queryChildMenu(menuList, menuSet);
        }
        systemMenuService.removeMenuByIds(menuSet);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
