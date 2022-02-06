package chat;

import server.ServerThread;

import javax.swing.JTextArea;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class KeyboardListener implements InputListener {

    private JTextArea textArea;
    private String nick;
    private ServerThread serverListen;
    private final LinkedList<String> messagesToSend;

    public KeyboardListener(JTextArea textArea, String nick, ServerThread serverListen) {
        this.textArea = textArea;
        this.nick = nick;
        this.serverListen = serverListen;
        this.messagesToSend = new LinkedList();
    }

    @Override
    public void inputReceived(String str) {
        this.serverListen.addNextMessage("<" + this.nick + "> " + str);
		/*
		textArea.append("<" + nick + "> " + str + System.getProperty("line.separator"));

		try {
			Socket socket = new Socket("localhost", 1337);
			socket.getOutputStream().write('t');
			socket.getOutputStream().flush();
			OutputStream outputStream = socket.getOutputStream();
			PrintStream printStream = new PrintStream(outputStream, true);
			printStream.println("Nachricht raus!");
			System.out.println("Nachricht!!!");
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(str);
		}catch (Exception e){
			e.printStackTrace();
		}*/
    }
}
