package com.simoon.agent.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MultiFileTextSplitter {

    /**
     * 读取单个文件并按 chunkSize 分割成字符串块
     */
    public static List<String> splitFileToChunks(File file, int chunkSize) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        List<String> chunks = new ArrayList<>();
        String text = content.toString();
        int length = text.length();
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(i + chunkSize, length);
            chunks.add(text.substring(i, end));
        }

        return chunks;
    }

    /**
     * 批量处理多个文件，返回 Map<文件名, List<字符串块>>
     */
    public static Map<String, List<String>> splitFilesToText(List<File> files, int chunkSize) throws IOException {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (File file : files) {
            List<String> chunks = splitFileToChunks(file, chunkSize);
            result.put(file.getName(), chunks);
            System.out.println("文件 [" + file.getName() + "] 分割成 " + chunks.size() + " 段文本");
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        List<File> files = new ArrayList<>();
        files.add(new File("/Users/pisiliang/work/nantian/code/agent-ui/src/main/resources/public/add_flow.html"));
        files.add(new File("/Users/pisiliang/work/nantian/code/agent-ui/src/main/resources/public/db.html"));

        int chunkSize = 500; // 每段500字符
        Map<String, List<String>> allTextChunks = splitFilesToText(files, chunkSize);

        // 测试打印
        allTextChunks.forEach((fileName, chunks) -> {
            System.out.println("=== " + fileName + " ===");
            for (int i = 0; i < chunks.size(); i++) {
                System.out.println("段 " + (i + 1) + ": " + chunks.get(i));
            }
        });
    }
}