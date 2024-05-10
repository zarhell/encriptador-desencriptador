package com.encriptaciondesencriptacion.encriptadordesencriptador.infrastructure.cli;

import com.encriptaciondesencriptacion.encriptadordesencriptador.application.port.CipherService;
import com.encriptaciondesencriptacion.encriptadordesencriptador.application.port.CipherServiceImpl;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.CipherText;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.GeneralKey;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.PlainText;

import java.util.Scanner;

public class CliRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CipherService cipherService = new CipherServiceImpl();

        System.out.print("Ingrese el texto a encriptar: ");
        String text = scanner.nextLine();

        System.out.print("Ingrese la clave general: ");
        String generalKey = scanner.nextLine();

        PlainText plainText = new PlainText(text);
        GeneralKey key = new GeneralKey(generalKey);

        // Encriptación
        CipherText encryptedText = cipherService.encrypt(plainText, key);
        System.out.println("Texto encriptado: " + encryptedText.getValue());

        // Desencriptación
        PlainText decryptedText = cipherService.decrypt(encryptedText, key);
        System.out.println("Texto desencriptado: " + decryptedText.getValue());

        scanner.close();
    }
}
