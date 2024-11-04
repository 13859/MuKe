package com.itheima.anno;

import com.itheima.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {StateValidation.class}//指定提供检验规则的类
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface State {
    //提供检验失败时的提示信息
    String message() default "state参数的值只能是已发布或者草稿";
    //提供约束的分组信息
    Class<?>[] groups() default {};
    //提供约束的负载信息
    Class<? extends Payload>[] payload() default {};
}
