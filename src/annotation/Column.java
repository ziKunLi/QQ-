package annotation;
import java.lang.annotation.*;
/**
 * 
 * @author Sun
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String name();
    String type() default "varchar";
    int length() default 20;
    boolean isNull() default false;
}