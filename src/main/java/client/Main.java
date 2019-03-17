package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args)
    {
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch(Exception e) {
            e.printStackTrace();
        }

        FirstDialog login = new FirstDialog();
        login.init();
    }

}

class FirstDialog extends JDialog {
    public String userName;
    public boolean isLogin = false;

    public FirstDialog()
    {
    }

    public void init(){
        final JFrame frame = new JFrame("ZJU Chat Space");
        final JButton btnLogin = new JButton("Login");
        final JButton btnReg = new JButton("Register");

        btnLogin.addActionListener(
                e -> {
                    LoginDialog loginDlg = new LoginDialog(frame);
                    loginDlg.setVisible(true);
                    // if login successfully
                    if(loginDlg.isSucceeded()){
                        userName = loginDlg.getUsername();
                        isLogin = true;
                        frame.setVisible(false);
                        ChatRoom chatRoom = new ChatRoom(userName, "localhost");
                        chatRoom.execute();
                    }
                });

        btnReg.addActionListener(
                e -> {
                    RegDialog regDlg = new RegDialog(frame);
                    regDlg.setVisible(true);
                });

        frame.setSize(300, 200);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.getContentPane().add(btnLogin, gbc);
        frame.getContentPane().add(btnReg, gbc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
