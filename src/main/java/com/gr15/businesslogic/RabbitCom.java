package com.gr15.businesslogic;

import com.gr15.messaging.models.Event;
import com.gr15.messaging.rabbitmq.RabbitMqListener;
import com.gr15.messaging.rabbitmq.RabbitMqSender;

import java.util.EventListener;

public class RabbitCom {
    private RabbitMqListener rabbitMqListener;
    private RabbitMqSender rabbitMqSender;

    public RabbitCom() {
        rabbitMqSender = new RabbitMqSender();
    }

    public void broadCastDelete(String accountId) {
        // Broadcast the deletion of an account
        Event deleteEvent = new Event("Delete", "Deleted the account: " + accountId);
        String exchangeName = "TODO";
        String queueType = "TODO";
        String topic = "TODO";
        try {
            rabbitMqSender.sendEvent(deleteEvent, exchangeName, queueType, topic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
