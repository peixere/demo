package cn.gotom.pojos;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注释
 * 
 * @author peixere@qq.com
 * 
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Note
{
	String value() default "";

	Class<?> clazz() default Note.class;
}
