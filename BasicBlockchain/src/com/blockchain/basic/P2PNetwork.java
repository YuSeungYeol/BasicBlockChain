package com.blockchain.basic;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class P2PNetwork {
    private ArrayList<String> peers = new ArrayList<>();
    private ServerSocket serverSocket;
    private int port;
    private static Network network = new Network(); // 네트워크 객체 초기화

    public P2PNetwork(int port) {
        this.port = port;
    }

    // 서버 시작
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("P2P Server started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 노드 연결
    public void connectToPeer(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            peers.add(host + ":" + port);
            System.out.println("Connected to peer: " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Failed to connect to peer: " + host + ":" + port);
        }
    }

    // 메시지 전송
    public void broadcast(String message) {
        for (String peer : peers) {
            try {
                String[] parts = peer.split(":");
                String host = parts[0];
                int port = Integer.parseInt(parts[1]);

                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
                socket.close();
            } catch (IOException e) {
                System.out.println("Failed to send message to: " + peer);
            }
        }
    }

    // 클라이언트 요청 처리
    class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                System.out.println("Received: " + message);

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void broadcastNewBlock(Block newBlock) {
        String blockData = serializeBlock(newBlock); // 블록 데이터를 직렬화
        network.broadcast("NEW_BLOCK:" + blockData); // 모든 피어에 전송
    }

    // 블록 직렬화 메서드 추가 (직렬화를 위한 메서드가 없다면 구현 필요)
    private static String serializeBlock(Block block) {
        // 블록 데이터를 문자열로 변환 (직렬화 예제)
        return block.hash + "," + block.previousHash + "," + block.data;
    }
    
    public static void syncBlockchain() {
        network.broadcast("SYNC_REQUEST");
    }

    
}

