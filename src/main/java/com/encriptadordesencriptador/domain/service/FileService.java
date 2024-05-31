package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.FileServicePort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService implements FileServicePort {

    private static final String FILE_NAME = "encryptedText.txt";

    @Override
    public void saveToFile(String text) {
        try {
            Path path = getWritablePath(FILE_NAME);
            Files.write(path, text.getBytes());
            System.out.println("Texto encriptado guardado en " + path.toString());
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @Override
    public String readFromFile() {
        try {
            Path path = getWritablePath(FILE_NAME);
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    private Path getWritablePath(String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), fileName);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }
}