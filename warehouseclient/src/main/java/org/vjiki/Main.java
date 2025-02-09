package org.vjiki;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main {
    private static final String CENTRAL_HOST = "localhost";
    private static final int TEMP_SENSOR_PORT = 3344;
    private static final int HUMIDITY_SENSOR_PORT = 3355;

    public static void main(String[] args) {

        while(true) {
            try {
                DatagramSocket socket = new DatagramSocket();

                // Simulate temperature sensor data
                String tempData = "sensor_id=t1;value=80";
                sendUdpData(socket, tempData, TEMP_SENSOR_PORT);

                // Simulate humidity sensor data
                String humidityData = "sensor_id=h1;value=90";
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