package com.encriptadordesencriptador.infrastructure.cli;

import com.encriptadordesencriptador.application.port.CipherUseCase;
import com.encriptadordesencriptador.application.port.EncryptionServicePort;
import com.encriptadordesencriptador.application.port.FileServicePort;
import com.encriptadordesencriptador.domain.model.CipherText;
import com.encriptadordesencriptador.domain.model.GeneralKey;
import com.encriptadordesencriptador.domain.model.PlainText;
import com.encriptadordesencriptador.domain.service.EncryptionService;
import com.encriptadordesencriptador.domain.service.FileService;
import com.encriptadordesencriptador.infrastructure.adapter.CipherAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class CliRunner {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EncryptionServicePort encryptionService = new EncryptionService(new CipherAdapter());
    private static final FileServicePort fileService = new FileService();

    public static void main(String[] args) {
        while (true) {
            printMainMenu();
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

        printEncryptionOptions();

        String generalKey = encryptionService.getEncryptionKeyFromUser();

        String encryptedText = encryptionService.encryptText(text, generalKey);
        System.out.println("Texto encriptado: " + encryptedText);

        fileService.saveToFile(encryptedText);
    }

    private static void handleDecryption() {
        printEncryptionOptions();

        String generalKey = encryptionService.getEncryptionKeyFromUser();

        String encryptedText = fileService.readFromFile();
        if (encryptedText == null) {
            System.out.println("No se pudo leer el texto encriptado desde el archivo.");
            return;
        }

        String decryptedText = encryptionService.decryptText(encryptedText, generalKey);
        System.out.println("Texto desencriptado: " + decryptedText);
    }

    private static void printMainMenu() {
        System.out.println("=== Sistema de Encriptacion ===");
        System.out.println("+---------------------------+");
        System.out.println("| Indice | Opcion           |");
        System.out.println("+---------------------------+");
        System.out.println("|   1    | Encriptar texto  |");
        System.out.println("|   2    | Desencriptar texto desde archivo |");
        System.out.println("|   3    | Salir            |");
        System.out.println("+---------------------------+");
        System.out.print("Seleccione una opcion: ");
    }

    private static void printEncryptionOptions() {
        System.out.println("+-----------------------+");
        System.out.println("| Indice | Tipo         |");
        System.out.println("+-----------------------+");
        System.out.println("|   1    | Caesar       |");
        System.out.println("|   2    | Vigenere     |");
        System.out.println("|   3    | Transposition|");
        System.out.println("|   4    | Huffman      |");
        System.out.println("+-----------------------+");
        System.out.print("Seleccione el tipo de encriptacion: ");
    }
}