package io.cnnex.mqtt.biz;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface MQService {

    boolean initializeClient(String host, String port, String client_id, String userName, String password);

    void subscribeTopic(String topic) throws MqttException;

    void publishMessage(String topic, byte[] message);

    void messageHandler(String topic, MqttMessage mqttMessage);

}
