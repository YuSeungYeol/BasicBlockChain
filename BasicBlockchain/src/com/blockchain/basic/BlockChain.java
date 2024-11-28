package com.blockchain.basic;

import java.util.ArrayList;

public class BlockChain {
    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        int difficulty = 4; // 난이도 설정

        // 제네시스 블록
        Block genesisBlock = new Block("0", "0");  // 첫 번째 블록, previousHash는 "0"
        genesisBlock.addTransaction(new Transaction("System", "Alice", 50));
        genesisBlock.mineBlock(difficulty);
        blockchain.add(genesisBlock);  // 블록체인에 제네시스 블록 추가
        
        // 트랜잭션 서명 테스트
        Wallet wallet = new Wallet();
        Transaction transaction = new Transaction("Alice", "Bob", 20.0f);
        transaction.signTransaction(wallet.getPrivateKey());  // 개인키로 서명
        boolean isValid = transaction.verifySignature(wallet.getPublicKey());  // 공개키로 서명 검증
        if (isValid) {
            System.out.println("Transaction is valid!");
        } else {
            System.out.println("Invalid transaction!");
        }

        // 두 번째 블록
        Block secondBlock = new Block("0", genesisBlock.hash);  // 이전 블록의 해시를 previousHash로 사용
        secondBlock.addTransaction(new Transaction("Alice", "Bob", 20));
        secondBlock.addTransaction(new Transaction("Bob", "Charlie", 5));
        secondBlock.mineBlock(difficulty);
        blockchain.add(secondBlock);  // 블록체인에 두 번째 블록 추가

        // 세 번째 블록
        Block thirdBlock = new Block("0", secondBlock.hash);  // 이전 블록의 해시를 previousHash로 사용
        thirdBlock.addTransaction(new Transaction("Charlie", "Dave", 2));
        thirdBlock.mineBlock(difficulty);
        blockchain.add(thirdBlock);  // 블록체인에 세 번째 블록 추가

        // 블록체인 출력
        genesisBlock.printBlockData();
        secondBlock.printBlockData();
        thirdBlock.printBlockData();

        // 블록체인 유효성 검증
        System.out.println("Is blockchain valid? " + isChainValid());
    }

    // 유효성 검증 메서드
    public static boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            // 현재 블록의 해시가 올바른지 확인
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Block's hash is invalid!");
                return false;
            }

            // 현재 블록의 이전 해시가 이전 블록의 해시와 일치하는지 확인
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Previous Block's hash doesn't match!");
                return false;
            }
        }
        return true; // 모든 블록이 유효한 경우 true 반환
    }
}
