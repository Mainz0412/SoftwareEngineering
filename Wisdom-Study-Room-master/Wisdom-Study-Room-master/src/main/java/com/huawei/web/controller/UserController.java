package com.huawei.web.controller;

import com.huawei.web.entity.Room;
import com.huawei.web.entity.User;
import com.huawei.web.service.UserService;
import com.huawei.web.util.AjaxResult;
import com.huawei.web.util.constant.Constant;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.huawei.web.util.TokenUtils.genToken;

/**
 * @author Ruijie Zhao
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    /**
     * 用户注册
     *
     * @param user 用户
     * @return msg信息
     */
    @PostMapping("/register")
    public AjaxResult register(@RequestBody User user) {
        User requestUser = userService.selectUserName(user);
        User requestUser2 = userService.selectUserAccount(user.getUserAccount());
        if (requestUser != null || requestUser2 != null) {
            return AjaxResult.error("用户名或账号重复");
        }
        //设置为普通用户
        user.setUserPrivilege(0);
        userService.insertUser(user);
        return AjaxResult.success();
    }

    /**
     * 用户登录
     *
     * @param user 用户
     * @return msg信息
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user) {
        AjaxResult ajax = AjaxResult.success();
        User loginUser = userService.login(user);
        if (loginUser == null) {
            return AjaxResult.error("用户名或密码错误");
        }
        String token = genToken(loginUser);
        ajax.put(Constant.TOKEN, loginUser.getUserPrivilege());
        return ajax;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return msg结果
     */
    @PutMapping("/update")
    public AjaxResult update(@RequestBody User user) {
        // 检查新的用户名是否存在
        User requestUser = userService.selectUserNameExclude(user);
        if (requestUser != null) {
            return AjaxResult.error("用户名重复");
        }
        //更新结果
        userService.updateUser(user);
        return AjaxResult.success();
    }

    /**
     * 删除用户
     *
     * @param user 用户
     * @return msg信息
     */
    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestBody User user) {
        //删除房间
        userService.deleteUser(user);
        return AjaxResult.success();
    }

    /**
     * 获得用户list
     *
     * @param userAccount 用户
     * @return 用户list
     */
    @GetMapping(value = "/list")
    public List<User> list(@RequestParam("userAccount") String userAccount) {
        return userService.selectUserList(userAccount);
    }

    /**
     * 获得所有用户list
     *
     * @return 用户list
     */
    @GetMapping(value = "/all")
    public List<User> listAll() {
        return userService.selectAllList();
    }
}
