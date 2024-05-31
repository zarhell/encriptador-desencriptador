package com.encriptadordesencriptador.infrastructure.cli;

import com.encriptadordesencriptador.application.port.CipherUseCase;
import com.encriptadordesencriptador.infrastructure.adapter.CipherAdapter;
import com.encriptadordesencriptador.domain.model.CipherText;
import com.encriptadordesencriptador.domain.model.GeneralKey;
import com.encriptadordesencriptador.domain.model.PlainText;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CliRunner {

    private static final Scanner scanner = new Scanner(System.in);
    private static final CipherUseCase CIPHER_USE_CASE = new CipherAdapter();
    private static final String FILE_NAME = "encryptedText.txt";

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

        CipherText encryptedText = CIPHER_USE_CASE.encrypt(plainText, key);
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

        PlainText decryptedText = CIPHER_USE_CASE.decrypt(cipherText, key);
        System.out.println("Texto desencriptado: " + decryptedText.getValue());
    }

    private static void saveToFile(String text) {
        try {
            Path path = getWritablePath(FILE_NAME);
            Files.write(path, text.getBytes());
            System.out.println("Texto encriptado guardado en " + path.toString());
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    private static String readFromFile() {
        try {
            Path path = getWritablePath(FILE_NAME);
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    private static Path getWritablePath(String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), fileName);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }
}
