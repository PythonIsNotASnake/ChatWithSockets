package chat;

import server.ServerThread;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;

public class Chat extends JFrame {

    private static final long serialVersionUID = 1L;
    private InputListener inputListener;
    private NetworkListener netListener;
    private String nick;
    private Socket socket;
    private ServerThread serverListen;
    private JTextArea textArea;

    public Chat() {
        JPanel mainPanel;

        setTitle("Chat Tool v0.1");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        nick = retrieveNickName();

        try {
            this.socket = new Socket("localhost", 1337);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.serverListen = new ServerThread(this.socket, this.nick, this.textArea, this.netListener);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        mainPanel = setupChatView();
        Thread serverThread = new Thread(this.serverListen);
        getContentPane().add(mainPanel);
        getContentPane().getParent().invalidate();
        getContentPane().validate();
        serverThread.start();
    }

    private JPanel setupChatView() {
        JPanel panel = new JPanel();
        JPanel southPanel = new JPanel();
        JTextArea textArea = new JTextArea();
        final JTextField textField = new JTextField();
        JButton sendButton = new JButton("Send");

        textField.setColumns(60);

        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                inputListener.inputReceived(textField.getText());
                textField.setText("");
            }

        });

        this.getRootPane().setDefaultButton(sendButton);

        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setEditable(false);

        JScrollPane scrollPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setViewportView(textArea);
        scrollPanel.setVisible(true);

        southPanel.setLayout(new FlowLayout());
        southPanel.add(textField);
        southPanel.add(sendButton);

        panel.setLayout(new BorderLayout());
        //panel.add(textArea, BorderLayout.CENTER);
        panel.add(scrollPanel, BorderLayout.CENTER);
        panel.add(southPanel, BorderLayout.SOUTH);

        // this is just an example, please modify for your listeners accordingly...
        this.netListener = new SocketListener(this.textArea, this.socket, this.nick);
        this.inputListener = new KeyboardListener(this.textArea, this.nick, this.serverListen);
        this.serverListen.setNetListener(this.netListener);
        this.serverListen.setTextArea(this.textArea);

        return panel;
    }

    private String retrieveNickName() {
        return (String) JOptionPane.showInputDialog(this, "Enter your nickname please:", "Enter nickname", JOptionPane.QUESTION_MESSAGE);
    }

    public static void main(String[] args) {
        new Chat();
    }

}
