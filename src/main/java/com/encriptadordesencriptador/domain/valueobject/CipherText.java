package com.encriptadordesencriptador.domain.valueobject;

public class CipherText {
    private final String value;

    public CipherText(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
