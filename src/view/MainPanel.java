package view;
import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private JLabel userLabel, trafficLabel;
    private JButton quit, send, pic, connect, addContact;
    private JPanel mainPanel, leftPnl, centerPnl, btnPnl, rightPnl;
    private JTextArea chatBox, messageBox, userBox, trafficBox;
    private JScrollPane chatPane, messagePane, userPane, trafficPane;
    public static void main(String[] args) {
        new MainPanel();    
    }

    public MainPanel() { 
        createComponents();
        JFrame frame = new JFrame("Chatty chatti chat away...");
        frame.add(this);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void clearFields() {
        setMessageBox(null);
    }

	public void createComponents() {
        setPreferredSize(new Dimension(930, 650));
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(930, 650));
        mainPanel.setBackground(new Color(0, 0, 0));
		mainPanel.setForeground(new Color(50, 205, 50));

        ////////////////////////
        leftPnl = new JPanel();
        leftPnl.setBackground(new Color(0, 0, 0));
        leftPnl.setPreferredSize(new Dimension(180, 630));
        
        userLabel = new JLabel("User Info");
        userLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        userLabel.setForeground(new Color(50, 205, 50));

        userBox = new JTextArea("Anna[Online] Malin[Online] Adam[Offline] Wael[Offline]");
        userBox.setEditable(false);
		userBox.setLineWrap(true);
		userBox.setWrapStyleWord(true);
        userBox.setBackground(new Color(0, 0, 0));
		userBox.setForeground(new Color(50, 205, 50));
		userBox.setFont(new Font("Monospaced", Font.BOLD, 13));

        userPane = new JScrollPane(userBox);
        userPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        userPane.setPreferredSize(new Dimension(160, 430));

        addContact = new JButton("Add to contacts");
        addContact.setPreferredSize(new Dimension(160, 30));
        addContact.setBackground(new Color(0, 0, 0));
		addContact.setForeground(new Color(50, 205, 50));
		addContact.setFont(new Font("Monospaced", Font.BOLD, 13));
 
        leftPnl.add(userLabel);
        leftPnl.add(userPane);
        leftPnl.add(addContact);

        ////////////////////////////////////////////////
        centerPnl = new JPanel();
        centerPnl.setBackground(new Color(0, 0, 0));
        centerPnl.setPreferredSize(new Dimension(550, 630));

        chatBox = new JTextArea("Testing.... Testing.... Testing.... Testing.... Testing.... Testing.... Testing.... ");
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
        send.setPreferredSize(new Dimension(150, 30));
        send.setBackground(new Color(0, 0, 0));
		send.setForeground(new Color(50, 205, 50));
		send.setFont(new Font("Monospaced", Font.BOLD, 13));

        pic = new JButton("Upload Pic");
        pic.setPreferredSize(new Dimension(150, 30));
        pic.setBackground(new Color(0, 0, 0));
		pic.setForeground(new Color(50, 205, 50));
		pic.setFont(new Font("Monospaced", Font.BOLD, 13));

        btnPnl = new JPanel();
        btnPnl.setBackground(new Color(0, 0, 0));
        btnPnl.setPreferredSize(new Dimension(500, 40));

        btnPnl.add(send);
        btnPnl.add(pic);
    
        centerPnl.add(chatPane);
        centerPnl.add(messagePane);
        centerPnl.add(btnPnl);

        ////////////////////////////////////////////////
        rightPnl = new JPanel();
        rightPnl.setBackground(new Color(0, 0, 0));
        rightPnl.setPreferredSize(new Dimension(180, 630));
        
        trafficLabel = new JLabel("Traffic");
        trafficLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        trafficLabel.setForeground(new Color(50, 205, 50));

        trafficBox = new JTextArea("220223.13:37.Anna[Logged in]");
        trafficBox.setEditable(false);
		//trafficBox.setLineWrap(true);
		//trafficBox.setWrapStyleWord(true);
        trafficBox.setBackground(new Color(0, 0, 0));
		trafficBox.setForeground(new Color(50, 205, 50));
		trafficBox.setFont(new Font("Monospaced", Font.BOLD, 13));

        trafficPane = new JScrollPane(trafficBox);
        trafficPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        trafficPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        trafficPane.setPreferredSize(new Dimension(160, 430));

        connect = new JButton("Connect");
        connect.setPreferredSize(new Dimension(150, 30));
        connect.setBackground(new Color(0, 0, 0));
		connect.setForeground(new Color(50, 205, 50));
		connect.setFont(new Font("Monospaced", Font.BOLD, 13));

        quit = new JButton("Disconnect");
        quit.setPreferredSize(new Dimension(150, 30));
        quit.setBackground(new Color(0, 0, 0));
		quit.setForeground(new Color(50, 205, 50));
		quit.setFont(new Font("Monospaced", Font.BOLD, 13));

        rightPnl.add(trafficLabel);
        rightPnl.add(trafficPane);
        rightPnl.add(connect);
        rightPnl.add(quit);


        ////////////////////////////////////////////////
        mainPanel.add(leftPnl);
        mainPanel.add(centerPnl);
        mainPanel.add(rightPnl);
        add(mainPanel);
        
    }

	/////////////////////////////////////////
    public void setChatBox(String chatBox) {
        this.chatBox.setText(chatBox);
    }
    public String getChatBox() {
        return chatBox.getText();
    }
    public void setMessageBox(String messageBox) {
        this.messageBox.setText(messageBox);
    }
    public String getMessageBox() {
        return messageBox.getText();
    }
    
    ///////////// GS for buttons/////////////
    public JButton getSend() {
        return send;
    }
    public JButton getQuit() {
        return quit;
    }

}
