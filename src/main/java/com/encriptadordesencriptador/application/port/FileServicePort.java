package com.encriptadordesencriptador.application.port;

public interface FileServicePort {
    void saveToFile(String text);
    String readFromFile();
}