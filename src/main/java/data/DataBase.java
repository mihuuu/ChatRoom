package data;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataBase {
    private static final String url="jdbc:mysql://localhost:3306/chat_room?verifyServerCertificate=false&useSSL=true&serverTimezone=GMT";//test为我的数据库的名字
    private static final String user="root";//用户名
    private static final String password="huhandan";//密码
    private Connection con;

    public DataBase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,user,password);//连接数据库
            if(!con.isClosed())
                System.out.println("Success connecting to the Database!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        //DataBase myDB = new DataBase();
    }

    public boolean userRegister(String userName, String password) {
        try {
            Statement statement = con.createStatement();
            String query = "INSERT INTO user_info VALUES('" + userName + "', '" + password + "')";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean userLogin(String userName, String password) {
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM `user_info` WHERE `user_name` = ? AND `password` = ?");
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if(rs.isBeforeFirst())  // find result
            {
                System.out.println(userName + " login success!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String[] getAllUsers()
    {
        ArrayList<String> users = new ArrayList<String>();
        try {
            Statement statement = con.createStatement();
            String query = "SELECT * FROM `user_info`";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                String name = rs.getString("user_name");
                users.add(name);
                System.out.println("user: " + name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(users);
        int size = users.size();
        String[] arr = (String[])users.toArray(new String[size]);
        return arr;
    }

}
