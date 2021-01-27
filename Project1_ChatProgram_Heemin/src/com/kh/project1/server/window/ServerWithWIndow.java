package com.kh.project1.server.window;

/* Client코드먼저 만든후 거기에서 상당부분 붙여넣기 하였으므로 자세한 주석은 Client.java참고 */

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ServerWindow{
	JFrame cFrame;
	JTextArea text; // 채팅 내용을 보여줄 TextArea
	JTextField message; // 메시지를 입력할 TextField
	JButton sendBt; // send 버튼
	JPanel input; // 메시지 입력창과 send 버튼을 넣기위해서 선언
	
	Socket sock;
	BufferedReader sockIn;
	BufferedWriter sockOut;
	
	
	public ServerWindow() {
		init();
	}
	
	public void init() {
		// Frame 생성
		cFrame = new JFrame("Server");
		cFrame.setSize(400,500);
		
		text = new JTextArea();
		text.setEditable(false); // 사용자가 마음대로 편집할 수 없도록
		
		JScrollPane scroll = new JScrollPane(text);
		cFrame.add(scroll, BorderLayout.CENTER);
		
		// 판넬 생성하고 textField와 send 버튼을 배치
		input = new JPanel();
		input.setLayout(new BorderLayout());
		
		message = new JTextField();
		sendBt = new JButton("send");
		cFrame.add(input, BorderLayout.SOUTH);
		input.add(message, BorderLayout.CENTER);
		input.add(sendBt, BorderLayout.EAST);
		
		// 마우스로 send버튼 클릭하면 textField의 메시지가 전송되도록 이벤트처리
		sendBt.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				sendMessage(); 
			}
		});
		
		// textField에 입력하고 Enter치면 메시지가 전송되도록 이벤트처리
		message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// 입력받은 키가 Enter라면 메시지를 전송
				int keyCode = e.getKeyCode();
				if(keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
	}
	
	public void show() {
		cFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cFrame.setResizable(false); 
		cFrame.setVisible(true);
	}
	
	// 서버를 열고 클라이언트의 접속을 받는다
	public void connect(){
		String recv = "";
		try {
			ServerSocket sSocket = new ServerSocket(5100);
			text.append("서버 준비 완료. 클라이언트를 기다립니다.\n");
			
			Socket sock = sSocket.accept();
			text.append("접속 클라이언트 정보 : " + sock.getInetAddress() + " : " + sock.getPort() + "\n");
			
			sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			sockOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			while(true) {
				// 클라이언트가 보내준 내용 읽어온다
				recv = sockIn.readLine();
				
				// 클라이언트가 종료하면 알림메시지 알려주기
				if(recv == null) {
					text.append("클라이언트가 채팅을 종료하였습니다.\n1분동안 클라이언트 접속 없을시 자동으로 종료합니다.\n");
					try {
						sSocket.close();
						Thread.sleep(60000); // 1분기다렸다가 프로그램을 종료한다
						if(recv == null) {
							System.exit(0);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					// textArea에 클라이언트가 보낸 메시지 붙이기
					text.append(recv + "\n");
					text.setCaretPosition(text.getText().length());
				}
			}
		} catch (IOException e) {
			text.append("클라이언트와 연결이 종료되었습니다.\n");
		}
	
	}
	
	public void sendMessage() {
		String send = message.getText() + "\r\n"; // textField의 문자열을 받아와서 send변수에 저장
		message.setText(""); // 받아오면 다시 빈 문자열로 만들어준다(버퍼비우기처럼)
		
		// textArea에 내용을 넣고 맨 아래로 스크롤해준다 
		text.append("[ " + "Server" + " ]  " + send);
		text.setCaretPosition(text.getText().length()); 
		
		// 클라이언트에게 메시지 전송
		try {
			sockOut.write(send);
			sockOut.flush();
		} catch (IOException e) {
			text.append("연결이 되지 않았습니다.");
		}
		
	}
}

public class ServerWithWIndow {

	public static void main(String[] args) {
		ServerWindow server = new ServerWindow();
		server.show();
		server.connect();
	}

}
