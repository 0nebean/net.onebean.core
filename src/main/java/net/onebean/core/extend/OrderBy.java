package net.onebean.core.extend;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在生成通用Mybatis的Mapping时 ，如果在实体类上有此注解，则生成的find方法里会带上此注解的值
 * 如 @OrderBy( "id desc")

 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface OrderBy {
	public String value();
}
