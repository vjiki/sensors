package org.vjiki;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Main {

    // consumer is not thread safe
    // todo: move to properties
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "kafka.monitoring.sensors";
    private static final String GROUP_ID = "test-consumer-group";
    private static final int TEMP_THRESHOLD = 35;
    private static final int HUMIDITY_THRESHOLD = 50;

    public static void main(String[] args) throws InterruptedException {

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer
                .class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer
                .class);


        ReceiverOptions<String, String> receiverOptions =
                ReceiverOptions.<String, String>create(consumerProps)
//                        .assignment(
//        (Collections.singleton(new TopicPartition(TOPIC, 0))));
                        .subscription(Collections.singleton(TOPIC));

        // ReceiverOptions<Object, Object> options = ReceiverOptions.create()
        //.maxDeferredCommits(100)
        //.subscription(Collections.singletonList("someTopic"));


        // KafkaReceiver.create(receiverOptions)
        //.receiveAutoAck()
        //.concatMap(r -> r) ①
        //.subscribe(r -> System.out.println("Received: " + r)); ②

//        Flux<ReceiverRecord<String, String>> inboundFlux =
//                KafkaReceiver.create(receiverOptions)
//                        .receiveAutoAck()
//                        .concatMap(r -> r)
//                        .;


        KafkaReceiver.create(receiverOptions)
                        .receiveAutoAck()
                        .retryWhen(Retry.backoff(3,
                                Duration.of(10L, ChronoUnit.SECONDS)))
                        .concatMap(r->r)
                        .subscribe(r -> {
//                            System.out.printf("Received message: %s\n", r);
                            processSensorData(r.key(), r.value());
                        });


        // receiverOptions = receiverOptions.assignment(Collections.singleton(new TopicPartition
        //(topic, 0)));

//        KafkaReceiver.create(receiverOptions)
//                        .receive()
//                        .doOnNext(r -> {
//                            System.out.println("Received: " + r);
//                    r.receiverOffset().commit().block();
//                });

//        receiver.receive()
//                .doOnNext(r -> {
//                    process(r);
//                    r.receiverOffset().commit().block();
//                });
//
//        inboundFlux
//
//
//        KafkaReceiver.create(receiverOptions)
//                .receiveAutoAck()
//                .concatMap(r -> r)
//                .subscribe(r -> System.out.println("Received: " + r));


        Thread.sleep(100000);


        System.out.println("Hello, World!");
    }

    private static void processSensorData(String id, String value) {
        int sensorValue = Integer.parseInt(value.split("=")[1]);
        String sensorId = id.split("=")[1];
        if ((sensorId.startsWith("t") && sensorValue > TEMP_THRESHOLD) ||
                (sensorId.startsWith("h") && sensorValue > HUMIDITY_THRESHOLD)) {
            System.out.println("ALARM: " + sensorId + " exceeded threshold with value " + sensorValue);
        }
    }
}