package jolly.upms.admin.web.controller.listen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.logging.log4j.LogManager;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * 描述:
 *
 * @author fd
 * @create 2018-07-05 11:14
 **/
public abstract class BaseListener implements ChannelAwareMessageListener {

    public static String ERROR_QUEUE_SUFFIX = ".error" ;
    public static final String ERROR_MESSAGE_KEY="errorMsg";
    protected static org.apache.logging.log4j.Logger logger = LogManager.getLogger(BaseListener.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            processMessage(message);
        }catch (Exception e){
            //把处理异常的信息放入错误队列中
            e.printStackTrace();
            try{
                logger.error(e.getMessage(),e);
                addError(message,channel,e);
            }catch (Exception e1){
                //TODO 此处需要记录持久化日志，因为此次异常已经无法解决了，要保证消息不丢失(比如存入数据库)
                logger.error(e.getMessage());
            }
        }finally {
            //在finally中设置应答，尽量确保消息不会重复消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),Boolean.FALSE);
        }
    }

    //将处理失败的消息放入错误队列中，待之后的再处理
    private void addError(Message message, Channel channel, Exception e) throws Exception{
        byte[] errorMsgBody = null;
        try {
            JSONObject jsonObj = JSON.parseObject(new String(message.getBody()));
            jsonObj.put(ERROR_MESSAGE_KEY, e.getMessage());
            errorMsgBody = jsonObj.toJSONString().getBytes();
        }catch (Exception e2){
            logger.error("Original Message Body: "+new String(message.getBody()));
            e2.printStackTrace();
            errorMsgBody = message.getBody();
        }

        String queueName = message.getMessageProperties().getConsumerQueue() ;
        String errorQueueName = queueName + ERROR_QUEUE_SUFFIX ;

        channel.queueDeclare(errorQueueName, true, false, false, null) ;
        channel.basicPublish("", errorQueueName,
                MessageProperties.PERSISTENT_TEXT_PLAIN, errorMsgBody);
    }

    /**
     * 模板方法,供子类实现
     */
    protected abstract void processMessage(Message message);

}
