/**
 * jollycorp.com Inc.
 * Copyright (c) 2013-2016 All Rights Reserved.
 */

package com.jolly.upms.manager.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * 引入调用mq接口服务
 * @author shixiang
 * @version MqUtil.class, v 0.1 2016/12/12 0012 上午 9:39 shixiang Exp $$
 */
public class MqUtil {
    private static final Logger logger = LoggerFactory.getLogger(MqUtil.class);

    private static final String AMQP_TEMPLATE = "amqpTemplate";

    private static final String EXCHANGE = "jollychic.topic";

    private static AmqpTemplate amqpTemplate = SpringUtil.getBean(AMQP_TEMPLATE, AmqpTemplate.class);

    public static String getDefaultExchange() {
        return EXCHANGE;
    }

    public static boolean sendMessage(String exChange, String routingKey, String message) {
        if (StringUtils.isEmpty(exChange)) {
            exChange = getDefaultExchange();
        }
        if (StringUtils.isEmpty(message) || StringUtils.isEmpty(routingKey)) {
            throw new RuntimeException("message或routingKey不允许为空!");
        }
        String logHead = "MQ消息[exChange:" + exChange + ",routingKey:" + routingKey + ",message:" + message + "]:";
        try {
            amqpTemplate.convertAndSend(exChange, routingKey, message);
            logger.info(logHead + "发送成功!");
            return true;
        } catch (AmqpException e) {
            logger.error(logHead + "发送异常!", e);
            throw new RuntimeException(logHead + "发送异常!", e);
        }
    }

    public static boolean sendMessage(String message, String routing) {
        return sendMessage(getDefaultExchange(), routing ,message);
    }




}