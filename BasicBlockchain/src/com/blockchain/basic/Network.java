package com.blockchain.basic;

public class Network {

	 // 모든 피어에 메시지를 전송
    public void broadcast(String message) {
        // 실제 네트워크 전송 코드 (예: 소켓 사용)
        System.out.println("Broadcasting message to peers: " + message);
    }
}
