package com.zzy.kafka.example;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

public class KaProducer extends Thread {
    private String topic;

    public KaProducer(String topic) {
        this.topic = topic;
    }

    private Producer<Integer, String> createProducer() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "192.168.2.184:2181,192.168.2.78:2181");// 声明zk
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("metadata.broker.list", "192.168.2.184:9092,192.168.2.78:9092");// 声明kafka broker
        return new Producer<Integer, String>(new ProducerConfig(properties));
    }

    @Override
    public void run() {
        Producer<Integer, String> producer = createProducer();
        int i = 0;
        while (true) {
            producer.send(new KeyedMessage<Integer, String>(topic, "message: " + i++));
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new KaProducer("test").start();// 使用kafka集群中创建好的主题 test
    }
}
