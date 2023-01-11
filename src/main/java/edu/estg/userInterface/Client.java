package edu.estg.userInterface;

import edu.estg.userInterface.GUI.InitialFrame;

import java.io.*;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.channels.NotYetConnectedException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;


public class Client {
    private MulticastSocket multicastSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private final InitialFrame initialFrame;

    public Client(MulticastSocket multicastSocket, String host, int port) {
        try {
            this.socket = new Socket(host, port);
            this.multicastSocket = multicastSocket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            this.initialFrame = new InitialFrame(this);
            initialFrame.configFrame();

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            throw new NotYetConnectedException();
        }
    }

    public void sendMessage(String message) {
        new Thread(() -> {
            if (message != null) {
                try {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    private void receiveMessagesMulticast() {
        new Thread(() -> {
            while (true) {
                try {
                    byte[] buf = new byte[256];
                    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                    multicastSocket.receive(datagramPacket);

                    String received = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());

                    System.out.println("MULTICAST -> " + received);

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

        }).start();
    }

    public void receiveMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    String responseMessage = bufferedReader.readLine();

                    if (responseMessage == null)
                        closeEverything(socket, bufferedReader, bufferedWriter);

                    this.initialFrame.processResponse(responseMessage);

                    System.out.println(responseMessage);

                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) bufferedReader.close();

            if (bufferedWriter != null) bufferedWriter.close();

            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            MulticastSocket multicastSocket = new MulticastSocket(4446);
            Client client = new Client(multicastSocket, "localhost", 2048);

            client.receiveMessages();
            client.receiveMessagesMulticast();

        } catch (NotYetConnectedException | IOException e) {
            showMessageDialog(null, "Unable to connect to server!", "", ERROR_MESSAGE);
        }
    }
}
