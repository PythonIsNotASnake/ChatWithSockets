package server;

import chat.NetworkListener;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ServerThread implements Runnable {

    private Socket socket;
    private String userName;
    private boolean isAlived;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;
    private NetworkListener netListener;
    private JTextArea textArea;
    private InputStreamReader input;
    private OutputStream output;

    public ServerThread(Socket socket, String userName, JTextArea textArea, NetworkListener netListener) throws IOException {
        this.socket = socket;
        this.userName = userName;
        this.messagesToSend = new LinkedList();
        this.textArea = textArea;
        this.netListener = netListener;
        this.input = new InputStreamReader(this.socket.getInputStream());
        this.output = this.socket.getOutputStream();
    }

    public void addNextMessage(String message) {
        PrintWriter writer = new PrintWriter(this.output);
        writer.println(message);
        writer.flush();
    }

    public void receiveMessage(String msg) {
        synchronized (this.messagesToSend) {
            this.hasMessages = true;
            this.netListener.messageReceived(msg);
        }
    }

    @Override
    public void run() {
        System.out.println("Welcome: " + this.userName);
        System.out.println("Local Port: " + this.socket.getLocalPort());
        System.out.println("Server = " + this.socket.getRemoteSocketAddress() + ":" + this.socket.getPort());

        while (!this.socket.isClosed()) {
            try {
                if (this.input.ready()) {
                    BufferedReader reader = new BufferedReader(this.input);
                    this.receiveMessage(reader.readLine());
                }
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public void setNetListener(NetworkListener netListener) {
        this.netListener = netListener;
    }
}
