package client;

import data.DataBase;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatRoom {
    private JPanel myPanel;
    private JList userList;
    private JTextArea msgArea;
    private JTextArea inputArea;
    private JButton sendButton;
    private JButton exitButton;
    private JPanel msgPanel;
    private JPanel inputPanel;
    private JLabel myInfo;

    String userName;
    Socket client;
    PrintWriter writer;
    BufferedReader reader;
    private int port;
    private String serverName;

    public ChatRoom(String userName, String serverName) {
        this.userName = userName;
        this.serverName = serverName;
    }

    public void execute() {
        try {
            client = new Socket(serverName, 5555);
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream(), true);
            writer.println(userName);   //send user name to server
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        ChatRoom.MessageThread messageThread = new ChatRoom.MessageThread();
        messageThread.start();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                buildInterface();
            }
        });
    }

    public void buildInterface() {
        JFrame mainFrame = new JFrame("ZJU Chat Space");
        mainFrame.add(myPanel);
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

//        myInfo.setText("Hello, " + userName);
        sendButton.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.green));

        DataBase myDB = new DataBase();
        userList.setListData(myDB.getAllUsers());
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputArea.getText();
                writer.println(message);    //send message
                inputArea.setText("");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                writer.println("exit");
                System.exit(0);
            }
        });

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    class MessageThread extends Thread {
        public void run()
        {
            String line;
            while(true)
            {
                try {
                    //read message from socket stream
                    line = reader.readLine();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    msgArea.append(line + "   " + dtf.format(now) + '\n');
                    msgArea.append("-------------------------------------------------------" + "\n");

                } catch(Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args)
    {
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch(Exception e) {
            //TODO exception
        }

//        String userName = JOptionPane.showInputDialog(null, "Enter your name: ",
//                "userName", JOptionPane.PLAIN_MESSAGE);

        ChatRoom chatRoom = new ChatRoom("hedy", "localhost");
        chatRoom.execute();
    }
}
