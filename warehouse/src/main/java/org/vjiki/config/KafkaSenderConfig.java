package org.vjiki.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("warehouse.kafka.sender")
public class KafkaSenderConfig {

    private String topic = "kafka.monitoring.sensors";
    private String bootstrapServers = "localhost:9092";
    private Integer maxInFlight = 1024;
    private Boolean stopOnError = false;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public Integer getMaxInFlight() {
        return maxInFlight;
    }

    public void setMaxInFlight(Integer maxInFlight) {
        this.maxInFlight = maxInFlight;
    }

    public Boolean getStopOnError() {
        return stopOnError;
    }

    public void setStopOnError(Boolean stopOnError) {
        this.stopOnError = stopOnError;
    }

    public SenderOptions<String, String> senderOptions() {
        return SenderOptions.<String, String>create(producerProps())
                        .maxInFlight(maxInFlight)
                        .stopOnError(stopOnError);
    }

    public Map<String, Object> producerProps() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.
                class);

        return producerProps;
    }
}
