package view; //haii

import javax.swing.*;

import controller.ClientController;
import model.pojo.Message;
import model.pojo.User;
import model.pojo.UserList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainPanel extends JPanel {

    private ClientController controller;
    private JLabel userLabel;
    private JButton quit, send, pic, connect, disconnect, showProfile;
    private JPanel mainPanel, leftPnl, centerPnl, btnPnl;
    private JTextArea chatBox, messageBox;
    private JList<String> userBox;
    private JScrollPane chatPane, messagePane, userPane;
    private ArrayList<User> users = new ArrayList<User>();

    // public static void main(String[] args) {
    // new MainPanel();
    // }

    public void createActionEvents() {
        getSend().addActionListener(l -> {
            controller.sendMessage(getMessageBox().getText());
        });
        getConnect().addActionListener(l -> {
            controller.connect();
            getConnect().setEnabled(false);
            getDisconnect().setEnabled(true);
            getSend().setEnabled(true);
            getShowProfile().setEnabled(true);
            getPic().setEnabled(true);
        });
        getDisconnect().addActionListener(e -> {
            controller.disconnect();
            getDisconnect().setEnabled(false);
            getConnect().setEnabled(true);
            getSend().setEnabled(false);
            getShowProfile().setEnabled(false);
            getPic().setEnabled(false);

        });
        getShowProfile().addActionListener(e -> {
            controller.getProfile(
                users.get(userBox.getSelectedIndex()).getUserID()
            );
        });
    }

    public MainPanel(ClientController clientController) {
        createComponents();
        this.controller = clientController;
        JFrame frame = new JFrame("Bit by Bit");
        frame.add(this);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getConnect().setEnabled(false);
        createActionEvents();
    }

    public void createComponents() {
        setPreferredSize(new Dimension(750, 550));
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(750, 550));
        mainPanel.setBackground(new Color(0, 0, 0));
        mainPanel.setForeground(new Color(50, 205, 50));

        ////////////////////////
        leftPnl = new JPanel();
        leftPnl.setBackground(new Color(0, 0, 0));
        leftPnl.setPreferredSize(new Dimension(180, 630));

        userLabel = new JLabel("Online Users");
        userLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        userLabel.setForeground(new Color(50, 205, 50));

        userBox = new JList<String>();
        // userBox.setEditable(true);
        userBox.setBackground(new Color(0, 0, 0));
        userBox.setForeground(new Color(50, 205, 50));
        userBox.setFont(new Font("Monospaced", Font.BOLD, 13));

        userPane = new JScrollPane(userBox);
        userPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        userPane.setPreferredSize(new Dimension(160, 460));

        showProfile = new JButton("Show Profile");
        showProfile.setPreferredSize(new Dimension(160, 30));
        showProfile.setBackground(new Color(0, 0, 0));
        showProfile.setForeground(new Color(50, 205, 50));
        showProfile.setFont(new Font("Monospaced", Font.BOLD, 13));

        leftPnl.add(userLabel);
        leftPnl.add(userPane);
        leftPnl.add(showProfile);

        ////////////////////////////////////////////////
        centerPnl = new JPanel();
        centerPnl.setBackground(new Color(0, 0, 0));
        centerPnl.setPreferredSize(new Dimension(550, 630));

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setLineWrap(true);
        chatBox.setWrapStyleWord(true);
        chatPane = new JScrollPane(chatBox);
        chatPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatPane.setPreferredSize(new Dimension(500, 430));

        chatBox.setBackground(new Color(0, 0, 0));
        chatBox.setForeground(new Color(50, 205, 50));
        chatBox.setFont(new Font("Monospaced", Font.BOLD, 13));

        messageBox = new JTextArea();
        messageBox.setEditable(true);
        messageBox.setFont(new Font("Arial", Font.PLAIN, 12));
        messageBox.setBackground(new Color(230, 230, 250));
        messagePane = new JScrollPane(messageBox);
        messagePane.setPreferredSize(new Dimension(500, 50));

        send = new JButton("Send");
        send.setPreferredSize(new Dimension(120, 30));
        send.setBackground(new Color(0, 0, 0));
        send.setForeground(new Color(50, 205, 50));
        send.setFont(new Font("Monospaced", Font.BOLD, 13));

        connect = new JButton("Connect");
        connect.setPreferredSize(new Dimension(120, 30));
        connect.setBackground(new Color(0, 0, 0));
        connect.setForeground(new Color(50, 205, 50));
        connect.setFont(new Font("Monospaced", Font.BOLD, 13));

        disconnect = new JButton("Disconnect");
        disconnect.setPreferredSize(new Dimension(120, 30));
        disconnect.setBackground(new Color(0, 0, 0));
        disconnect.setForeground(new Color(50, 205, 50));
        disconnect.setFont(new Font("Monospaced", Font.BOLD, 13));

        pic = new JButton("Send Pic");
        pic.setPreferredSize(new Dimension(120, 30));
        pic.setBackground(new Color(0, 0, 0));
        pic.setForeground(new Color(50, 205, 50));
        pic.setFont(new Font("Monospaced", Font.BOLD, 13));

        btnPnl = new JPanel();
        btnPnl.setBackground(new Color(0, 0, 0));
        btnPnl.setPreferredSize(new Dimension(500, 40));

        btnPnl.add(send);
        btnPnl.add(connect);
        btnPnl.add(disconnect);
        btnPnl.add(pic);

        centerPnl.add(chatPane);
        centerPnl.add(messagePane);
        centerPnl.add(btnPnl);

        ////////////////////////////////////////////////
        mainPanel.add(leftPnl);
        mainPanel.add(centerPnl);
        add(mainPanel);

    }

    /////////////////////////////////////////
    public void setChatBox(String chatBox) {
        this.chatBox.setText(chatBox);
    }

    public JTextArea getChatBox() {
        return chatBox;
    }

    public void setMessageBoxValue(String text) {
        this.messageBox.setText(text);
    }

    public JTextArea getMessageBox() {
        return messageBox;
    }

    public JList<String> getUserBox() {
        return userBox;
    }

    public void setUserBoxValue(ArrayList<User> usersparam) {

        users = usersparam;

        String[] toView = new String[users.size()];

        for (int i = 0; i < toView.length; i++) {

            toView[i] = users.get(i).getName();

        }

        userBox.setListData(toView);
        
    }

    ///////////// GS for buttons/////////////
    public JButton getSend() {
        return send;
    }

    public JButton getQuit() {
        return quit;
    }

    public JButton getConnect() {
        return connect;
    }

    public JButton getShowProfile() {
        return showProfile;
    }

    public JButton getDisconnect() {
        return disconnect;
    }

    public JButton getPic() { 
        return pic; 
    }
}
