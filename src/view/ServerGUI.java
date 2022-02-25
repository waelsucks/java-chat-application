package view;

import java.awt.*;
import javax.swing.*;

public class ServerGUI extends JPanel{
    private JLabel trafficLabel, specificTrafficLabel;
    private JButton quit, connect, show, reset;
    private JPanel mainPanel, rightPnl;
    private JTextArea trafficBox;
    private JScrollPane trafficPane;
    private JTextField trafficStart, trafficStop;
        
        public static void main(String[] args) {
            ServerGUI sg = new ServerGUI();
        }
        public ServerGUI () {
            createComponents();
            JFrame frame = new JFrame("Chatty chatti chat away...");
            frame.add(this);
            frame.setVisible(true);
            frame.pack();
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private void createComponents() {
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
            trafficPane.setPreferredSize(new Dimension(160, 230));

            connect = new JButton("Connect");
            connect.setPreferredSize(new Dimension(160, 30));
            connect.setBackground(new Color(0, 0, 0));
            connect.setForeground(new Color(50, 205, 50));
            connect.setFont(new Font("Monospaced", Font.BOLD, 13));

            quit = new JButton("Disconnect");
            quit.setPreferredSize(new Dimension(160, 30));
            quit.setBackground(new Color(0, 0, 0));
            quit.setForeground(new Color(50, 205, 50));
            quit.setFont(new Font("Monospaced", Font.BOLD, 13));

            String msg= String.format("\n %s", "Specific traffic");
            specificTrafficLabel = new JLabel(msg);
            specificTrafficLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
            specificTrafficLabel.setForeground(new Color(50, 205, 50));

            trafficStart = new JTextField("22.02.22 22:22:00");
            trafficStart.setPreferredSize(new Dimension (160, 30));
            trafficStart.setBackground(new Color(0, 0, 0));
            trafficStart.setForeground(new Color(50, 205, 50));
            trafficStart.setFont(new Font("Monospaced", Font.BOLD, 13));

            trafficStop = new JTextField("22.02.22 23:23:00");
            trafficStop.setPreferredSize(new Dimension (160, 30));
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
            rightPnl.add(connect);
            rightPnl.add(quit);
            rightPnl.add(specificTrafficLabel);
            rightPnl.add(trafficStart);
            rightPnl.add(trafficStop);
            rightPnl.add(show);
            rightPnl.add(reset);


            mainPanel.add(rightPnl);
            add(mainPanel);
		}
}