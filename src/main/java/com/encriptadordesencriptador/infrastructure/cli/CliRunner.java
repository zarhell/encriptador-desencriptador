package com.encriptadordesencriptador.infrastructure.cli;

import com.encriptadordesencriptador.application.port.CipherService;
import com.encriptadordesencriptador.application.service.CipherServiceImpl;
import com.encriptadordesencriptador.domain.valueobject.CipherText;
import com.encriptadordesencriptador.domain.valueobject.GeneralKey;
import com.encriptadordesencriptador.domain.valueobject.PlainText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CliRunner {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CipherService cipherService = new CipherServiceImpl();
    private static final String FILE_PATH = "src/main/resources/encryptedText.txt";

    public static void main(String[] args) {
        while (true) {
            System.out.println("=== Sistema de Encriptación ===");
            System.out.println("1. Encriptar texto");
            System.out.println("2. Desencriptar texto desde archivo");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleEncryption();
                    break;
                case "2":
                    handleDecryption();
                    break;
                case "3":
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida, por favor intente nuevamente.");
            }
        }
    }

    private static void handleEncryption() {
        System.out.print("Ingrese el texto a encriptar: ");
        String text = scanner.nextLine();

        System.out.print("Ingrese la clave general: ");
        String generalKey = scanner.nextLine();

        PlainText plainText = new PlainText(text);
        GeneralKey key = new GeneralKey(generalKey);

        CipherText encryptedText = cipherService.encrypt(plainText, key);
        System.out.println("Texto encriptado: " + encryptedText.getValue());

        saveToFile(encryptedText.getValue());
    }

    private static void handleDecryption() {
        System.out.print("Ingrese la clave general: ");
        String generalKey = scanner.nextLine();

        String encryptedText = readFromFile();
        if (encryptedText == null) {
            System.out.println("No se pudo leer el texto encriptado desde el archivo.");
            return;
        }

        CipherText cipherText = new CipherText(encryptedText);
        GeneralKey key = new GeneralKey(generalKey);

        PlainText decryptedText = cipherService.decrypt(cipherText, key);
        System.out.println("Texto desencriptado: " + decryptedText.getValue());
    }

    private static void saveToFile(String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(text);
            System.out.println("Texto encriptado guardado en " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    private static String readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }
}
