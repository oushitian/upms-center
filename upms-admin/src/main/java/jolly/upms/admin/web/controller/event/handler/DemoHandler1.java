package jolly.upms.admin.web.controller.event.handler;

import jolly.upms.admin.web.controller.event.anno.EventGroup;
import jolly.upms.admin.web.controller.event.message.DemoMessage;


/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 10:31
 **/
@EventGroup(name = "demo")
public class DemoHandler1 extends SimpleDemoEventHandler{
    @Override
    public boolean condition(String name, DemoMessage message) {
        return "fd".equals(name);
    }

    @Override
    public void handle(DemoMessage message) {
        System.out.println("进入DemoHandler2");
        System.out.println(message);
    }
}
