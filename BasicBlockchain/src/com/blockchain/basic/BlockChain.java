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
        user = user.toLowerCase();  // 대소문자 구분 없이 비교하도록 소문자로 변환

        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                System.out.println("Checking transaction: Sender: " + tx.getSender() + " Receiver: " + tx.getReceiver() + " Amount: " + tx.getAmount());

                // 'System' 송금 처리
                if (tx.getSender().equals("System") && tx.getReceiver().toLowerCase().equals(user)) {
                    balance += tx.getAmount();
                }

                // 송금 및 수신 처리
                if (tx.getReceiver().toLowerCase().equals(user)) {
                    balance += tx.getAmount();
                }
                if (tx.getSender().toLowerCase().equals(user)) {
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
            System.out.println("Enter a command (balance, block, transactions, all, check, exit):");
            String commandLine = scanner.nextLine().trim().toLowerCase();  // 전체 입력을 받음
            System.out.println("Received command: " + commandLine);  // 디버깅

            String[] commandParts = commandLine.split(" ");  // 명령어와 파라미터를 분리
            String command = commandParts[0];  // 첫 번째는 명령어
            String parameter = (commandParts.length > 1) ? commandParts[1] : "";  // 두 번째는 파라미터

            switch (command) {
                case "balance":
                    if (parameter.isEmpty()) {
                        System.out.println("Please enter a user name for balance query.");
                    } else {
                        System.out.println("Getting balance for user: " + parameter);  // 디버깅
                        double balance = getBalance(parameter);
                        System.out.println("Balance of " + parameter + ": " + balance);
                    }
                    break;

                case "block":
                    if (parameter.isEmpty()) {
                        System.out.println("Please enter a block hash for block query.");
                    } else {
                        System.out.println("Looking for block with hash: " + parameter);  // 디버깅
                        Block block = getBlockByHash(parameter);
                        if (block != null) {
                            block.printBlockData();
                        }
                    }
                    break;

                case "transactions":
                    if (parameter.isEmpty()) {
                        System.out.println("Please enter a user name for transaction query.");
                    } else {
                        System.out.println("Getting transactions for user: " + parameter);  // 디버깅
                        ArrayList<Transaction> transactions = getTransactionsByUser(parameter);
                        if (transactions.isEmpty()) {
                            System.out.println("No transactions found for " + parameter);
                        } else {
                            for (Transaction tx : transactions) {
                                System.out.println(tx);
                            }
                        }
                    }
                    break;

                case "all":
                    System.out.println("Displaying all blocks:");  // 디버깅
                    for (Block b : getAllBlocks()) {
                        b.printBlockData();
                    }
                    break;

                case "check":
                    // 블록체인 상태 점검
                    System.out.println("Checking blockchain...");
                    printBlockchain();  // 모든 트랜잭션 출력
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

    // 블록체인 내 모든 트랜잭션 출력
    public static void printBlockchain() {
        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                System.out.println("Sender: " + tx.getSender() + " Receiver: " + tx.getReceiver() + " Amount: " + tx.getAmount());
            }
        }
    }
}
