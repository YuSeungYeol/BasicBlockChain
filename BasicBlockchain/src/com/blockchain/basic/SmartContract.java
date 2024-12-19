package com.blockchain.basic;

public class SmartContract {
    private String condition;
    private Transaction transaction; // 조건이 충족되면 실행할 트랜잭션

    public SmartContract(String condition, Transaction transaction) {
        this.condition = condition;
        this.transaction = transaction;
    }

    // 스마트 계약 실행 메서드
    public boolean execute(String currentState) {
        if (currentState.equals(condition)) {
            System.out.println("Smart Contract Executed: " + transaction);
            return true;
        }
        return false;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
