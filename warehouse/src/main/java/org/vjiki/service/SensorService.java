package org.vjiki.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.vjiki.config.KafkaSenderConfig;
import reactor.kafka.sender.KafkaSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


@Service
public class SensorService {

    public final KafkaSenderService kafkaSenderService;
    public final KafkaSenderConfig kafkaSenderConfig;

    public SensorService(KafkaSenderConfig kafkaSenderConfig, KafkaSenderService kafkaSenderService) {
        this.kafkaSenderService = kafkaSenderService;
        this.kafkaSenderConfig = kafkaSenderConfig;
    }

    @Async
    public void listenForData(int port) {
        KafkaSender<String, String> kafkaSender = KafkaSender.create(kafkaSenderConfig.senderOptions());
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];
            System.out.println("Listening on port " + port);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedData = new String(packet.getData(), 0, packet.getLength());
                processSensorData(receivedData, kafkaSender);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            kafkaSender.close();
        }
    }

    private void processSensorData(String data, KafkaSender<String, String> kafkaSender) {
        String[] parts = data.split(";");
        if (parts.length < 2) return;

        kafkaSenderService.sendToKafka(parts, kafkaSender, kafkaSenderConfig.getTopic());
    }
}
