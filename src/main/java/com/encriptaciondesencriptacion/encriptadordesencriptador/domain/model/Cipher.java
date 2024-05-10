package com.encriptaciondesencriptacion.encriptadordesencriptador.domain.model;

public interface Cipher {

    String encrypt(String text);
    String decrypt(String text);
}
