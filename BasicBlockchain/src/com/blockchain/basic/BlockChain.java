package com.blockchain.basic;

import java.util.ArrayList;
import java.util.function.Predicate;

public class BlockChain {
    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        int difficulty = 4; // 난이도 설정

        // 제네시스 블록 생성
        Block genesisBlock = new Block("0", "0");  // 첫 번째 블록, previousHash는 "0"
        genesisBlock.addTransaction(new Transaction("System", "Alice", 50));
        genesisBlock.mineBlock(difficulty);
        blockchain.add(genesisBlock);  // 블록체인에 제네시스 블록 추가

        // 두 번째 블록 생성
        Block secondBlock = new Block("0", genesisBlock.hash);  // 이전 블록의 해시를 previousHash로 사용

        // 트랜잭션 추가
        secondBlock.addTransaction(new Transaction("Alice", "Bob", 20));

        // 스마트 계약 추가: 다양한 조건 처리
        SmartContract contract1 = new SmartContract(
            input -> input.equals("approved") && input.length() > 5,
            new Transaction("Alice", "Bob", 15)
        );
        SmartContract contract2 = new SmartContract(
            input -> input.contains("valid"),
            new Transaction("Bob", "Charlie", 10)
        );

        // 블록에 스마트 계약 추가
        secondBlock.addSmartContract(contract1);
        secondBlock.addSmartContract(contract2);

        // 조건에 따라 스마트 계약 실행
        secondBlock.executeSmartContracts("approved_and_valid");

        secondBlock.mineBlock(difficulty);
        blockchain.add(secondBlock);  // 블록체인에 두 번째 블록 추가

        // 세 번째 블록 생성
        Block thirdBlock = new Block("0", secondBlock.hash);  // 이전 블록의 해시를 previousHash로 사용
        thirdBlock.addTransaction(new Transaction("Charlie", "Dave", 2));
        thirdBlock.mineBlock(difficulty);
        blockchain.add(thirdBlock);  // 블록체인에 세 번째 블록 추가

        // 블록 데이터 출력
        for (Block block : blockchain) {
            block.printBlockData();
        }

        // 블록체인 유효성 검증
        System.out.println("Is blockchain valid? " + isChainValid());
    }

    // 블록체인 유효성 검사 메서드
    public static boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            // 현재 블록의 해시가 일치하는지 확인
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Block's hash is invalid!");
                return false;
            }

            // 이전 블록의 해시가 일치하는지 확인
            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Previous Block's hash doesn't match!");
                return false;
            }
        }
        return true;
    }
}
