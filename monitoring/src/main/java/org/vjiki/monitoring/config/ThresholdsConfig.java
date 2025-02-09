package org.vjiki.monitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("monitoring.thresholds")
public class ThresholdsConfig {
    private Integer temperature = 35;
    private Integer humidity = 50;

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }
}

