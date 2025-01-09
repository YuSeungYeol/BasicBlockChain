package com.blockchain.basic;

public class Wallet {
    private String privateKey;
    private String publicKey;

    public Wallet() {
        // 개인키와 공개키 생성
        this.privateKey = "1234";  // 실제로는 암호화된 키 생성 필요
        this.publicKey = "12345678";
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}