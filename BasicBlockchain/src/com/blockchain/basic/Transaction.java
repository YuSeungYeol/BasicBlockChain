package com.blockchain.basic;

public class Transaction {
    public String sender;   // 송신자
    public String receiver; // 수신자
    public float amount;    // 전송 금액
    private String signature; // 서명 추가

    public Transaction(String sender, String receiver, float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    } 

    @Override
    public String toString() {
        return sender + " -> " + receiver + ": " + amount;
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
}