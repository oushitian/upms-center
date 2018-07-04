package jolly.upms.admin.web.controller.event;

import com.alibaba.fastjson.JSON;
import jolly.upms.admin.web.controller.event.context.BaseEventContext;
import jolly.upms.admin.web.controller.event.context.GroupEventContext;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * 描述: 事件总管
 *
 * @author fd
 * @create 2018-07-04 10:23
 **/
@Component
public class Eventors {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Eventors.class);

    @Autowired
    private EventHandlerSelector eventHandlerSelector;

    /**
     * 触发事件,只触发一个Handler
     *
     * @param eventName:唯一的事件名称
     * @param eventMessage
     * @throws Exception
     */
    public void fireEvent(String eventName, Object eventMessage) throws Exception {
        fireEvent(eventName, eventName, eventMessage);
    }

    /**
     * 触发单事件
     *
     * @param groupName
     * @param eventName
     * @param eventMessage:参数,可以是任意形式的数据对象
     * @throws Exception
     */
    public void fireEvent(String groupName, String eventName, Object eventMessage) throws Exception {
        fireEvent(groupName, eventName, eventMessage, false);
    }

    /**
     * 触发事件,只触发一个Handler
     *
     * @param eventName:唯一的事件名称
     * @param messageJson       原始json消息格式
     * @throws Exception
     */
    public void fireEventDynamic(String eventName, String messageJson) throws Exception {
        fireEventDynamic(eventName, eventName, messageJson);
    }

    /**
     * 触发单事件
     * 支持对messageJson进行动态对象转换
     *
     * @param groupName
     * @param eventName
     * @param messageJson 原始json消息格式
     * @throws Exception
     */
    public void fireEventDynamic(String groupName, String eventName, String messageJson) throws Exception {
        fireEvent(groupName, eventName, messageJson, true);
    }

    /**
     * 触发一组Handler执行(只要符合条件)
     *
     * @param groupName    组名,
     * @param eventMessage 任意格式的参数
     */
    public void fireGroupEvents(String groupName, Object eventMessage) throws Exception {
        fireEvent(groupName, groupName, eventMessage, true, false);
    }

    /**
     * 触发一组Handler执行(只要符合条件)
     *
     * @param groupName   组名
     * @param messageJson 原始json消息格式
     */
    public void fireGroupEventsDynamic(String groupName, String messageJson) throws Exception {
        fireEvent(groupName, groupName, messageJson, true, true);
    }

    private void fireEvent(String groupName, String eventName, Object eventMessage, boolean dynamic) throws Exception {
        if (StringUtils.isBlank(eventName)) {
            eventName = groupName;
        }
        fireEvent(groupName, eventName, eventMessage, false, dynamic);
    }

    private Object packageRealEventMessage(Object eventMessage, Class target) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object realEventMessage = eventMessage;
        if (target != null && eventMessage != null) {
            String messageJson = String.valueOf(eventMessage);
            realEventMessage = JSON.parseObject(messageJson, target);
            //为了方便如果有设置messageJson字段,则进行赋值,
            // 也可以约定handler的消息格式,比如必须继承某一个基类,包含了该字段,则可以直接赋值,相对依赖程度更大一些
            String messageJsonProperty = "messageJson";
            if (PropertyUtils.isWriteable(realEventMessage, messageJsonProperty)) {
                PropertyUtils.setProperty(realEventMessage, messageJsonProperty, messageJson);
            }
        }
        return realEventMessage;
    }

    /**
     * 组别名为groupName的事件触发
     *
     * @param groupName
     * @param eventName    事件名,
     * @param eventMessage
     */
    private void fireEvent(String groupName, String eventName, Object eventMessage, boolean isGroup, boolean dynamic) throws Exception {
        boolean hasException = false;
        Exception latestException = null;
        BaseEventContext eventContext = eventHandlerSelector.select(groupName, eventName, isGroup);
        if (eventContext == null) {
            return;
        }
        Object realEventMessage = eventMessage;
        Class target = null;
        //dynamic模式时,需要进行转换
        if (isGroup && dynamic) {
            target = ((GroupEventContext) eventContext).getMessageBeanClass();
            realEventMessage = packageRealEventMessage(eventMessage, target);
        }
        for (AbstractEventHandler eventHandler : eventContext.getEventHandlers()) {
            if ((!isGroup) && dynamic) {
                target = eventHandler.getMessageBeanClass();
                realEventMessage = packageRealEventMessage(eventMessage, target);
            }
            //每一个handler独立处理
            boolean isHandled = false;
            try {
                if (eventHandler.condition(realEventMessage)) {
                    eventHandler.handle(realEventMessage);
                    logger.info("EventHandler:{} is fired", eventHandler.getClass().getSimpleName());
                    isHandled = true;
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                isHandled = false;
                hasException = true;
                latestException = e;
            } finally {
                try {
                    eventHandler.handleCompleted(isHandled, realEventMessage);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    hasException = true;
                    latestException = e;
                }
            }
        }
        if (hasException) {
            throw latestException;
        }
    }
}

