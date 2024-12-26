package com.blockchain.basic;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;

public class BlockChain {
    public static ArrayList<Block> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        int difficulty = 4; // 난이도 설정

        // 제네시스 블록 생성
        Block genesisBlock = new Block("0", "0");
        genesisBlock.addTransaction(new Transaction("System", "Alice", 50));
        genesisBlock.mineBlock(difficulty);
        blockchain.add(genesisBlock);

        // 두 번째 블록 생성
        Block secondBlock = new Block("0", genesisBlock.hash);
        secondBlock.addTransaction(new Transaction("Alice", "Bob", 20));
        secondBlock.mineBlock(difficulty);
        blockchain.add(secondBlock);

        // 세 번째 블록 생성
        Block thirdBlock = new Block("0", secondBlock.hash);
        thirdBlock.addTransaction(new Transaction("Charlie", "Dave", 2));
        thirdBlock.mineBlock(difficulty);
        blockchain.add(thirdBlock);

        // 블록 데이터 출력
        for (Block block : blockchain) {
            block.printBlockData();
        }

        // 블록체인 유효성 검증
        System.out.println("Is blockchain valid? " + isChainValid());

        // 명령어 핸들러 실행
        handleCommands();
    }

    // 블록체인 유효성 검사 메서드
    public static boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Block's hash is invalid!");
                return false;
            }

            if (!currentBlock.previousHash.equals(previousBlock.hash)) {
                System.out.println("Previous Block's hash doesn't match!");
                return false;
            }
        }
        return true;
    }

    // 사용자 잔고 조회
    public static double getBalance(String user) {
        double balance = 0;
        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                if (tx.getReceiver().equals(user)) {
                    balance += tx.getAmount();
                }
                if (tx.getSender().equals(user)) {
                    balance -= tx.getAmount();
                }
            }
        }
        return balance;
    }

    // 블록 해시로 블록 조회
    public static Block getBlockByHash(String hash) {
        for (Block block : blockchain) {
            if (block.hash.equals(hash)) {
                return block;
            }
        }
        System.out.println("Block with hash " + hash + " not found.");
        return null;
    }

    // 사용자 트랜잭션 조회
    public static ArrayList<Transaction> getTransactionsByUser(String user) {
        ArrayList<Transaction> userTransactions = new ArrayList<>();
        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                if (tx.getSender().equals(user) || tx.getReceiver().equals(user)) {
                    userTransactions.add(tx);
                }
            }
        }
        return userTransactions;
    }

    // 전체 블록 데이터 반환
    public static ArrayList<Block> getAllBlocks() {
        return blockchain;
    }

    // 명령어 핸들러
    public static void handleCommands() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a command (balance, block, transactions, all, exit):");
            String command = scanner.nextLine().trim().toLowerCase();  // 입력 값에 공백 제거 및 소문자 처리
            System.out.println("Received command: " + command);  // 디버깅 출력

            switch (command) {
                case "balance":
                    System.out.println("Enter user name:");
                    String user = scanner.nextLine().trim();
                    System.out.println("Getting balance for user: " + user);  // 디버깅
                    double balance = getBalance(user);
                    System.out.println("Balance of " + user + ": " + balance);
                    break;

                case "block":
                    System.out.println("Enter block hash:");
                    String hash = scanner.nextLine().trim();
                    System.out.println("Looking for block with hash: " + hash);  // 디버깅
                    Block block = getBlockByHash(hash);
                    if (block != null) {
                        block.printBlockData();
                    }
                    break;

                case "transactions":
                    System.out.println("Enter user name:");
                    String userTx = scanner.nextLine().trim();
                    System.out.println("Getting transactions for user: " + userTx);  // 디버깅
                    ArrayList<Transaction> transactions = getTransactionsByUser(userTx);
                    if (transactions.isEmpty()) {
                        System.out.println("No transactions found for " + userTx);
                    } else {
                        for (Transaction tx : transactions) {
                            System.out.println(tx);
                        }
                    }
                    break;

                case "all":
                    System.out.println("Displaying all blocks:");  // 디버깅
                    for (Block b : getAllBlocks()) {
                        b.printBlockData();
                    }
                    break;

                case "exit":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Unknown command! Please enter a valid command.");
            }
        }
    }

}
