package org.vjiki;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Main {
    private static final String CENTRAL_HOST = "localhost";
    private static final int TEMP_SENSOR_PORT = 3344;
    private static final int HUMIDITY_SENSOR_PORT = 3355;

    public static void main(String[] args) {

        while(true) {
            try {
                DatagramSocket socket = new DatagramSocket();
                Random random = new Random();
                int temp =  random.nextInt(100);
                int humidity =  random.nextInt(100);

                // Simulate temperature sensor data
                String tempData = String.format("sensor_id=t1;value=%d", temp);
                sendUdpData(socket, tempData, TEMP_SENSOR_PORT);

                // Simulate humidity sensor data
                String humidityData = String.format( "sensor_id=h1;value=%d", humidity);
                sendUdpData(socket, humidityData, HUMIDITY_SENSOR_PORT);

                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void sendUdpData(DatagramSocket socket, String data, int port) throws IOException {
        byte[] buffer = data.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(CENTRAL_HOST), port);
        socket.send(packet);
        System.out.println("Sent: " + data + " to port " + port);
    }
}