package com.kh.project1.server;


import java.io.*;
import java.net.*;

// 클라이언트는 textField에서 문자열을 그대로 버퍼로 넘겨 읽어올것이니까 그냥 바로 BufferedWriter sockOut해도 되는데
// 서버는 내가 키보드로 입력한 문자열을 전달해줄거니까 PrintWriter라는걸 쓰면 더 간단한 것 같다.
// (사실 그냥 BufferedWriter sockOut 쓰고 Scanner로 받아서 문자열 버퍼로 넘겨주면 왜인지 문자열이 클라이언트로 안넘어간다ㅠㅠ)
// 아무튼 PrintWriter 쓰니까 해결되기는 했다.

public class Server {
	
	public static void main(String[] args) {
		
		try {
			String recv = "", send = "";
			char[] buffer = new char[512];
			
			ServerSocket sSocket = new ServerSocket(51000);
			
			Socket sock = sSocket.accept();
			System.out.println("서버 연결되었습니다.");
			System.out.println("접속 클라이언트 정보 : " + sock.getInetAddress() + " : " + sock.getPort());
			
			BufferedReader sockIn; // Client의 데이터를 읽어올 입력스트림
			sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			BufferedReader keyboardIn; // 키보드로부터 데이터를 읽어올 입력스트림
			keyboardIn = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter sockOut; // Client로 내보내기 위한 출력스트림
			sockOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));

//			sockOut.println("서버가 준비되었습니다. 클라이언트 접속을 기다립니다.");
			while(true) {
				if(sockIn.ready()) {	
					int len = sockIn.read(buffer);
					recv = new String(buffer, 0, len);
					
					System.out.println("Client가 보낸 메시지 입니다 : " + recv);
				
					System.out.print("Client로 보낼 메시지 입력하세요 : ");
					send = keyboardIn.readLine();
					sockOut.println(send);
					sockOut.flush();

				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

