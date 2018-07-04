package jolly.upms.admin.web.controller.event;

/**
 * 描述:固定的Handler
 * Event name和handler是简单一 一绑定关系
 *
 * @author fd
 * @create 2018-07-04 10:26
 **/
public abstract class SimpleSingleEventHandler<T> extends AbstractEventHandler<T> {

    /**
     * 处理前逻辑,可以进行参数校验,数据适配,数据转换等
     *
     * @param message :入参
     * @return true:执行handle,否则不执行
     */
    @Override
    public boolean condition(T message) {
        return true;
    }
}
