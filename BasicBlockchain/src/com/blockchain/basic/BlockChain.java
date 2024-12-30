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
        genesisBlock.addTransaction(new Transaction("System", "Alice", 50, 0.0));
        genesisBlock.mineBlock(difficulty);
        blockchain.add(genesisBlock);

        // 두 번째 블록 생성
        Block secondBlock = new Block("0", genesisBlock.hash);
        secondBlock.addTransaction(new Transaction("Alice", "Bob", 20, 0.0));
        secondBlock.mineBlock(difficulty);
        blockchain.add(secondBlock);

        // 세 번째 블록 생성
        Block thirdBlock = new Block("0", secondBlock.hash);
        thirdBlock.addTransaction(new Transaction("Charlie", "Dave", 2, 0.0));
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
        user = user.toLowerCase(); // 입력값 소문자 변환

        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                String sender = tx.getSender().toLowerCase();  // 송신자 소문자 변환
                String receiver = tx.getReceiver().toLowerCase();  // 수신자 소문자 변환

                System.out.println("Checking transaction: Sender: " + tx.getSender() + " Receiver: " + tx.getReceiver() + " Amount: " + tx.getAmount() + " Fee: " + tx.getFee());

                if (receiver.equals(user)) {
                    System.out.println("Matched as receiver: " + receiver);
                    balance += tx.getAmount();  // 수신자는 금액 증가
                }
                if (sender.equals(user)) {
                    System.out.println("Matched as sender: " + sender);
                    balance -= (tx.getAmount() + tx.getFee());  // 송신자는 금액 및 수수료 차감
                }
            }
        }
        return balance;
    }


    // 블록 해시 또는 번호로 블록 조회
    public static Block getBlockByIdentifier(String identifier) {
        // 블록 해시로 조회
        for (Block block : blockchain) {
            if (block.hash.equals(identifier)) {
                return block;
            }
        }
  
        // 블록 번호로 조회
        try {
            int blockNumber = Integer.parseInt(identifier); // identifier를 숫자로 변환
            if (blockNumber >= 0 && blockNumber < blockchain.size()) {
                return blockchain.get(blockNumber); // 해당 번호의 블록 반환
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid block identifier. Must be hash or numeric index.");
        }

        System.out.println("Block with identifier " + identifier + " not found.");
        return null;
    }


    // 사용자 트랜잭션 조회
    public static ArrayList<Transaction> getTransactionsByUser(String user) {
        ArrayList<Transaction> userTransactions = new ArrayList<>();
        String targetUser = user.toLowerCase(); // 입력값 소문자 변환
        for (Block block : blockchain) {
            for (Transaction tx : block.getTransactions()) {
                String sender = tx.getSender().toLowerCase();  // 송신자 소문자 변환
                String receiver = tx.getReceiver().toLowerCase();  // 수신자 소문자 변환
                if (sender.equals(targetUser) || receiver.equals(targetUser)) {
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
                        System.out.println("Please enter a block identifier (hash or number) for block query.");
                    } else {
                        System.out.println("Looking for block with identifier: " + parameter);
                        Block block = getBlockByIdentifier(parameter); // 해시 또는 번호로 블록 조회
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
