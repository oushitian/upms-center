package jolly.upms.admin.web.controller.event;

import jolly.upms.admin.web.controller.event.utils.ClassUtils;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 9:27
 **/
public abstract class AbstractEventHandler<T> {

    /**
     * 事件参数Class,构造器中初始化
     * Json动态转换Bean时使用
     */
    private Class<T> messageBeanClass;

    public AbstractEventHandler() {
        messageBeanClass = ClassUtils.getGenericType(this.getClass());
    }


    /**
     * 处理前逻辑,可以进行参数校验,数据适配,数据转换等
     *
     * @param message:入参
     * @return true:执行handle,否则不执行
     */
    public abstract boolean condition(T message);

    /**
     * 核心处理逻辑实现
     *
     * @param message:入参
     */
    public abstract void handle(T message);

    /**
     * 处理完成后逻辑
     *
     * @param isHandled:是否handle方法已经执行
     * @param message:入参
     */
    public void handleCompleted(Boolean isHandled, T message) {
        //do nothing
    }

    public Class<T> getMessageBeanClass() {
        return messageBeanClass;
    }
}
