package io.cnnex.mqtt.controller;

import io.cnnex.mqtt.biz.MQServiceImpl;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/mqtt")
public class MQTTController {
    @Autowired
    private MQServiceImpl mqServiceImpl;

    @GetMapping("/publish")
    public String publishMsg() throws MqttException {
        String host = "127.0.0.1";
        String port = "1883";
        String username = "root";
        String password = "root";
        String topic = "my_topic";
        String msgStr = "There is a new message @ " + new Date();

        if (mqServiceImpl.initializeClient(host, port, "harvey1102", username, password)) {
            mqServiceImpl.publishMessage(topic, msgStr.getBytes());
            System.out.println(topic + " "  + msgStr  + " " + msgStr.getBytes().length);
        }
        mqServiceImpl.subscribeTopic(topic);

        return "success";
    }

}
