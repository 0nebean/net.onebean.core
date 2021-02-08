package net.onebean.core.Json;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Inherited
public @interface EnumDeserialize {
    Class<? extends  Enum<?>> using();
}
