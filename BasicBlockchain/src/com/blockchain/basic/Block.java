package com.blockchain.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    public String hash;            // 현재 블록의 해시
    public String previousHash;    // 이전 블록의 해시
    String data;           // 블록에 저장될 데이터
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<SmartContract> smartContracts = new ArrayList<>();
    private long timestamp;        // 블록 생성 시간
    
    // 새 필드 추가
    private static final double BLOCK_REWARD = 50; // 채굴 보상 (예: 50 암호화폐)
    private double totalFees = 0;  // 트랜잭션 수수료 합산
 
    // 생성자
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.transactions = new ArrayList<>();
        this.smartContracts = new ArrayList<>();
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
        totalFees += transaction.getFee(); // 트랜잭션 수수료 합산
        hash = calculateHash(); // 트랜잭션 추가 후 해시 재계산
    }
    
    // 스마트 계약 추가 메서드
    public void addSmartContract(SmartContract contract) {
        smartContracts.add(contract);
    }

    // 스마트 계약 실행 메서드
    public void executeSmartContracts(String input) {
        for (SmartContract sc : smartContracts) {
            sc.execute(input);
        }
    }
 
    // 블록 데이터 출력
    public void printBlockData() {
        System.out.println("Block Hash: " + hash);
        System.out.println("Previous Hash: " + previousHash);
        for (Transaction t : transactions) {
            System.out.println("  Transaction: " + t);
        }
    }
   
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // 채굴 보상 지급 메서드
    public double mineBlockAndReward(int difficulty) {
        // 채굴 보상 및 수수료 지급
        double reward = BLOCK_REWARD + totalFees; // 블록 보상 + 수수료
        System.out.println("Block mined! Reward: " + reward);
        return reward;  // 보상 반환
    }
    
    // 채굴 보상 및 수수료 반영
    public void rewardMiner() {
        float reward = (float) (BLOCK_REWARD + totalFees);  // 보상 + 트랜잭션 수수료 합산, float 타입으로 변환
        Transaction rewardTransaction = new Transaction("System", "Miner", reward, 0.0);  // 보상 트랜잭션, 수수료는 0
        this.addTransaction(rewardTransaction);  // 보상 트랜잭션을 블록에 추가
        System.out.println("Miner rewarded with: " + reward);
    }

}
