package org.vjiki.monitoring.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.vjiki.monitoring.config.KafkaReceiverConfig;
import org.vjiki.monitoring.config.ThresholdsConfig;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class AlarmService {
    public final KafkaReceiverConfig kafkaReceiverConfig;
    public final ThresholdsConfig thresholdsConfig;

    public AlarmService(KafkaReceiverConfig kafkaReceiverConfig, ThresholdsConfig thresholdsConfig) {
        this.kafkaReceiverConfig = kafkaReceiverConfig;
        this.thresholdsConfig = thresholdsConfig;
    }

    @PostConstruct
    public void processRecords() {
        KafkaReceiver.create(kafkaReceiverConfig.receiverOptions())
                .receiveAutoAck()
                .retryWhen(Retry.backoff(3,
                        Duration.of(10L, ChronoUnit.SECONDS)))
                .concatMap(r->r)
                .subscribe(r -> {
//                            System.out.printf("Received message: %s\n", r);
                    processSensorData(r.key(), r.value());
                });
    }

    private void processSensorData(String id, String value) {
        int sensorValue = Integer.parseInt(value.split("=")[1]);
        String sensorId = id.split("=")[1];
        if ((sensorId.startsWith("t") && sensorValue > thresholdsConfig.getTemperature() ) ||
                (sensorId.startsWith("h") && sensorValue > thresholdsConfig.getHumidity())) {
            System.out.println("ALARM: " + sensorId + " exceeded threshold with value " + sensorValue);
        }
    }
}
