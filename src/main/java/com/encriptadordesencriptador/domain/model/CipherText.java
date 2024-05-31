package com.encriptadordesencriptador.domain.model;

public class CipherText {
    private final String value;

    public CipherText(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
