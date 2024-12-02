package com.blockchain.basic;

import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;            // 현재 블록의 해시
    public String previousHash;    // 이전 블록의 해시
    String data;           // 블록에 저장될 데이터
    private ArrayList<Transaction> transactions; // 트랜잭션 리스트
    private long timestamp;        // 블록 생성 시간
 
    // 생성자
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.transactions = new ArrayList<>();
        this.timestamp = new Date().getTime(); // 현재 시간
        this.hash = calculateHash();           // 해시 계산
    }

    // 해시 계산 메서드
    public String calculateHash() {
        String input = previousHash + Long.toString(timestamp) + data;
        return HashUtil.applySHA256(input);
    }
    
    public void mineBlock(int difficulty) {
        // 목표 조건: "0000..." 문자열 생성 (난이도에 따라 0의 개수 결정)
        String target = new String(new char[difficulty]).replace('\0', '0');

        // 해시가 목표 조건을 만족할 때까지 반복
        while (!hash.substring(0, difficulty).equals(target)) {
            timestamp++; // 값 변경 (timestamp 또는 nonce를 변경)
            hash = calculateHash(); // 해시 재계산
        }

        System.out.println("Block Mined: " + hash); // 채굴 성공 시 출력
    }

    // 트랜잭션 추가 메서드
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction); // 트랜잭션 추가
        hash = calculateHash(); // 트랜잭션 추가 후 해시 재계산
    }

    // 블록 데이터 출력
    public void printBlockData() {
        System.out.println("Block Hash: " + hash);
        System.out.println("Previous Hash: " + previousHash);
        for (Transaction t : transactions) {
            System.out.println("  Transaction: " + t);
        }
    }
    
    
    

}