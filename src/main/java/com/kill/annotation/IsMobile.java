/*
 * Bean Validation API
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.kill.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull.List;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * class_name: IsMobile
 * package: com.kill.annotation
 * describe: 手机验证注解
 * creat_user: sl
 * creat_date: 2018/7/16
 * creat_time: 18:21
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { IsMobileValidator.class})
public @interface IsMobile {

	/**
	 * 是否为必填
	 * @return
	 */
	boolean required() default true;

	String message() default "手机验证不通过";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
