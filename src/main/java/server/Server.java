package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private int port;
    private List<ClientThread> clients;

    public Server(int port) {
        this.port = port;
    }

    public List<ClientThread> getClients() {
        return clients;
    }

    // Initialisiert den Server
    public void startServer() {
        this.clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(this.port);
            this.acceptClients(serverSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Wartet auf eingehende Verbindungsanfragen und wandelt jede in ClientThread um
    private void acceptClients(ServerSocket serverSocket) {
        System.out.println("Server starts on port: " + serverSocket.getLocalSocketAddress());

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Accepts: " + socket.getRemoteSocketAddress());

                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();

                this.clients.add(client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Server chatServer = new Server(1337);
        chatServer.startServer();
    }
}
