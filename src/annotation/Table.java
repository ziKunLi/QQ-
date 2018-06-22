package annotation;
import java.lang.annotation.*;
/**
 * 
 * @author Sun
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	 String name();
}
