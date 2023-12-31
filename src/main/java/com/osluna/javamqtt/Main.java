package com.osluna.javamqtt;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class Main implements MqttCallback {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        String broker = "tcp://localhost:1883";
        String clientId = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");

            // Set callback
            client.setCallback(new Main());

            // Subscribe to a topic
            String topic = "test/topic";
            client.subscribe(topic);

            // Publish a message
            String content = "Hello MQTT";
            int qos = 2;
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(topic, message);
            System.out.println("Message published");

        } catch (MqttException me) {
            // Handle exceptions
            System.err.println("reason " + me.getReasonCode());
            System.err.println("msg " + me.getMessage());
            System.err.println("loc " + me.getLocalizedMessage());
            System.err.println("cause " + me.getCause());
            System.err.println("excep " + me);
            LOGGER.log(Level.SEVERE, "Exception caught", me);

        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // Called when the client lost the connection to the broker
        System.out.println("Connection lost! " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Called when a message arrives from the server that matches any subscription made by the client
        System.out.println("Received message: " + new String(message.getPayload()) + "\nTopic: " + topic);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Called when delivery for a message has been completed
    }
}
