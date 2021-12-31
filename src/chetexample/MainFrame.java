package chetexample;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.Color;

public class MainFrame extends JFrame {

	private JPanel mainPanel;
	private JPanel contentPane;
	private JLabel programName;
	private JButton serverButton, clientButton;
	private JLabel ipAddress;
	private JTextField ipField;
	private JLabel portNum;
	private JTextField portField;
	private JTextField cNameField;
	private JLabel clientName;

	public MainFrame() {
		/* 디자인 */
		setBackground(Color.WHITE);

		setTitle("java talk");
		setBounds(0, 0, 349, 537);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(240, 248, 255));
		mainPanel.setBounds(0, 0, 349, 509);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);

		programName = new JLabel("JAVA Talk");
		programName.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		programName.setBounds(78, 70, 225, 129);
		mainPanel.add(programName);

		serverButton = new JButton("서버 들어가기");
		serverButton.setBounds(97, 347, 147, 50);
		mainPanel.add(serverButton);

		JButton clientButton = new JButton("클라이언트 들어가기");
		clientButton.setBounds(97, 415, 147, 50);
		mainPanel.add(clientButton);
		
		ipAddress = new JLabel("아이피 주소");
		ipAddress.setBounds(138, 192, 61, 16);
		mainPanel.add(ipAddress);
		
		ipField = new JTextField();
		ipField.setBounds(97, 208, 147, 26);
		mainPanel.add(ipField);
		ipField.setColumns(10);
		
		portNum = new JLabel("포트 번호");
		portNum.setHorizontalAlignment(SwingConstants.CENTER);
		portNum.setBounds(138, 240, 61, 16);
		mainPanel.add(portNum);
		
		portField = new JTextField();
		portField.setBounds(97, 258, 147, 26);
		mainPanel.add(portField);
		portField.setColumns(10);
		
		cNameField = new JTextField();
		cNameField.setBounds(97, 309, 147, 26);
		mainPanel.add(cNameField);
		cNameField.setColumns(10);
		
		clientName = new JLabel("클라이언트 닉네임");
		clientName.setBounds(128, 288, 112, 16);
		mainPanel.add(clientName);


		// 서버 버튼 리스너 
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 빈칸 처리 
				if(portField.getText().equals("") || ipField.getText().equals("") || cNameField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "정보를 입력해주세요!!", "message", JOptionPane.PLAIN_MESSAGE);
				}
				else {
				// ServerFrame 객체 생성
				try {
					int port = Integer.parseInt(portField.getText());	// 필드 읽어서 int형으로 변환 
					String clientName = cNameField.getText();	// 필드 읽어오기 
					ServerFrame frame = new ServerFrame(port); 	// 서버 프레임 생성 
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "실패", "message", JOptionPane.PLAIN_MESSAGE);
				}
				}
			}
		});

		// 클라이언트 버튼 리스너 
		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 빈칸 처리 
				if(portField.getText().equals("") || ipField.getText().equals("") || cNameField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "정보를 입력해주세요!!", "message", JOptionPane.PLAIN_MESSAGE);
				}
				else{
					// ClientFrame 객체 생성
					try {
						int port = Integer.parseInt(portField.getText());	// 필드 읽어서 int형으로 변환 
						String ipAd = ipField.getText();	// 필드 읽어오기 
						ClientFrame frame = new ClientFrame(port, ipAd);	// 클라이언트 프레임 생성 
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "실패, 서버가 연결됐는지 확인해주세요.", "message", JOptionPane.PLAIN_MESSAGE);
					}
				}
				
			}
		});
		
		setVisible(true);
	}

	
	public static void main(String[] args) {
		new MainFrame();	// 메인 프레임 실행 
	}

}
