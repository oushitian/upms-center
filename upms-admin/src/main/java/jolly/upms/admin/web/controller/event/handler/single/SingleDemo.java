package jolly.upms.admin.web.controller.event.handler.single;

import jolly.upms.admin.web.controller.event.SimpleSingleEventHandler;
import jolly.upms.admin.web.controller.event.anno.EventName;
import jolly.upms.admin.web.controller.event.message.DemoMessage;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 13:41
 **/
@EventName(name = "singleDemo")
public class SingleDemo extends SimpleSingleEventHandler<DemoMessage> {

    @Override
    public void handle(DemoMessage message) {
        System.out.println("单事件模式！");
        System.out.println(message);
    }
}
