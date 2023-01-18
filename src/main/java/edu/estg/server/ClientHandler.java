package edu.estg.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
   protected Socket socket;
   protected BufferedReader bufferedReader;
   protected BufferedWriter bufferedWriter;
   protected Protocol protocol;
   protected ArrayListSync<ClientHandler> clientHandlers;
   protected Server server;
   protected String username;

   public ClientHandler(ArrayListSync<ClientHandler> clientHandlers, Socket socket, Server server) {
       try {
           this.socket = socket;
           this.server = server;
           this.clientHandlers = clientHandlers;
           this.protocol = new Protocol(this, clientHandlers, server);
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.clientHandlers.add(this);
       } catch (IOException e) {
           closeEverything(socket, bufferedReader, bufferedWriter);
       }
   }

   @Override
    public void run() {
       receivedMessages();
   }

    private void receivedMessages() {
        new Thread(() -> {
            while (socket.isConnected() && !socket.isClosed()) {
                try {
                    String inputMessage = bufferedReader.readLine();

                    if (inputMessage == null) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        return;
                    }
                    System.out.println("REQUEST -> " + inputMessage);

                    String responseMessage = this.protocol.processMessage(inputMessage);

                    System.out.println("RESPONSE -> " + responseMessage);

                    sendMessage(responseMessage);
                } catch (IOException | NullPointerException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
            }
        }).start();
    }

    public void sendMessage(String messageToSend) {
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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
}
