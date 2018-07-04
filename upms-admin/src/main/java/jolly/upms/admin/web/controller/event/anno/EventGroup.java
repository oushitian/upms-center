package jolly.upms.admin.web.controller.event.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fd
 * @create 2018/01/23 下午 6:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface EventGroup {

    String name();

}
