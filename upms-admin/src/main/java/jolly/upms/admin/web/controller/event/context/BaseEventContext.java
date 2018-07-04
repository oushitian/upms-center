package jolly.upms.admin.web.controller.event.context;

import jolly.upms.admin.web.controller.event.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 9:27
 **/
public abstract class BaseEventContext {

    private boolean group = false;
    private String groupName;

    protected List<AbstractEventHandler> eventHandlers = new ArrayList<>();

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<AbstractEventHandler> getEventHandlers() {
        return eventHandlers;
    }

    public void setEventHandlers(List<AbstractEventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public void addEventHandler(AbstractEventHandler handler) {
        eventHandlers.add(handler);
    }

}
