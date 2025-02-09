package org.vjiki.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Instant;

@Service
public class KafkaSenderService {

    public void sendToKafka(String[] parts, KafkaSender<String, String> kafkaSender, String topic) {

//        String sensorId = parts[0].split("=")[1];
//        int value = Integer.parseInt(parts[1].split("=")[1]);

//        if ((sensorId.startsWith("t") && value > TEMP_THRESHOLD) || (sensorId.startsWith("h") && value > HUMIDITY_THRESHOLD)) {
//            System.out.println("ALARM: " + sensorId + " exceeded threshold with value " + value);
//        }




//        sender.doOnProducer(producer -> producer.partitionsFor(kafkaSenderConfig.getTopic()))
//                .doOnSuccess(partitions -> System.out.println("Partitions " + partitions))
//                .subscribe();

//        Flux<SenderRecord<Integer, String, Integer>> outboundFlux =
//                Flux.range(1, 10)
//                        .map(i -> SenderRecord.create(topic, partition, timestamp, i,
//                                "Message_" + i, i));

        //     public static <K, V, T> SenderRecord<K, V, T> create(String topic, Integer partition, Long timestamp, K key, V value, T correlationMetadata) {
        Mono<SenderRecord<String,String,String>> outboundMono = Mono.just(
                SenderRecord.create(topic, null, Instant.now().getEpochSecond(),
                        parts[0], parts[1], "a"));

        kafkaSender.send(outboundMono)
                .doOnError(e-> System.out.println(e.getMessage())) // log.error(e)
                .doOnNext(r -> System.out.printf("Message #%s send response: %s\n",
                        r.correlationMetadata(), r.recordMetadata()))
                .subscribe();

//        Thread.sleep(100000);



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

    }
//
}
