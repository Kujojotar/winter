package annotation.beans;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WinterConstructor {
    Class[] types();
    String[] values() default "";
}
