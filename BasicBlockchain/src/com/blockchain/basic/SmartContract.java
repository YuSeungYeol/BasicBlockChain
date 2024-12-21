package com.blockchain.basic;

import java.util.function.Predicate;

public class SmartContract {
    private Predicate<String> condition; // 조건을 처리하는 Predicate
    private Transaction action; // 조건이 만족될 때 실행할 트랜잭션

    public SmartContract(Predicate<String> condition, Transaction action) {
        this.condition = condition;
        this.action = action;
    }

    public boolean execute(String input) {
        if (condition.test(input)) {
            System.out.println("Smart Contract Executed: " + action.getSender() + " -> " + action.getReceiver() + ": " + action.getAmount());
            return true;
        } else {
            System.out.println("Smart Contract condition not met.");
            return false;
        }
    }

    public Transaction getAction() {
        return action;
    }
    
    public static void executeConditionalTransaction(Transaction transaction) {
        if (transaction.canExecute()) {
            System.out.println("Smart Contract Executed: " + transaction);
        } else {
            System.out.println("Smart Contract Conditions Not Met: Transaction Pending");
        }
    }
    
    public static void executeOrRefund(Transaction transaction, String refundReceiver) {
        if (transaction.canExecute()) {
            System.out.println("Smart Contract Executed: " + transaction);
        } else {
            transaction.refund(refundReceiver);
        }
    }
}
