package jolly.upms.admin.web.controller.event;

import jolly.upms.admin.web.controller.event.anno.EventGroup;
import jolly.upms.admin.web.controller.event.anno.EventName;
import jolly.upms.admin.web.controller.event.context.BaseEventContext;
import jolly.upms.admin.web.controller.event.context.GroupEventContext;
import jolly.upms.admin.web.controller.event.context.SingleEventContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 9:30
 **/
@Component
public class EventHandlerSelector {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EventHandlerSelector.class);

    private Map<String,Map<String,SingleEventContext>> handlerSingleMap;
    private Map<String, GroupEventContext> handlerGroupMap;

    //自动注入继承了AbstractEventHandler的类
    @Autowired
    public void setEventHandlers(List<AbstractEventHandler> eventHandlers){
        handlerSingleMap = new HashMap<>(16);
        handlerGroupMap = new HashMap<>();
        for (AbstractEventHandler eventHandler : eventHandlers) {
            EventGroup eventGroupAnno = eventHandler.getClass().getAnnotation(EventGroup.class);
            EventName eventNameAnno = eventHandler.getClass().getAnnotation(EventName.class);
            if (eventGroupAnno == null && eventNameAnno == null) {
                logger.error("eventHandler:{} must be marked with @GroupEventHandler or @SingleEventHandler", eventHandler.getClass().getName());
                continue;
            }
            //得到注解的名称
            String groupName = null;
            if (eventGroupAnno != null) {
                groupName = eventGroupAnno.name();
            }
            String eventName = null;
            if (eventNameAnno != null) {
                eventName = eventNameAnno.name();
            }
            if (StringUtils.isBlank(groupName) && StringUtils.isBlank(eventName)) {
                logger.error("eventHandler:{}, @GroupEventHandler or @SingleEventHandler must be set with value", eventHandler.getClass().getName());
                continue;
            }
            //事件模式
            if(eventNameAnno != null){
                if (StringUtils.isBlank(eventName)) {
                    logger.error("{} eventName must be set in event mode(marked as @EventName)", eventHandler.getClass().getName());
                    continue;
                }
                if(StringUtils.isBlank(groupName)){
                    groupName = eventName;
                }
                //根据名称得到上下文map
                Map<String,SingleEventContext> singleEventContextMap = handlerSingleMap.get(groupName);
                if (singleEventContextMap == null) {
                    singleEventContextMap = new HashMap<>(2);
                    handlerSingleMap.put(groupName,singleEventContextMap);
                }
                if (singleEventContextMap.containsKey(eventName)) {
                    logger.error("eventName:{} and groupName:{} is duplicated", eventName, groupName);
                    continue;
                }
                SingleEventContext context = new SingleEventContext();
                context.addEventHandler(eventHandler);
                singleEventContextMap.put(eventName,context);
            }else{//事件组模式
                if (StringUtils.isBlank(groupName)) {
                    logger.error("{} groupName must be set in group mode(marked as @EventGroup)", eventHandler.getClass().getName());
                    continue;
                }
                //组模式
                GroupEventContext context = handlerGroupMap.get(groupName);
                if (context == null) {
                    context = new GroupEventContext();
                    //组模式的MessageBeanClass必须一致,由第一个handler初始化
                    context.setMessageBeanClass(eventHandler.getMessageBeanClass());
                    handlerGroupMap.put(groupName, context);
                }
                //判断MessageBeanClass必须一致
                if (!eventHandler.getMessageBeanClass().equals(context.getMessageBeanClass())) {
                    logger.error("{},{} must own the same MessageBeanClass in group mode(marked as @EventGroup)", eventHandler.getClass().getName(), groupName);
                    continue;
                }
                context.addEventHandler(eventHandler);
            }
        }
    }

    public BaseEventContext select(String groupName,String eventName,boolean isGroup) throws Exception{
        if (StringUtils.isBlank(groupName) || StringUtils.isBlank(eventName)) {
            throw new IllegalArgumentException("invalid params while fireEvent");
        }
        if (isGroup && handlerGroupMap == null) {
            throw new IllegalArgumentException("Eventors has no handler in context");
        }
        if (!isGroup && handlerSingleMap == null) {
            throw new IllegalArgumentException("Eventors has no handler in context");
        }
        BaseEventContext eventContext;
        if (!isGroup) {
            Map<String, SingleEventContext> handlerMap = handlerSingleMap.get(groupName);
            if (handlerMap == null) {
                logger.warn("groupName {} has not been registered", groupName);
                return null;
            }
            eventContext = handlerMap.get(eventName);
            if (eventContext == null) {
                logger.warn("eventName {} has not been registered in event mode", eventName);
            }
        } else {
            eventContext = handlerGroupMap.get(groupName);
            if (eventContext == null) {
                logger.warn("groupName {} has not been registered in group mode", groupName);
            }
        }
        return eventContext;
    }

}
