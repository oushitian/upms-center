package jolly.upms.admin.web.controller.listen;

import org.springframework.amqp.core.Message;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-05 11:13
 **/
public class PushMessageListener extends BaseListener{

    @Override
    protected void processMessage(Message message) {
        int a = 1/0;
        System.out.println("接受到消息："+new String(message.getBody()));
    }
}
