package org.vjiki.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {
    @Value("${warehouse.temperature.sensor.port:3344}")
    public final Integer temperatureSensorPort = 3344;
    @Value("${warehouse.humidity.sensor.port:3355}")
    public final Integer humiditySensorPort = 3355;

    public final SensorService sensorService;

    public WarehouseService(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostConstruct
    public void processRecords() {
        sensorService.listenForData(temperatureSensorPort);
        sensorService.listenForData(humiditySensorPort);
    }
}
