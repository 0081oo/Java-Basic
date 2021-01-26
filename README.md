# 서버/클라이언트 채팅 프로그램

## 설계 & 목업 이미지
![Mockup Image](./img/MockUp.png)

## 결과물

## 개선사항

2021.01.26 현재
1. textField에 입력하고 Enter를 눌러도 server로 메시지가 전송되지 않는다.
	-> 지금 찾아보는 중인데, Enter키를 눌러서 server로 메시지를 보내고 싶으면 그냥 addKeyListener쓰는 방법이 있고,
	   client클래스가 JFrame을 상속받은 다음에 ActionListenr와 ActionPerformed라는걸 사용해주는 방법이 있는 것 같다.
2. server의 while문 수정해야함 (client가 보내는 모든 메시지들을 받아온 뒤에 내가 답장을 할 수 있도록).
   지금은 client가 한번 메시지 보내고 그다음 server가 한번 메시지 보내고 이런식으로만 소통이 된다.
3. server가 종료하였을시 textArea에 "서버가 연결을 중지하였습니다."라고 출력되게 만들어야함
	-> 해결. client에서 recv = sockIn.readLine() 해줄때 recv == null이면 종료되게하면 된다.
4. 뭔가  send버튼을  두번 눌러줘야 가는것 같은 기분이 든다.

의문 : 왜 서버코드에서 BufferedWriter sockOut쓰고 Scanner객체로 문자열받아서 넘기면 client로 잘 안넘어갈까? -> PrintWriter sockOut하면 잘 되긴한다.
