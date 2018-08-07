package com.kill.annotation;

import com.kill.util.ValidatorUtil;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机验证实现类
 *
 * @author Administrator
 * @create 2018-07-16 18:20
 */
public class IsMobileValidator implements  ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    /**
     * 注解初始化方法
     * @param constraintAnnotation
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {

        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(required){
            return ValidatorUtil.isMobile(value);
        } else {
            if(StringUtils.isEmpty(value)){
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
