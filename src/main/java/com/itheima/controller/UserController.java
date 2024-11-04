package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.JwtUtil;
import com.itheima.utils.Md5Util;
import com.itheima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^$\\S{5,16}$")String username, @Pattern(regexp = "^$\\S{5,16}$") String password) {

        User u=userService.findByUsername(username);
        if(u==null){
            userService.register(username, password);
            return Result.success("注册成功");
        }
        else{
            return Result.error("用户名已存在");
        }
    }
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^$\\S{5,16}$")String username, @Pattern(regexp = "^$\\S{5,16}$")String password) {
        User u = userService.findByUsername(username);
        if (u == null) {
            return Result.error("用户名错误");
        }

        if (Md5Util.getMD5String(password).equals(u.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", u.getUsername());
            claims.put("id", u.getId());
            String token = JwtUtil.genToken(claims);
            ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
            stringStringValueOperations.set(token, token, 1, TimeUnit.HOURS);


            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo() {
        Map<String, Object> map= ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User u = userService.findByUsername(username);
        if (u == null) {
            return Result.error("用户不存在");
        }
        return Result.success(u);

    }
    @PutMapping("/update")
    //@RequestBody 用于将请求体中的json数据转换成代码可以处理的对象，并将其绑定到user对象中
    public Result update(@RequestBody @Validated User user)     {
        userService.update(user);
        return Result.success("更新成功");
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatar) {
        userService.updateAvatar(avatar);
        return Result.success("更新头像成功");
    }

    @PatchMapping("/updatePwd")
    //注意这里的请求体参数类型为Map<String, String>，而不是User对象，因为前端传过来的数据中并没有id，username等属性，只有oldPassword和newPassword等属性，不符合User对象中的属性，所以这里需要用Map来接收前端传过来的数据
    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader String token ) {
        //1\. 获取请求参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd=params.get("re_pwd");
        if(!StringUtils.hasText(oldPwd) && !StringUtils.hasText(newPwd) && !StringUtils.hasText(rePwd)){
            return Result.error("参数不能为空");
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User u = userService.findByUsername(username);
        if(!u.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码输入不一致");
        }

        userService.updatePwd(newPwd);
        //删除redis对应的token
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.getOperations().delete(token);

        return Result.success("更新密码成功");
    }
}




