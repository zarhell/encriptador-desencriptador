package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.Cipher;

public class VigenereCipherService implements Cipher {

    private final String key;
    public VigenereCipherService(String key) {
        this.key = key.toLowerCase();
    }

    @Override
    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base + (key.charAt(j % key.length()) - 'a')) % 26 + base);
                j++;
            }
            result.append(ch);
        }

        return result.toString();
    }

    @Override
    public String decrypt(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base - (key.charAt(j % key.length()) - 'a') + 26) % 26 + base);
                j++;
            }
            result.append(ch);
        }

        return result.toString();
    }
}
