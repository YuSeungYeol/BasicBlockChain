package com.blockchain.basic;

import java.util.Random;

public class ExternalDataFetcher {

	public static double fetchExchangeRate() {
        // 실제로는 외부 API 호출을 통해 데이터를 가져와야 함
        // 예: https://api.exchangerate-api.com/v4/latest/USD
        Random random = new Random();
        return 950 + (random.nextDouble() * 200); // 950 ~ 1150 사이의 임의 환율
    } 
}
