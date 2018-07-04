package jolly.upms.admin.web.controller.event.context;

/**
 * 描述:事件组的上下文
 *
 * @author fd
 * @create 2018-07-04 9:29
 **/
public class GroupEventContext extends BaseEventContext{

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Object.class);
    /**
     * 事件参数Class,一个组只允许是同一个Class
     * Json动态转换Bean时使用
     */
    private Class messageBeanClass;

    public GroupEventContext() {
        setGroup(true);
    }

    public Class getMessageBeanClass() {
        return messageBeanClass;
    }

    public void setMessageBeanClass(Class messageBeanClass) {
        this.messageBeanClass = messageBeanClass;
    }

}
