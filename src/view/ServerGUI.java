package view;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import controller.ServerController;
import model.pojo.Message;
import model.pojo.PackageType;
import model.pojo.TrafficPackage;

public class ServerGUI extends JPanel {
    private JLabel trafficLabel, specificTrafficLabel;
    private JButton show, reset;
    private JPanel mainPanel, rightPnl;
    private JTextArea trafficBox;
    private JScrollPane trafficPane;
    private JTextField trafficStart, trafficStop;
    private ServerController controller;

    public ServerGUI(ServerController controller) {
        createComponents();
        this.controller = controller;
        JFrame frame = new JFrame("Traffic Information");
        frame.add(this);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getReset().addActionListener(l -> {

            getTrafficBox().setText(null);

            getTrafficStart().setText("Tue, Mar 08 2021 22:26:03");
            getTrafficStop().setText("Tue, Mar 08 2023 22:26:03");

            getShow().doClick();

        });

        getShow().addActionListener(l -> {

            SimpleDateFormat format = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss"); 

            try {

                Date from = format.parse(getTrafficStart().getText());
                Date to = format.parse(getTrafficStop().getText());

                getTrafficBox().setText(null);

                for (TrafficPackage tp : controller.getEventsBetween(from, to)) {

                    StringBuilder string = new StringBuilder();

                    if (tp.getType() == PackageType.MESSAGE) {

                        Message message = (Message) tp.getEvent();

                        string.append(String.format("[%s]\n", new Date()));
                        string.append(tp.getUser().getName() + " has sent a message to:\n");

                        for (String id : message.getRecieverID()) {
                            string.append(">> " + controller.getUser(id).getName() + "\n");
                        }
                    }

                    if (tp.getType() == PackageType.SIGN_UP) {

                        string.append(String.format("[%s]\n", new Date()));
                        string.append(tp.getUser().getName() + " registered!\n");

                    }

                    if (tp.getType() == PackageType.CLIENT_CONNECT) {

                        string.append(String.format("[%s]\n", new Date()));
                        string.append(tp.getUser().getName() + " connected!\n");

                    }

                    if (tp.getType() == PackageType.CLIENT_DISCONNECT) {

                        string.append(String.format("[%s]\n", new Date()));
                        string.append(tp.getUser().getName() + " disconnected!\n");

                    }

                    getTrafficBox().append(string.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            

        });

    }

    private void createComponents() {
        setPreferredSize(new Dimension(500, 370));
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 370));
        mainPanel.setBackground(new Color(0, 0, 0));
        mainPanel.setForeground(new Color(50, 205, 50));

        rightPnl = new JPanel();
        rightPnl.setBackground(new Color(0, 0, 0));
        rightPnl.setPreferredSize(new Dimension(480, 380));

        trafficLabel = new JLabel("Traffic");
        trafficLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        trafficLabel.setForeground(new Color(50, 205, 50));

        trafficBox = new JTextArea();
        trafficBox.setEditable(false);
        // trafficBox.setLineWrap(true);
        // trafficBox.setWrapStyleWord(true);
        trafficBox.setBackground(new Color(0, 0, 0));
        trafficBox.setForeground(new Color(50, 205, 50));
        trafficBox.setFont(new Font("Monospaced", Font.BOLD, 13));

        trafficPane = new JScrollPane(trafficBox);
        trafficPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        trafficPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        trafficPane.setPreferredSize(new Dimension(480, 240));

        specificTrafficLabel = new JLabel("Specific traffic: ");
        specificTrafficLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        specificTrafficLabel.setForeground(new Color(50, 205, 50));

        trafficStart = new JTextField("Tue, Mar 08 2022 22:26:03");
        trafficStart.setPreferredSize(new Dimension(160, 30));
        trafficStart.setBackground(new Color(0, 0, 0));
        trafficStart.setForeground(new Color(50, 205, 50));
        trafficStart.setFont(new Font("Monospaced", Font.BOLD, 13));

        trafficStop = new JTextField("Tue, Mar 08 2022 22:26:03");
        trafficStop.setPreferredSize(new Dimension(160, 30));
        trafficStop.setBackground(new Color(0, 0, 0));
        trafficStop.setForeground(new Color(50, 205, 50));
        trafficStop.setFont(new Font("Monospaced", Font.BOLD, 13));

        show = new JButton("Show");
        show.setPreferredSize(new Dimension(160, 30));
        show.setBackground(new Color(0, 0, 0));
        show.setForeground(new Color(50, 205, 50));
        show.setFont(new Font("Monospaced", Font.BOLD, 13));

        reset = new JButton("Reset");
        reset.setPreferredSize(new Dimension(160, 30));
        reset.setBackground(new Color(0, 0, 0));
        reset.setForeground(new Color(50, 205, 50));
        reset.setFont(new Font("Monospaced", Font.BOLD, 13));

        rightPnl.add(trafficLabel);
        rightPnl.add(trafficPane);
        rightPnl.add(specificTrafficLabel);
        rightPnl.add(trafficStart);
        rightPnl.add(trafficStop);
        rightPnl.add(show);
        rightPnl.add(reset);
        mainPanel.add(rightPnl);
        add(mainPanel);
    }

    public void setTrafficBox(String msg) {
        this.trafficBox.setText(msg);
    }

    public JTextArea getTrafficBox() {
        return trafficBox;
    }

    public JButton getShow() {
        return show;
    }

    public JButton getReset() {
        return reset;
    }

    public JTextField getTrafficStart() {
        return trafficStart;
    }

    public JTextField getTrafficStop() {
        return trafficStop;
    }

}