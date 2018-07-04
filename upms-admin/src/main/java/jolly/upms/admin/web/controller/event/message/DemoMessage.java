package jolly.upms.admin.web.controller.event.message;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 10:33
 **/
public class DemoMessage {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DemoMessage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
