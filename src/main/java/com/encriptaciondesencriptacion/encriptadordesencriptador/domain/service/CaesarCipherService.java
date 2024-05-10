package com.encriptaciondesencriptacion.encriptadordesencriptador.domain.service;

import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.model.Cipher;

public class CaesarCipherService implements Cipher {

    private final int shift;

    public CaesarCipherService(int shift) {
        this.shift = shift;
    }
    @Override
    public String encrypt(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base + shift) % 26 + base);
            }
            result.append(ch);
        }

        return result.toString();
    }

    @Override
    public String decrypt(String text) {
        return encrypt(text, 26 - shift);
    }

    private String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetter(ch)) {
                char base = Character.isLowerCase(ch) ? 'a' : 'A';
                ch = (char) ((ch - base + shift) % 26 + base);
            }
            result.append(ch);
        }

        return result.toString();
    }
}
