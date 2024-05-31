package com.encriptadordesencriptador.application.port;

public interface EncryptionServicePort {
    String getEncryptionKeyFromUser();
    String encryptText(String text, String generalKey);
    String decryptText(String encryptedText, String generalKey);
}
