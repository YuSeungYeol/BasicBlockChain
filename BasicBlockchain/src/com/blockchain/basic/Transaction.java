package com.blockchain.basic;

import java.util.Date;

public class Transaction {
    public String sender;   // 송신자
    public String receiver; // 수신자
    public float amount;    // 전송 금액
    private String signature; // 서명 추가
    private Date executionDate; // 특정 날짜 조건 추가
    private boolean conditionMet; // 조건 충족 여부

    public Transaction(String sender, String receiver, float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.executionDate = executionDate;
        this.conditionMet = false;
    }
    
    public void signTransaction(String privateKey) {
        // 개인키로 트랜잭션 서명 (단순화를 위해 해시값으로 서명한다고 가정)
        this.signature = HashUtil.applySHA256(sender + receiver + amount + privateKey);
    } 

    public boolean verifySignature(String publicKey) {
        // 송신자의 공개키로 서명 검증
        String data = sender + receiver + amount;  // 서명할 데이터
        String calculatedSignature = HashUtil.applySHA256(data + publicKey);  // 공개키로 서명 검증
        return signature != null && signature.equals(calculatedSignature);
    }
    
    public boolean canExecute() {
        return conditionMet && new Date().after(executionDate);
    }

    public void setConditionMet(boolean conditionMet) {
        this.conditionMet = conditionMet;
    }
    
    public void refund(String refundReceiver) {
        System.out.println("Refund: " + amount + " returned to " + refundReceiver);
    }

    @Override
    public String toString() {
        return sender + " -> " + receiver + ": " + amount;
    }
}