package view; 

import javax.swing.*;
import controller.ClientController;
import model.pojo.User;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

public class ClientGUI extends JPanel implements KeyListener{

    private ClientController controller;
    private JLabel userLabel, contactsLabel;
    private JButton quit, send, pic, connect, disconnect, showProfile;
    private JPanel mainPanel, leftPnl, centerPnl, btnPnl;
    private JTextArea messageBox;
    private JTextPane chatBox;
    private ImageIcon icon = null;
    private JList<String> userBox, contactsBox;
    private JScrollPane chatPane, messagePane, userPane, contactsPane;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<User> friends = new ArrayList<User>();;

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

            if (userBox.getSelectedIndex() == -1) {
                controller.getProfile(
                        friends.get(contactsBox.getSelectedIndex()).getUserID());
            } else {
                controller.getProfile(
                        users.get(userBox.getSelectedIndex()).getUserID());
            }
        });
        getPic().addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            //Scaling the picure!!
            ImageIcon image = new ImageIcon(file.getAbsolutePath());
            java.awt.Image newimg = image.getImage().getScaledInstance(70,70, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newimg);

            try {
                controller.sendPic(getMessageBox().getText(),icon);
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println("Picture Error.");            
            }
            }
        });

    }

    public ClientGUI(ClientController clientController) {
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
        userPane.setPreferredSize(new Dimension(160, 200));

        showProfile = new JButton("Show Profile");
        showProfile.setPreferredSize(new Dimension(160, 30));
        showProfile.setBackground(new Color(0, 0, 0));
        showProfile.setForeground(new Color(50, 205, 50));
        showProfile.setFont(new Font("Monospaced", Font.BOLD, 13));

        contactsLabel = new JLabel("Contacts");
        contactsLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        contactsLabel.setForeground(new Color(50, 205, 50));

        contactsBox = new JList<String>();
        // userBox.setEditable(true);
        contactsBox.setBackground(new Color(0, 0, 0));
        contactsBox.setForeground(new Color(50, 205, 50));
        contactsBox.setFont(new Font("Monospaced", Font.BOLD, 13));

        contactsPane = new JScrollPane(contactsBox);
        contactsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contactsPane.setPreferredSize(new Dimension(160, 200));

        leftPnl.add(userLabel);
        leftPnl.add(userPane);
        leftPnl.add(showProfile);
        leftPnl.add(contactsLabel);
        leftPnl.add(contactsPane);

        ////////////////////////////////////////////////
        centerPnl = new JPanel();
        centerPnl.setBackground(new Color(0, 0, 0));
        centerPnl.setPreferredSize(new Dimension(550, 630));

        chatBox = new JTextPane();
        chatBox.setEditable(false);
        // chatBox.setLineWrap(true);
        // chatBox.setWrapStyleWord(true);
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

        messageBox.addKeyListener(this);

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

    public JTextPane getChatBox() {
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

    public JList<String> getContactBox() {
        return contactsBox;
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

    public void setContactBoxValue(ArrayList<User> friends) {

        this.friends = friends;

        String[] toView = new String[friends.size()];

        for (int i = 0; i < toView.length; i++) {

            toView[i] = friends.get(i).getName();

        }

        contactsBox.setListData(toView);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            controller.sendMessage(getMessageBox().getText()); 
            messageBox.setText(null); 
            messageBox.resetKeyboardActions();
            messageBox.setCursor(Cursor.getPredefinedCursor(-1));
        }        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    public ImageIcon chooseImage() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Upload a picture! (png/jpg)");

        ImageIcon icon;

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            ImageIcon image = new ImageIcon(file.getAbsolutePath());
            java.awt.Image newimg = image.getImage().getScaledInstance(90,90, java.awt.Image.SCALE_SMOOTH);        
            icon = new ImageIcon(newimg);
        } 
        else {
            icon = null;
        }

        return icon;
    }
}
