package net.onebean.core.Json;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Inherited
public @interface EnableEnumDeserialize {
    public String[] key() default "data";
}
