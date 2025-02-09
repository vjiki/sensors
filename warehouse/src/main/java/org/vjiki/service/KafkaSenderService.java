package org.vjiki.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Instant;

@Service
public class KafkaSenderService {

    public void sendToKafka(String[] parts, KafkaSender<String, String> kafkaSender, String topic) {
        Mono<SenderRecord<String,String,String>> outboundMono = Mono.just(
                SenderRecord.create(topic, null, Instant.now().getEpochSecond(),
                        parts[0], parts[1], "a"));

        kafkaSender.send(outboundMono)
                .doOnError(e-> System.out.println(e.getMessage())) // log.error(e)
                .doOnNext(r -> System.out.printf("Message #%s send response: %s\n",
                        r.correlationMetadata(), r.recordMetadata()))
                .subscribe();
    }
}
