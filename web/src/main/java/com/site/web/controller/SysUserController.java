package com.site.web.controller;

import com.site.web.plugins.logging.annotation.Logging;
import com.site.web.plugins.logging.enums.BusinessType;
import com.site.web.plugins.repeat.annotation.RepeatSubmit;
import com.site.web.utils.security.SecurityUtil;
import com.site.web.utils.sequence.SequenceUtil;
import com.site.web.utils.servlet.ServletUtil;
import com.site.web.web.base.BaseController;
import com.site.web.web.domain.response.Result;
import com.site.web.domain.SysMenu;
import com.site.web.domain.SysUser;
import com.site.web.service.ISysRoleService;
import com.site.web.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @Describe 用户控制器
 * @author  lenyuqin‘
 * */
@RestController
@RequestMapping("system/user")
@Api(value="用户controller",tags={"用户操作接口"})
public class SysUserController extends BaseController {

    /**
     * Describe: 基础路径
     * */
    private static final String MODULE_PATH = "system/user/";

    /**
     * Describe: 用户模块服务
     * */
    @Resource
    private ISysUserService sysUserService;

    /**
     * Describe: 角色模块服务
     * */
    @Resource
    private ISysRoleService sysRoleService;

    /**
     * Describe: 获取用户列表视图
     * Param ModelAndView
     * Return 用户列表视图
     * */
    @GetMapping("main")
    @ApiOperation(value="获取用户列表视图")
    //@PreAuthorize("hasPermission('/system/user/main','sys:user:main')")
    public ModelAndView main( ){
        return JumpPage(MODULE_PATH + "main");
    }

    /**
     * Describe: 获取用户列表数据
     * Param ModelAndView
     * todo 这个是分页 需要进行修改
     * Return 用户列表数据
     * */
    //@GetMapping("data")
    //@ApiOperation(value="获取用户列表数据")
    ////@PreAuthorize("hasPermission('/system/user/data','sys:user:data')")
    //@Logging(title = "查询用户",describe = "查询用户",type = BusinessType.QUERY)
    //public ResultTable data(PageDomain pageDomain, SysUser param){
    //    PageInfo<SysUser> pageInfo = sysUserService.page(param,pageDomain);
    //    return pageTable(pageInfo.getList(),pageInfo.getTotal());
    //}

    /**
     * Describe: 用户新增视图
     * Param ModelAndView
     * Return 返回用户新增视图
     * */
    @GetMapping("add")
    @ApiOperation(value="获取用户新增视图")
    //@PreAuthorize("hasPermission('/system/user/add','sys:user:add')")
    public ModelAndView add(Model model){
        model.addAttribute("sysRoles",sysRoleService.list(null));
        return JumpPage(MODULE_PATH+"add");
    }

    /**
     * Describe: 用户新增接口
     * Param ModelAndView
     * Return 操作结果
     * */
    @RepeatSubmit
    @PostMapping("save")
    @ApiOperation(value="保存用户数据")
    //@PreAuthorize("hasPermission('/system/user/add','sys:user:add')")
    //@Logging(title = "新增用户",describe = "新增用户",type = BusinessType.ADD)
    public Result save(@RequestBody SysUser sysUser){
        sysUser.setLogin("0");
        sysUser.setEnable("1");
        sysUser.setStatus("1");
        sysUser.setUserId(SequenceUtil.makeStringId());
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setPassword(new BCryptPasswordEncoder().encode(sysUser.getPassword()));
        sysUserService.saveUserRole(sysUser.getUserId(), Arrays.asList(sysUser.getRoleIds().split(",")));
        Boolean result = sysUserService.save(sysUser);
        return decide(result);
    }

    /**
     * Describe: 用户修改视图
     * Param ModelAndView
     * Return 返回用户修改视图
     * */
    @GetMapping("edit")
    @ApiOperation(value="获取用户修改视图")
    //@PreAuthorize("hasPermission('/system/user/edit','sys:user:edit')")
    public  ModelAndView edit(Model model,String userId){
        model.addAttribute("sysRoles",sysUserService.getUserRole(userId));
        model.addAttribute("sysUser",sysUserService.getById(userId));
        return JumpPage(MODULE_PATH + "edit");
    }

    /**
     * Describe: 用户密码修改视图
     * Param ModelAndView
     * Return 返回用户密码修改视图
     * */
    @GetMapping("editPassword")
    public ModelAndView editPasswordView(){
        return JumpPage(MODULE_PATH + "editPassword");
    }

