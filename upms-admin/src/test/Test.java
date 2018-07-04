import com.jolly.upms.manager.service.AuthService;
import jolly.upms.admin.web.controller.AuthenticationAccessController;
import jolly.upms.admin.web.controller.event.Eventors;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-04 11:04
 **/
@RunWith(SpringJUnit4ClassRunner.class) //指定测试用例的运行器 这里是指定了Junit4
@ContextConfiguration({"classpath:spring/upms-context.xml"})
public class Test {

    @Autowired
    private Eventors eventors;


    @org.junit.Test
    public void test() throws Exception {
        String json = "{\n" +
                " \"id\":\"1\",\n" +
                " \"name\":\"fd\",\n" +
                "}";
        eventors.fireGroupEventsDynamic("demo", json);
    }

    @org.junit.Test
    public void testSingle() throws Exception {
        String json = "{\n" +
                " \"id\":\"2\",\n" +
                " \"name\":\"fd\",\n" +
                "}";
        eventors.fireEventDynamic("singleDemo",json);
    }

    @org.junit.Test
    public void testSingle1() throws Exception {
        String json = "{\n" +
                " \"id\":\"2\",\n" +
                " \"name\":\"fd\",\n" +
                "}";
        eventors.fireEventDynamic("singleDemo1",json);
    }

}
