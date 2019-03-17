package server;

import java.io.*;
import java.net.*;

public class UserThread implements Runnable{
    String userName = "";
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server)
    {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run()
    {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            userName = reader.readLine();    //read username
            server.addUserName(userName);   //add new user to set userNames

            printUsers();   //print all connected users
            String serverMessage;
            if(!server.getUserNames().isEmpty())
            {
                serverMessage = "[ " + userName + " ]" + " joined the chat";
                server.broadcast(serverMessage, this);  //broadcast new user info
            }

            while(true) {
                String clientMessage = reader.readLine();
                if(clientMessage.equals("exit"))
                {   //user exit
                    server.removeUser(this);
                    socket.close();
                    serverMessage = userName + " exit";
                    server.broadcast(serverMessage, this);
                    break;
                }
                serverMessage = "[ " + userName + " ]: " + clientMessage;
                server.broadcast(serverMessage, this);  //send user message to others
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    void sendMessage(String message) {
        writer.println(message);
    }

    void printUsers() {
        writer.println(">>> Online users: " + server.getUserNames());
    }

}
