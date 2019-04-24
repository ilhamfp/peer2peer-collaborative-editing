package org.sister.p2pcollaborative;


import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;

import java.io.*;
import java.net.*;
import java.util.*;

public class Messenger {

    private static Messenger messenger;
    private List<Client> clients = new ArrayList<>();
    private Server server;

    private String serverHost;
    private int serverPort;

    public Messenger(int port) {
        clients = new ArrayList<>();
        serverPort = port;

    }

    public static Messenger getInstance(int port) {
        if (messenger == null) {
            messenger = new Messenger(port);
        }
        return messenger;
    }

    public void start(){
        serverHost = getIP();
        System.out.println(serverHost);
        server = new Server(serverPort);
        server.start();

    }

    public void startClient(String signalHost, int signalPort){

        Client signalConnect = new Client(signalHost, signalPort);
        signalConnect.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            signalConnect.getOutputStream().writeUTF(serverHost + " " + String.valueOf(serverPort) + " "
                    + Controller.getInstance().getCrdt().getSiteId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(String str) {
        for (Messenger.Client client : clients) {
            try {
                client.getOutputStream().writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getIP() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (address.isLoopbackAddress()) {
            try {
                for (NetworkInterface networkInterface : Collections
                        .list(NetworkInterface.getNetworkInterfaces())) {
                    if (!networkInterface.isLoopback()) {
                        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                            InetAddress a = interfaceAddress.getAddress();
                            if (a instanceof Inet4Address) {
                                return a.getHostAddress();
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return address.getHostAddress();
    }


    private class Client extends Thread {
        private String address;
        private Integer port;
        private Socket socket;
        private DataOutputStream outputStream;

        public Socket getSocket() {
            return socket;
        }

        public DataOutputStream getOutputStream() {
            return outputStream;
        }


        public Client(String address, Integer port) {
            this.address = address;
            this.port = port;
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
                System.out.println("Server started at port " + String.valueOf(serverPort));

            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress().getHostAddress());
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
                        System.out.println("GET NEW MESSAGE: " + message);
                        Controller controller = Controller.getInstance();
                        String operation = message.substring(0,1);
                        if (operation.equals("S")) {
                            // message from signal
                            String[] splited = message.split("\\s+");
                            Client newConnection = new Client(splited[1], Integer.parseInt(splited[2]));

                            Controller.getInstance().getVersionVectors().put(UUID.fromString(splited[3]), new VersionVector(UUID.fromString(splited[3]), 0));
                            System.out.println(new Gson().toJson(Controller.getInstance().getVersionVectors()));
                            newConnection.start();
                            clients.add(newConnection);
                        } else {
                            Character character = new Gson().fromJson(message.substring(1), Character.class);
                            controller.onMessage(operation, character);
                        }

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
//        Messenger messenger = Messenger.getInstance();
//        messenger.startServer(8884);
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        messenger.startClient("127.0.0.1", 8884);
    }

}
