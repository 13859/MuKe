package com.itheima.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class User {
    @NotNull //NotNull注解用于在pojo对象创建时，检查该字段是否为空
    private Integer id;//主键ID
    private String username;//用户名
    @JsonIgnore //JsonIgnore注解用于在json序列化时忽略该字段
    private String password;//密码

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}")      //Pattern注解用于检查该字段是否符合正则表达式的规则
    private String nickname;//昵称
    @NotEmpty
    @Email //Email注解用于检查该字段是否为邮箱格式
    private String email;//邮箱
    private String userPic;//用户头像地址
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//更新时间
}
