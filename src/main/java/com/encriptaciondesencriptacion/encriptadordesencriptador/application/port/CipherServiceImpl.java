package com.encriptaciondesencriptacion.encriptadordesencriptador.application.port;

import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.service.CaesarCipherService;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.service.TranspositionCipherService;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.service.VigenereCipherService;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.CipherText;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.GeneralKey;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.PlainText;

public class CipherServiceImpl implements CipherService {

    @Override
    public CipherText encrypt(PlainText plainText, GeneralKey key) {
        int caesarShift = generateCaesarShift(key.getValue());
        String vigenereKey = generateVigenereKey(key.getValue(), plainText.getValue().length());

        CaesarCipherService caesarCipher = new CaesarCipherService(caesarShift);
        VigenereCipherService vigenereCipher = new VigenereCipherService(vigenereKey);
        TranspositionCipherService transpositionCipher = new TranspositionCipherService();

        String caesarEncrypted = caesarCipher.encrypt(plainText.getValue());
        String vigenereEncrypted = vigenereCipher.encrypt(caesarEncrypted);
        String finalEncrypted = transpositionCipher.encrypt(vigenereEncrypted);

        return new CipherText(finalEncrypted);
    }

    @Override
    public PlainText decrypt(CipherText cipherText, GeneralKey key) {
        int caesarShift = generateCaesarShift(key.getValue());
        String vigenereKey = generateVigenereKey(key.getValue(), cipherText.getValue().length());

        TranspositionCipherService transpositionCipher = new TranspositionCipherService();
        VigenereCipherService vigenereCipher = new VigenereCipherService(vigenereKey);
        CaesarCipherService caesarCipher = new CaesarCipherService(caesarShift);

        String transposedDecrypted = transpositionCipher.decrypt(cipherText.getValue());
        String vigenereDecrypted = vigenereCipher.decrypt(transposedDecrypted);
        String finalDecrypted = caesarCipher.decrypt(vigenereDecrypted);

        return new PlainText(finalDecrypted);
    }

    private int generateCaesarShift(String key) {
        int shift = 0;
        for (char ch : key.toCharArray()) {
            shift += ch;
        }
        return shift % 26;
    }

    private String generateVigenereKey(String key, int length) {
        StringBuilder vigenereKey = new StringBuilder();
        for (int i = 0; i < length; i++) {
            vigenereKey.append(key.charAt(i % key.length()));
        }
        return vigenereKey.toString();
    }
}
