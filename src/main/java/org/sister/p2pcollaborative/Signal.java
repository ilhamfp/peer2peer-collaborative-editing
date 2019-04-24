package org.sister.p2pcollaborative;


import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Signal {

    private static Signal signal;
    private List<Client> connectedClient = new ArrayList<>();
    private Signal() {
        connectedClient = new ArrayList<>();
    }

    public static Signal getInstance() {
        if (signal == null) {
            signal = new Signal();
        }
        return signal;
    }

    public void startSignal(Integer port) {
        new Server(port).start();
    }

    private class Client extends Thread {
        private String address;
        private Integer port;
        private Socket socket;
        private DataOutputStream outputStream;
        private UUID siteId;

        public UUID getSiteId() {
            return siteId;
        }

        public Socket getSocket() {
            return socket;
        }

        public DataOutputStream getOutputStream() {
            return outputStream;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Client(String address, Integer port, UUID siteId) {
            this.address = address;
            this.port = port;
            this.siteId = siteId;
        }

        @Override
        public void run() {
            super.run();
            try {
                socket = new Socket(address, port);
                outputStream = new DataOutputStream(socket.getOutputStream());
                System.out.println("Connected to " + address + ":" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class Server extends Thread {
        private Integer port;
        private ServerSocket serverSocket;

        public Server(Integer port) {
            this.port = port;
        }

        @Override
        public void run() {
            super.run();
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Signal started at port " + String.valueOf(port));

            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("NEW CONNECTION: " + socket.getInetAddress().getHostAddress() + ":" + String.valueOf(socket.getPort()));
                    new ClientHandler(socket).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ClientHandler extends Thread {

        private Socket socket;
        private DataInputStream inputStream;
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
//            Messenger.getInstance().startClient(socket.getInetAddress().getHostAddress(), 8888);
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                String message;
                while (true) {
                    try {
                        message = inputStream.readUTF();
                    } catch (EOFException e) {
                        message = null;
                    }
                    if (message != null) {
                        System.out.println("SERVER OF NEW CONENCTION: " + message);
                        String[] splited = message.split("\\s+");
                        Client newConnection = new Client(splited[0], Integer.parseInt(splited[1]), UUID.fromString(splited[2]));
                        newConnection.start();

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (connectedClient.size() != 0) {
                            // send new connection info to all connected client
                            for (Client client : connectedClient) {
                                try {
                                    client.getOutputStream().writeUTF("S " + splited[0] + " " + Integer.parseInt(splited[1]) + " " + client.getSiteId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            // send connected client info to new connection
                            for (Client client : connectedClient) {
                                try {
                                    newConnection.getOutputStream().writeUTF("S " + client.getAddress() + " "
                                            + client.getPort() + " " + client.getSiteId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        connectedClient.add(newConnection);

                    }
                    else {
//                        System.out.println("WAITING FOR MESSAGE");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Signal signal = Signal.getInstance();
        signal.startSignal(8885);
    }

}