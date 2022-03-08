package view;

import java.awt.*;
import javax.swing.*;
import controller.ClientController;

public class UserGUI extends JPanel {
    private ClientController controller;
    private JLabel usernameLbl, realNameLbl, statusLbl, picLabel;
	private JButton addFriend;
	private JPanel mainPanel, rightPnl;
    private ImageIcon icon = null;
	private String username = null;

	public static void main(String[] args) {
		//UserGUI lgui = new UserGUI();
	}

	private void createActionEvents() {
	    getAddFriend().addActionListener(l -> {
			controller.addFriend(username);
		});
	}

	public UserGUI(ClientController controller) {
        this.controller = controller;
		createComponents();
		JFrame frame = new JFrame("Profile Info");
		frame.add(this);
		frame.setVisible(true);
		frame.pack();
		frame.setResizable(false);
		createActionEvents();
	}

	private void createComponents() {
		setPreferredSize(new Dimension(400, 220));
		setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(400, 220));
		mainPanel.setBackground(new Color(0, 0, 0));
		mainPanel.setForeground(new Color(50, 205, 50));

		rightPnl = new JPanel();
		rightPnl.setBackground(new Color(0, 0, 0));
		rightPnl.setPreferredSize(new Dimension(380, 200));

		usernameLbl = new JLabel("");
		usernameLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
		usernameLbl.setForeground(new Color(50, 205, 50));
		usernameLbl.setPreferredSize(new Dimension(370, 30));
        
        picLabel = new JLabel();

		realNameLbl = new JLabel("");
		realNameLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
		realNameLbl.setForeground(new Color(50, 205, 50));
		realNameLbl.setPreferredSize(new Dimension(370, 30));
		
		statusLbl = new JLabel("");
		statusLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
		statusLbl.setForeground(new Color(50, 205, 50));
		statusLbl.setPreferredSize(new Dimension(370, 30));
		
		addFriend = new JButton("Add friend :)");
		addFriend.setPreferredSize(new Dimension(370, 30));
		addFriend.setBackground(new Color(0, 0, 0));
		addFriend.setForeground(new Color(50, 205, 50));
		addFriend.setFont(new Font("Monospaced", Font.BOLD, 13));

		rightPnl.add(picLabel);
		rightPnl.add(usernameLbl);
		rightPnl.add(realNameLbl);
		rightPnl.add(statusLbl);
		
		rightPnl.add(addFriend);
		mainPanel.add(rightPnl);
		add(mainPanel);
	}

	public void setUsername(String text) {
		usernameLbl.setText("Username: " + text);
		username = text;
	}
	
	public void setName(String text) {
		realNameLbl.setText("Name: " + text);
	}
	
	public void setStatus(String text) {
		statusLbl.setText("Status: " + text);
	}

	public JButton getAddFriend() {
		return addFriend;
	}

    public void setProfilePic(ImageIcon image) { 
        java.awt.Image newimg = image.getImage().getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);        
		icon = new ImageIcon(newimg);
		picLabel.setIcon(icon);
    }
}
