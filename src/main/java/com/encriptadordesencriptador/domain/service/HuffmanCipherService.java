package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.Cipher;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class HuffmanCipherService implements Cipher {

    private Map<Character, String> huffmanCodes;
    private Node root;

    private static class Node implements Comparable<Node> {
        final char character;
        final int frequency;
        final Node leftChild, rightChild;

        Node(char character, int frequency, Node leftChild, Node rightChild) {
            this.character = character;
            this.frequency = frequency;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        boolean isLeaf() {
            return leftChild == null && rightChild == null;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }

    @Override
    public String encrypt(String text) {
        Map<Character, Integer> frequencyMap = buildFrequencyMap(text);
        root = buildHuffmanTree(frequencyMap);
        huffmanCodes = generateHuffmanCodes(root);
        return encodeText(text);
    }

    @Override
    public String decrypt(String encodedText) {
        return decodeText(encodedText);
    }

    private Map<Character, Integer> buildFrequencyMap(String text) {
        return text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
    }

    private Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<Node> priorityQueue = frequencyMap.entrySet().stream()
                .map(entry -> new Node(entry.getKey(), entry.getValue(), null, null))
                .collect(Collectors.toCollection(PriorityQueue::new));

        while (priorityQueue.size() > 1) {
            Node leftChild = priorityQueue.poll();
            Node rightChild = priorityQueue.poll();
            Node parentNode = new Node('\0', leftChild.frequency + rightChild.frequency, leftChild, rightChild);
            priorityQueue.add(parentNode);
        }

        return priorityQueue.poll();
    }

    private Map<Character, String> generateHuffmanCodes(Node root) {
        Map<Character, String> huffmanCodes = new HashMap<>();
        buildHuffmanCode(root, "", huffmanCodes);
        return huffmanCodes;
    }

    private void buildHuffmanCode(Node node, String code, Map<Character, String> huffmanCodes) {
        if (node.isLeaf()) {
            huffmanCodes.put(node.character, code);
        } else {
            buildHuffmanCode(node.leftChild, code + '0', huffmanCodes);
            buildHuffmanCode(node.rightChild, code + '1', huffmanCodes);
        }
    }

    private String encodeText(String text) {
        return text.chars()
                .mapToObj(c -> (char) c)
                .map(huffmanCodes::get)
                .collect(Collectors.joining());
    }

    private String decodeText(String encodedText) {
        return encodedText.chars()
                .mapToObj(bit -> (char) bit)
                .map(bit -> bit == '0' ? '0' : '1')
                .collect(() -> new Object[]{new StringBuilder(), root}, (acc, bit) -> {
                    Node currentNode = (Node) acc[1];
                    currentNode = bit == '0' ? currentNode.leftChild : currentNode.rightChild;
                    if (currentNode.isLeaf()) {
                        ((StringBuilder) acc[0]).append(currentNode.character);
                        acc[1] = root;
                    } else {
                        acc[1] = currentNode;
                    }
                }, (acc1, acc2) -> {
                    throw new UnsupportedOperationException("Parallel execution not supported");
                })[0].toString();
    }
}