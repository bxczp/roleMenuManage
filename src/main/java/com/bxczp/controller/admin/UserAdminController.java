package com.bxczp.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bxczp.entity.Role;
import com.bxczp.entity.User;
import com.bxczp.entity.UserRole;
import com.bxczp.service.RoleService;
import com.bxczp.service.UserRoleService;
import com.bxczp.service.UserService;
import com.bxczp.util.StringUtil;

/**
 * 后台管理用户Controller
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/admin/user")
public class UserAdminController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    /**
     * 分页查询用户信息
     * 
     * @param user
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    // @RequiresRoles()
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> list(User user, @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "rows", required = false) Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<User> userList = userService.list(user, page, rows, Direction.ASC, "id");
        for (User u : userList) {
            List<Role> roleList = roleService.findByUserId(u.getId());
            StringBuffer sb = new StringBuffer();
            for (Role r : roleList) {
                sb.append("," + r.getName());
            }
            u.setRoles(sb.toString().replaceFirst(",", ""));
        }
        Long total = userService.getCount(user);
        resultMap.put("rows", userList);
        resultMap.put("total", total);
        return resultMap;
    }

    /**
     * 添加或者修改用户信息
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> save(User user) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (user.getId() == null) {
            if (userService.findByUserName(user.getUserName()) != null) {
                resultMap.put("success", false);
                resultMap.put("errorInfo", "用户名已经存在！");
                return resultMap;
            }
        }
        userService.save(user);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 删除用户信息
     * 
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userRoleService.deleteByUserId(id);
        userService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 保存用户角色设置
     * 
     * @param roleIds
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveRoleSet")
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> saveRoleSet(String roleIds, Integer userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userRoleService.deleteByUserId(userId);
        if (StringUtil.isNotEmpty(roleIds)) {
            String roleIdStr[] = roleIds.split(",");
            for (int i = 0; i < roleIdStr.length; i++) {
                UserRole userRole = new UserRole();
                userRole.setUser(userService.findById(userId));
                userRole.setRole(roleService.findById(Integer.parseInt(roleIdStr[i])));
                userRoleService.save(userRole);
            }
        }
        resultMap.put("success", true);
        return resultMap;
    }
}
