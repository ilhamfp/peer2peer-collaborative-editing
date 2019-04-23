package org.sister.p2pcollaborative;


import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Messenger {

    private static Messenger messenger;
    private Messenger() {

    }

    public static Messenger getInstance() {
        if (messenger == null) {
            messenger = new Messenger();
        }
        return messenger;
    }


    public void startClient(String address, Integer port) {
        new Client(address, port).start();
    }

    public void startServer(Integer port) {
        new Server(port).start();
    }

    static class Client extends Thread {
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

    static class Server extends Thread {
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
                System.out.println("Server started");

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

    static class ClientHandler extends Thread {

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
                        System.out.println(message);
                        Controller controller = Controller.getInstance();
                        String operation = message.substring(0,1);
                        Character character = new Gson().fromJson(message.substring(1), Character.class);
                        controller.onMessage(operation, character);
                    }
                    else {
                        System.out.println("WAITING FOR MESSAGE");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Messenger messenger = Messenger.getInstance();
        messenger.startServer(8884);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messenger.startClient("127.0.0.1", 8884);
    }

}
