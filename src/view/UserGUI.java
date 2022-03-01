package view;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import controller.ClientController;

public class UserGUI extends JPanel {
    private JLabel usernameLbl, picLbl;
    private JButton upload, logIn;
    private JPanel mainPanel, rightPnl;
    private JTextField username;
    private ClientController controller;

    // public static void main(String[] args) {
    // LoginGUI lgui = new LoginGUI();
    // }

    public UserGUI(ClientController controller) {
        createComponents();
        JFrame frame = new JFrame("Welcome New Person...");
        frame.add(this);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.controller = controller;
        addActionListeners();
    }

    private void addActionListeners() {

		getUpload().addActionListener(l -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                controller.upload(file);
                
            }

        });

	}

    private void createComponents() {
        setPreferredSize(new Dimension(400, 200));
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(400, 200));
        mainPanel.setBackground(new Color(0, 0, 0));
        mainPanel.setForeground(new Color(50, 205, 50));

        rightPnl = new JPanel();
        rightPnl.setBackground(new Color(0, 0, 0));
        rightPnl.setPreferredSize(new Dimension(380, 180));

        usernameLbl = new JLabel("Enter your name: ");
        usernameLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
        usernameLbl.setForeground(new Color(50, 205, 50));

        username = new JTextField();
        username.setEditable(true);
        username.setBackground(new Color(230, 230, 250));
        username.setFont(new Font("Monospaced", Font.BOLD, 13));
        username.setPreferredSize(new Dimension(380, 30));

        picLbl = new JLabel("Choose your profile picture");
        picLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
        picLbl.setForeground(new Color(50, 205, 50));

        upload = new JButton("Upload");
        upload.setPreferredSize(new Dimension(380, 30));
        upload.setBackground(new Color(0, 0, 0));
        upload.setForeground(new Color(50, 205, 50));
        upload.setFont(new Font("Monospaced", Font.BOLD, 13));

        logIn = new JButton("Log In");
        logIn.setPreferredSize(new Dimension(380, 30));
        logIn.setBackground(new Color(0, 0, 0));
        logIn.setForeground(new Color(50, 205, 50));
        logIn.setFont(new Font("Monospaced", Font.BOLD, 13));

        rightPnl.add(usernameLbl);
        rightPnl.add(username);
        rightPnl.add(picLbl);
        rightPnl.add(upload);
        rightPnl.add(logIn);
        mainPanel.add(rightPnl);
        add(mainPanel);
    }

    public JButton getUpload() {
        return upload;
    }

    public JButton getLogin() {
        return logIn;
    }

}
