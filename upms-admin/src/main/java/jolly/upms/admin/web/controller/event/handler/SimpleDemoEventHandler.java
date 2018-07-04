package jolly.upms.admin.web.controller.event.handler;

import jolly.upms.admin.web.controller.event.SimpleGroupEventHandler;
import jolly.upms.admin.web.controller.event.message.DemoMessage;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 10:32
 **/
public abstract class SimpleDemoEventHandler extends SimpleGroupEventHandler<DemoMessage> {

    /**
     * 处理前逻辑,可以进行参数校验,数据适配,数据转换等
     *
     * @param message :入参
     * @return true:执行handle,否则不执行
     */
    @Override
    public boolean condition(DemoMessage message) {
        if (message == null) {
            return false;
        }
        return condition(message.getName(),message);
    }

    /**
     * 处理前逻辑,可以进行参数校验,数据适配,数据转换等
     *
     * @param message :入参
     * @return true:执行handle,否则不执行
     */
    public abstract boolean condition(String name, DemoMessage message);

}
