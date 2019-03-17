package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<String>();
    private Set<UserThread> userThreads = new HashSet<UserThread>();

    public ChatServer(int port)
    {
        this.port = port;
    }

    public void execute()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            ExecutorService pool = Executors.newFixedThreadPool(20);    //创建线程池

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected!");

                //create a UserThread for the socket
                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                //new Thread(newUser).start();
                pool.execute(newUser);

            }
        } catch (Exception ex) {

        }

    }

    public void broadcast(String message, UserThread excludeUser)
    {
        System.out.println("server receive message: " + message);
        for(UserThread user : userThreads)
        {
            if(message.contains("joined the chat"))
            {//broadcast to users except himself
                if(user != excludeUser)
                {
                    user.sendMessage(message);
                }
            }
            else    //broadcast to all users
                user.sendMessage(message);
        }
    }

    void addUserName(String userName) {
        userNames.add(userName);
    }

    void removeUser(UserThread user) {
        String name = user.userName;
        boolean removed = userNames.remove(name);
        if (removed) {
            userThreads.remove(user);
            System.out.println("The user " + name + " exit");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    public static void main(String[] args) throws Exception
    {
        ChatServer chatServer = new ChatServer(5555);
        chatServer.execute();
    }
}
