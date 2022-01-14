package annotation.beans;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

}
