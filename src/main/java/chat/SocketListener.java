package chat;

import javax.swing.*;
import java.net.Socket;

public class SocketListener implements NetworkListener {

    /*
    Listener wird im ServerThread verwendet
     */


    private JTextArea textArea;
    private String nick;
    private Socket socket;

    public SocketListener(JTextArea textArea, Socket socket, String nick) {
        this.textArea = textArea;
        this.nick = nick;
        this.socket = socket;
    }

    @Override
    public void messageReceived(String msg) {
        this.textArea.append(msg + "\n");
    }
}
