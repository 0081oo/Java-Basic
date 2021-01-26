package com.kh.project1.client;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ClientWindow{
	JFrame cFrame;
	JTextArea text; // 채팅 내용을 보여줄 TextArea
	JTextField message; // 메시지를 입력할 TextField
	JButton sendBt; // send 버튼
	JPanel input; // 메시지 입력창과 send 버튼을 넣기위해서 선언
	String clientName;
	
	Socket sock;
	BufferedReader sockIn;
	BufferedWriter sockOut;
	
	
	public ClientWindow() {
		clientName = JOptionPane.showInputDialog("대화명을 입력하세요");
		init();
	}
	
	public void init() {
		// Frame 생성 : JFrame을 상속받는 방법을 알게되어서 그렇게 하려고 했으나.. 다시 원래방법대로 하기로 했다.
		cFrame = new JFrame("Client");
		cFrame.setSize(400,500);
		
		
		// 채팅 내용을 보여주는 Text변수는 사용자가 쓸 수 없도록 setEditable 함수를 이용
		text = new JTextArea();
		text.setEditable(false);
		
		// scroll pane 은 컴포넌트에 스크롤 기능을 제공한다. textArea와 많이 같이쓴다!
		// 채팅량이 많아져서 text변수가 보이기에 꽉차면 위로 스크롤을 올리려고 scrollPane을 사용
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
//			@Override 
			public void KeyPressed(KeyEvent e) {
				
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
		cFrame.setResizable(false); // 사용자가 창 크기를 마우스드래그로 조정못하게
		cFrame.setVisible(true);
	}
	
	// 서버와 연결하고 서버로부터 받아온 메시지를 textArea에 보여주는 connect() 메소드
	public void connect(){
		String recv = "";
		try {
			sock = new Socket("127.0.0.1", 51000);
			
			sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			sockOut = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			// 최초로 보여줄 메시지. 어차피 sock이 연결안되면 밑에서 catch처리 되기때문에 여기 한번 넣어주면 될 것 같다.
			text.append("서버와 연결되었습니다. 메시지를 입력하세요");
			while(true) {
				// 서버가 보내준 내용을 읽어온다
				recv = sockIn.readLine();
				
				// 서버가 종료하면 2초후 클라이언트도 종료
				if(recv == null) {
					text.append("서버와의 연결이 끊어졌습니다. 2초후 프로그램을 종료합니다.");
					try {
						Thread.sleep(2000); // 2초 기다렸다가 프로그램을 종료한다.
						System.exit(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else {
					// textArea에 서버가 보내준 내용을 붙여넣고 맨 아래로 스크롤해준다 
					text.append("[ Server ]  " + recv + "\n");
					text.setCaretPosition(text.getText().length());
				}
			}
		} catch (UnknownHostException e) {
//			e.printStackTrace();
			text.append("서버의 주소를 찾을 수 없습니다.");
			
		} catch (IOException e) {
//			e.printStackTrace();
			text.append("서버와 연결이 되지 않았습니다.");
		}
	
	}
	
	// textField에 입력한 메시지를 서버로 보내주는 메소드 sendMessage()
	// 위에서 send버튼 클릭시 그리고 입력후 엔터쳤을시 두 번의 방법으로 서버에 메시지를 보내야 하니까
	// 여러번 사용될 코드기 때문에 아얘 메소드로 따로 만들었다.
	public void sendMessage() {
		String send = message.getText(); // textField의 문자열을 받아와서 send변수에 저장
		message.setText(""); // 받아오면 다시 빈 문자열로 만들어준다(버퍼비우기처럼)
		
		// textArea에 내용을 넣고 맨 아래로 스크롤해준다 
		text.append("[ " + this.clientName + " ]  " + send + "\n");
		text.setCaretPosition(text.getText().length()); 
		
		// server에게 메시지 전송
		try {
			sockOut.write(send);
			sockOut.flush();
		} catch (IOException e) {
//			e.printStackTrace();
			text.append("서버와 연결이 되지 않았습니다.");
		}
		
	}
}
public class Client {

	public static void main(String[] args) {
		ClientWindow client = new ClientWindow();
		client.show();
		client.connect();
	}

}

