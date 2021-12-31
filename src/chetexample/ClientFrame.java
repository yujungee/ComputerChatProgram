package chetexample;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;

/// 디자인이랑 기능 다듬어서 내자 그냥 

public class ClientFrame extends JFrame {

	private JTextArea textArea;
	private JTextField msgField;
	private JButton sendButton;
	private Socket socket;
	private DataInputStream reader;
	private DataOutputStream writer;
	private JButton exitButton;
	private JPanel panel;
	private JLabel nickLabel;
	private JPanel menuPanel;

	public ClientFrame(int port, String ipAd) throws IOException {
//		serverName = serverNick;

		// 소켓 연결 ,  소켓부분 익셉션은 메인에서
		socket = new Socket(ipAd, port);

		// 스레드
		ReceiverThread clientThread = new ReceiverThread();
		clientThread.setDaemon(true);
		clientThread.start();

		/* 디자인 */
		setBackground(Color.WHITE);

		setTitle("Client");
	setBackground(new Color(255, 255, 255));		
		setBounds(450, 50, 345, 450);
		getContentPane().setLayout(null);

		

		textArea = new JTextArea();		
		textArea.setBackground(new Color(176, 196, 222));

		textArea.setEditable(false); //쓰기 금지
		textArea.append("대화를 시작합니다.\n");


		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(0, 36, 345, 342);

		getContentPane().add(scrollPane);

				

		JPanel msgPanel = new JPanel();
		msgPanel.setBounds(0, 378, 345, 45);

		msgPanel.setLayout(new BorderLayout());

		msgField = new JTextField();

		sendButton = new JButton("send");

		msgPanel.add(msgField, BorderLayout.CENTER);

		msgPanel.add(sendButton, BorderLayout.EAST);

		

		getContentPane().add(msgPanel);
		
		menuPanel = new JPanel();
		menuPanel.setBackground(new Color(240, 248, 255));
		menuPanel.setBounds(0, 0, 345, 36);
		getContentPane().add(menuPanel);
		menuPanel.setLayout(null);
		
		exitButton = new JButton("채팅 종료");
		exitButton.setBounds(237, 6, 102, 29);
		menuPanel.add(exitButton);
		
		nickLabel = new JLabel("<SERVER>");
		nickLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nickLabel.setBounds(6, 6, 219, 21);
		menuPanel.add(nickLabel);

		// 종료하기 버튼 리스너
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int exitOption = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "채팅 종료", JOptionPane.YES_NO_OPTION);
				// YES_OPTION은 0, NO_OPTION은 1, CLOSED_OPTION은 -1을 반환한다
				if (exitOption == JOptionPane.YES_OPTION) {
					try {
						if (writer != null)
							writer.close();

						if (reader != null)
							reader.close();

						if (socket != null)
							socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
					dispose();
				} else if ((exitOption == JOptionPane.NO_OPTION) || (exitOption == JOptionPane.CLOSED_OPTION)) {
					return; // 아무 작업도 하지 않고 다이얼로그 상자를 닫는다
				}
			}
		});

		// send 버튼 리스너
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage(); // 발신
			}
		});

		// 엔터키 리스너
		msgField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				// 입력받은 키가 엔터인지 알아내기, KeyEvent 객체가 키에대한 정보 갖고있음
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_ENTER:
					sendMessage();
					break;
				}
			}
		});

		msgField.requestFocus();
		setVisible(true); // 보이기
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				try {
					if (writer != null)
						writer.close();

					if (reader != null)
						reader.close();

					if (socket != null)
						socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

	}// 생성자

	// 이너클래스로 스레드
	class ReceiverThread extends Thread {

		public void run() {
			try {
				// 데이터 전송을 위한 스트림 생성
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				// 보조스트림으로 만들어서 데이터전송
				reader = new DataInputStream(is);
				writer = new DataOutputStream(os);
				
				while (true) {// 메시지 수신
					String msg = reader.readUTF(); // 읽어오기
					textArea.append(" [SERVER] : " + msg + "\n"); // 화면에 보여줌
					textArea.setCaretPosition(textArea.getText().length());
				}

			} catch (UnknownHostException e) {
				textArea.append("서버 주소가 이상합니다.\n");
			} catch (IOException e) {
				textArea.append("대화가 종료되었습니다.\n");
			}

		}

	}

	// 메시지 발신 메소드
	void sendMessage() {

		String msg = msgField.getText(); // TextField에 써있는 글씨를 얻어오기
		msgField.setText(""); // 입력 후 빈칸으로
		textArea.append(" [나] : " + msg + "\n"); // 화면에 표시
		textArea.setCaretPosition(textArea.getText().length());

		// senderThread
		Thread senderThread = new Thread() {

			public void run() {
				try {
					writer.writeUTF(msg); // 파일에 쓰기
					writer.flush();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "전송을 실패하였습니다.", "message", JOptionPane.PLAIN_MESSAGE);
				}
			}
		};

		senderThread.start();

	}
	
	
}// class