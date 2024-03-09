package io.cnnex.mqtt.biz;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQClient {

    private final MQServiceImpl mqServiceImpl;

    private MqttClient client;

    private final String host;
    private final String port;
    private final String client_id;
    private final String userName;
    private final String password;

    private MQClient(MQServiceImpl mqServiceImpl, String host, String port, String client_id,
                     String userName, String password) {
        this.mqServiceImpl = mqServiceImpl;
        this.host = host;
        this.port = port;
        this.client_id = client_id;
        this.userName = userName;
        this.password = password;
        connectToBroker();
    }

    // Singleton mode
    public static MQClient getClient(MQServiceImpl mqServiceImpl, String host, String port,
                              String client_id, String userName, String password) {
        return new MQClient(mqServiceImpl, host, port, client_id, userName, password);
    }

    private void connectToBroker() {
        try {
            String hostUrl = "tcp://" + this.host + ":" + this.port;
            client = new MqttClient(hostUrl, client_id, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(20);
            options.setUserName(userName);
            options.setPassword(password.toCharArray());
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Disconnected");
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    mqServiceImpl.messageHandler(s, mqttMessage);
                    String payload = new String(mqttMessage.getPayload());
                    System.out.println("messageArrived: " + s + " : " + payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("Delivered: " + iMqttDeliveryToken);
                }
            });

            client.connect(options);

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, byte[] data, Integer qos, Boolean retained) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(data);
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        MqttTopic mqttTopic = client.getTopic(topic);
        try {
            MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        } catch (MqttException e) {
            System.out.println(e.getMessage());
        }
    }

    public void subscribe(String topic, int qos) throws MqttException {
        client.subscribe(topic, qos);
    }

    public void close(String msg) {
        try {
            Thread.sleep(1000);
            client.close();
        } catch (MqttException | InterruptedException e) {
            try {
                client.disconnect();
                client.close();
            } catch (MqttException ignored) {
            }
        }
    }
}
