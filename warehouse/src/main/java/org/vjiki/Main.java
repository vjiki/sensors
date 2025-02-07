package org.vjiki;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "kafka.monitoring.sensors";

    public static void main(String[] args) throws InterruptedException {

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.
                class);

        SenderOptions<String, String> senderOptions =
                SenderOptions.<String, String>create(producerProps)
                        .maxInFlight(1024)
                        .stopOnError(false);

        //public SenderOptions<K, V> scheduler(Scheduler scheduler);
        // get responses on separate thread

        KafkaSender<String, String> sender = KafkaSender.create(senderOptions);

        sender.doOnProducer(producer -> producer.partitionsFor(TOPIC))
                .doOnSuccess(partitions -> System.out.println("Partitions " + partitions))
                .subscribe();

//        Flux<SenderRecord<Integer, String, Integer>> outboundFlux =
//                Flux.range(1, 10)
//                        .map(i -> SenderRecord.create(topic, partition, timestamp, i,
//                                "Message_" + i, i));

        //     public static <K, V, T> SenderRecord<K, V, T> create(String topic, Integer partition, Long timestamp, K key, V value, T correlationMetadata) {
        Mono<SenderRecord<String,String,String>> outboundMono = Mono.just(
                SenderRecord.create(TOPIC, null, Instant.now().getEpochSecond(), "sensor_id=t1",
                        "value=80", "a"));

        sender.send(outboundMono)
                .doOnError(e-> System.out.println(e.getMessage())) // log.error(e)
                .doOnNext(r ->
                        System.out.printf("Message #%s send response: %s\n",
                r.correlationMetadata(), r.recordMetadata()))
                .subscribe();

        // sender.createOutbound()
        //.send(Flux.range(1, 10)
        //.map(i -> new ProducerRecord<Integer, String>(topic, i,
        //"Message_" + i)))
        //.then()
        //.doOnError(e -> e.printStackTrace())
        //.doOnSuccess(s -> System.out.println("Sends succeeded"))
        //.subscribe();

        // sender.createOutbound()
        //.send(flux1) ①
        //.send(flux2)
        //.send(flux3)
        //.then() ②
        //.doOnError(e -> e.printStackTrace()) ③
        //.doOnSuccess(s -> System.out.println("Sends succeeded")) ④
        //.subscribe();

        Thread.sleep(10000);

        sender.close();

        System.out.println("Hello, World!");
    }
}