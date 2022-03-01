package view;

import java.awt.*;
import java.io.File;

import javax.swing.*;

import controller.ClientController;

public class UserGUI extends JPanel {
    private ClientController controller;
    private JLabel usernameLbl, realNameLbl, statusLbl, picLabel;
	private JButton upload, logIn, addFriend;
	private JPanel mainPanel, rightPnl;
    private ImageIcon icon = null;
    private String picText;

	public static void main(String[] args) {
		//UserGUI lgui = new UserGUI();
	}

	private void createActionEvents() {
		/*getUpload().addActionListener(l -> { 
			controller.upload();
		});
	    getAddFriend().addActionListener(l -> {
			controller.addFriend();
		});*/
	}

	public UserGUI(ClientController controller) {
        this.controller = controller;
		createComponents();
		JFrame frame = new JFrame("Contact");
		frame.add(this);
		frame.setVisible(true);
		frame.pack();
		frame.setResizable(false);
		createActionEvents();
	}

	private void createComponents() {
		setPreferredSize(new Dimension(400, 250));
		setLayout(new BorderLayout());

		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(400, 250));
		mainPanel.setBackground(new Color(0, 0, 0));
		mainPanel.setForeground(new Color(50, 205, 50));

		rightPnl = new JPanel();
		rightPnl.setBackground(new Color(0, 0, 0));
		rightPnl.setPreferredSize(new Dimension(380, 230));

		usernameLbl = new JLabel("");
		usernameLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
		usernameLbl.setForeground(new Color(50, 205, 50));
		usernameLbl.setPreferredSize(new Dimension(370, 30));

        // picLabel = new JLabel(); 
		// java.awt.Image newimg = icon.getImage().getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);
		// icon = new ImageIcon(newimg);
		// picLabel.setIcon(icon);
        
        picLabel = new JLabel();

		realNameLbl = new JLabel("");
		realNameLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
		realNameLbl.setForeground(new Color(50, 205, 50));
		realNameLbl.setPreferredSize(new Dimension(370, 30));
		
		statusLbl = new JLabel("");
		statusLbl.setFont(new Font("Monospaced", Font.BOLD, 13));
		statusLbl.setForeground(new Color(50, 205, 50));
		statusLbl.setPreferredSize(new Dimension(370, 30));

		upload = new JButton("Upload Profile Picture");
		upload.setPreferredSize(new Dimension(370, 30));
		upload.setBackground(new Color(0, 0, 0));
		upload.setForeground(new Color(50, 205, 50));
		upload.setFont(new Font("Monospaced", Font.BOLD, 13));
		
		addFriend = new JButton("Add friend :)");
		addFriend.setPreferredSize(new Dimension(370, 30));
		addFriend.setBackground(new Color(0, 0, 0));
		addFriend.setForeground(new Color(50, 205, 50));
		addFriend.setFont(new Font("Monospaced", Font.BOLD, 13));

		rightPnl.add(picLabel);
		rightPnl.add(usernameLbl);
		rightPnl.add(realNameLbl);
		rightPnl.add(statusLbl);
		
		rightPnl.add(upload);
		rightPnl.add(addFriend);
		mainPanel.add(rightPnl);
		add(mainPanel);
	}

	public void setUsername(String text) {
		usernameLbl.setText("Username: " + text);
	}
	
	public void setName(String text) {
		realNameLbl.setText("Name: " + text);
	}
	
	public void setStatus(String text) {
		statusLbl.setText("Status: " + text);
	}
	
	public JButton getUpload() {
		return upload;
	}

	public JButton getAddFriend() {
		return addFriend;
	}

    public void setProfilePic(ImageIcon image) { 

        java.awt.Image newimg = image.getImage().getScaledInstance(40,40, java.awt.Image.SCALE_SMOOTH);
        
		icon = new ImageIcon(newimg);
		picLabel.setIcon(icon);
        //Image im = image.getImage();
        //this.icon.setImage(im);
    }
}
