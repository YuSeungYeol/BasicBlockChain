package com.blockchain.basic;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BlockChainManager {
    private static final String BLOCKCHAIN_FILE = "blockchain.json";
    private static final Gson gson = new Gson();

    // 블록체인을 JSON 파일로 저장
    public static void saveBlockchain(List<Block> blockchain) {
        try (FileWriter writer = new FileWriter(BLOCKCHAIN_FILE)) {
            gson.toJson(blockchain, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // JSON 파일에서 블록체인을 불러오기
    public static List<Block> loadBlockchain() {
        try (FileReader reader = new FileReader(BLOCKCHAIN_FILE)) {
            Type blockListType = new TypeToken<ArrayList<Block>>() {}.getType();
            return gson.fromJson(reader, blockListType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
