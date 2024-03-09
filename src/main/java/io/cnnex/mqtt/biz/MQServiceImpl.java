package io.cnnex.mqtt.biz;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MQServiceImpl implements MQService{

    private MQClient mqClient;

    @Override
    public boolean initializeClient(String host, String port, String client_id, String userName, String password) {
        mqClient = MQClient.getClient(this, host, port, client_id, userName, password);
        return true;
    }

    @Override
    public void subscribeTopic(String topic) throws MqttException {
        mqClient.subscribe(topic, 1);
    }

    @Override
    public void publishMessage(String topic, byte[] message) {
        mqClient.publish(topic, message, 1, true);
    }

    @Override
    public void messageHandler(String topic, MqttMessage mqttMessage) {

    }
}
