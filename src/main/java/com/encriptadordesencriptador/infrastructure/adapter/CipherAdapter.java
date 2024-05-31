package com.encriptadordesencriptador.infrastructure.adapter;

import com.encriptadordesencriptador.application.port.Cipher;
import com.encriptadordesencriptador.application.port.CipherUseCase;
import com.encriptadordesencriptador.domain.model.CipherText;
import com.encriptadordesencriptador.domain.model.GeneralKey;
import com.encriptadordesencriptador.domain.model.PlainText;
import com.encriptadordesencriptador.domain.service.CaesarCipherService;
import com.encriptadordesencriptador.domain.service.HuffmanCipherService;
import com.encriptadordesencriptador.domain.service.TranspositionCipherService;
import com.encriptadordesencriptador.domain.service.VigenereCipherService;

import java.util.HashMap;
import java.util.Map;

public class CipherAdapter implements CipherUseCase {
    private Map<String, Cipher> cipherStrategies = new HashMap<>();

    public CipherAdapter() {
        cipherStrategies.put("caesar", new CaesarCipherService(3));
        cipherStrategies.put("vigenere", new VigenereCipherService("key"));
        cipherStrategies.put("transposition", new TranspositionCipherService());
        cipherStrategies.put("huffman", new HuffmanCipherService());
    }
    @Override
    public CipherText encrypt(PlainText plainText, GeneralKey key) {
        Cipher cipher = selectCipher(key.getValue());
        String encryptedText = cipher.encrypt(plainText.getValue());
        return new CipherText(encryptedText);
    }

    @Override
    public PlainText decrypt(CipherText cipherText, GeneralKey key) {
        Cipher cipher = selectCipher(key.getValue());
        String decryptedText = cipher.decrypt(cipherText.getValue());
        return new PlainText(decryptedText);
    }

    private Cipher selectCipher(String key) {
        return cipherStrategies.getOrDefault(key.toLowerCase(), new CaesarCipherService(3)); // Default to Caesar if not found
    }
}