    @PutMapping("editPassword")
    public Result editPassword(String oldPassword,String newPassword,String confirmPassword){
        SysUser sysUser = (SysUser) ServletUtil.getSession().getAttribute("currentUser");
        SysUser editUser = sysUserService.getById(sysUser.getUserId());
        if(Strings.isBlank(confirmPassword)
        || Strings.isBlank(newPassword)
        || Strings.isBlank(oldPassword)){
            return failure("输入不能为空");
        }
        if(!new BCryptPasswordEncoder().matches(oldPassword,editUser.getPassword())){
            return failure("密码验证失败");
        }
        if(!newPassword.equals(confirmPassword)){
            return failure("两次密码输入不一致");
        }
        editUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        boolean result = sysUserService.update(editUser);
        return decide(result,"修改成功","修改失败");
    }

    /**
     * Describe: 用户修改接口
     * Param ModelAndView
     * Return 返回用户修改接口
     * */
    @PutMapping("update")
    @ApiOperation(value="修改用户数据")
    //@PreAuthorize("hasPermission('/system/user/edit','sys:user:edit')")
    //@Logging(title = "修改用户",describe = "修改用户",type = BusinessType.EDIT)
    public Result update(@RequestBody SysUser sysUser){
        sysUserService.saveUserRole(sysUser.getUserId(), Arrays.asList(sysUser.getRoleIds().split(",")));
        boolean result = sysUserService.update(sysUser);
        return decide(result);
    }

    /**
     * Describe: 用户批量删除接口
     * Param: ids
     * Return: ResuBean
     * */
    @DeleteMapping("batchRemove/{ids}")
    @ApiOperation(value="批量删除用户")
    //@PreAuthorize("hasPermission('/system/user/remove','sys:user:remove')")
    //@Logging(title = "删除用户",describe = "删除用户",type = BusinessType.REMOVE)
    public Result batchRemove(@PathVariable String ids){
        boolean result = sysUserService.batchRemove(ids.split(","));
        return decide(result);
    }

    /**
     * Describe: 用户删除接口
     * Param: id
     * Return: ResuBean
     * */
    @Transactional
    @DeleteMapping("remove/{id}")
    @ApiOperation(value="删除用户数据")
    //@PreAuthorize("hasPermission('/system/user/remove','sys:user:remove')")
    //@Logging(title = "删除用户",describe = "删除用户",type = BusinessType.REMOVE)
    public Result remove(@PathVariable String id){
        // TODO remove userRole data
        boolean result  = sysUserService.remove(id);
        return decide(result);
    }

    /**
     * Describe: 根据 username 获取菜单数据
     * todo 查询菜单
     * Param SysRole
     * Return 执行结果
     * */
    @GetMapping("getUserMenu")
    @ApiOperation(value = "获取用户菜单数据")
    public List<SysMenu> getUserMenu(){
        SysUser sysUser = (SysUser) SecurityUtil.currentUser().getPrincipal();
        List<SysMenu> menus = sysUserService.getUserMenu(sysUser.getUsername());
        //for (SysMenu menu : menus) {
        //    System.out.println(menu.getTitle()+"===>"+menu.getHref());
        //}
        return sysUserService.toUserMenu(menus,"0");
    }

    /**
     * Describe: 根据 userId 开启用户
     * Param: SysUser
     * Return: 执行结果
     * */
    @PutMapping("enable")
    @ApiOperation(value = "开启用户登录")
    public Result enable(@RequestBody SysUser sysUser){
        sysUser.setEnable("1");
        boolean result = sysUserService.update(sysUser);
        return decide(result);
    }

    /**
     * Describe: 根据 userId 禁用用户
     * Param: SysUser
     * Return: 执行结果
     * */
    @PutMapping("disable")
    @ApiOperation(value = "禁用用户登录")
    public Result disable(@RequestBody SysUser sysUser){
        sysUser.setEnable("0");
        boolean result = sysUserService.update(sysUser);
        return decide(result);
    }

    /**
     * Describe: 跳转用户个人资料
     * Param: null
     * Return: ModelAndView
     * */
    @GetMapping("center")
    @ApiOperation(value = "个人资料")
    public ModelAndView center(Model model){
        SysUser sysUser = (SysUser) SecurityUtil.currentUser().getPrincipal();
        model.addAttribute("userInfo",sysUserService.getById(sysUser.getUserId()));
        return JumpPage(MODULE_PATH + "center");
    }

}
