package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.Cipher;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCipherService implements Cipher {

    private class Node implements Comparable<Node> {
        char ch;
        int frequency;
        Node left, right;

        Node(char ch, int frequency, Node left, Node right) {
            this.ch = ch;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(Node o) {
            return this.frequency - o.frequency;
        }
    }

    private Map<Character, String> huffmanCodes;
    private Node root;

    @Override
    public String encrypt(String text) {
        Map<Character, Integer> frequencyMap = buildFrequencyMap(text);
        root = buildHuffmanTree(frequencyMap);
        huffmanCodes = new HashMap<>();
        buildHuffmanCodes(root, "");
        return encode(text);
    }

    @Override
    public String decrypt(String text) {
        return decode(text);
    }

    private Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char ch : text.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }
        return frequencyMap;
    }

    private Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (var entry : frequencyMap.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue(), null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            pq.add(parent);
        }

        return pq.poll();
    }

    private void buildHuffmanCodes(Node node, String code) {
        if (!node.isLeaf()) {
            buildHuffmanCodes(node.left, code + '0');
            buildHuffmanCodes(node.right, code + '1');
        } else {
            huffmanCodes.put(node.ch, code);
        }
    }

    private String encode(String text) {
        StringBuilder sb = new StringBuilder();
        for (char ch : text.toCharArray()) {
            sb.append(huffmanCodes.get(ch));
        }
        return sb.toString();
    }

    private String decode(String text) {
        StringBuilder sb = new StringBuilder();
        Node node = root;
        for (char bit : text.toCharArray()) {
            node = bit == '0' ? node.left : node.right;
            if (node.isLeaf()) {
                sb.append(node.ch);
                node = root;
            }
        }
        return sb.toString();
    }
}