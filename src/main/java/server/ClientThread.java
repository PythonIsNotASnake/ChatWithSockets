package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;

public class ClientThread implements Runnable {

    private Socket socket;
    private PrintWriter clientOut;
    private Server chatServer;

    public ClientThread(Server chatServer, Socket socket) {
        this.chatServer = chatServer;
        this.socket = socket;
    }

    public PrintWriter getClientOut() {
        return clientOut;
    }

    @Override
    public void run() {
        try {
            this.clientOut = new PrintWriter(this.socket.getOutputStream(), false);
            Scanner in = new Scanner(this.socket.getInputStream());

            while (true) {
                do {
                    if (this.socket.isClosed()) {
                        return;
                    }
                } while (!in.hasNextLine());

                String input = in.nextLine();
                Iterator iterator = this.chatServer.getClients().iterator();

                while (iterator.hasNext()) {
                    ClientThread thatClient = (ClientThread) iterator.next();
                    PrintWriter thatClientOut = thatClient.getClientOut();
                    if (thatClientOut != null) {
                        thatClientOut.write(input + "\r\n");
                        thatClientOut.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
