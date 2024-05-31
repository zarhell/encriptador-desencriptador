package com.encriptadordesencriptador.application.port;

public interface Cipher {

    String encrypt(String text);
    String decrypt(String text);
}
