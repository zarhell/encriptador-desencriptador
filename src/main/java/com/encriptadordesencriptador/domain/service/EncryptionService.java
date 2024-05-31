package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.CipherUseCase;
import com.encriptadordesencriptador.application.port.EncryptionServicePort;
import com.encriptadordesencriptador.domain.model.CipherText;
import com.encriptadordesencriptador.domain.model.GeneralKey;
import com.encriptadordesencriptador.domain.model.PlainText;

import java.util.Scanner;

public class EncryptionService implements EncryptionServicePort {

    private static final Scanner scanner = new Scanner(System.in);
    private final CipherUseCase cipherUseCase;

    public EncryptionService(CipherUseCase cipherUseCase) {
        this.cipherUseCase = cipherUseCase;
    }

    @Override
    public String getEncryptionKeyFromUser() {
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                return "caesar";
            case "2":
                return "vigenere";
            case "3":
                return "transposition";
            case "4":
                return "huffman";
            default:
                System.out.println("Opcion no válida, se utilizara 'huffman' por defecto.");
                return "huffman";
        }
    }

    @Override
    public String encryptText(String text, String generalKey) {
        PlainText plainText = new PlainText(text);
        GeneralKey key = new GeneralKey(generalKey);
        CipherText encryptedText = cipherUseCase.encrypt(plainText, key);
        return encryptedText.getValue();
    }

    @Override
    public String decryptText(String encryptedText, String generalKey) {
        CipherText cipherText = new CipherText(encryptedText);
        GeneralKey key = new GeneralKey(generalKey);
        PlainText decryptedText = cipherUseCase.decrypt(cipherText, key);
        return decryptedText.getValue();
    }
}