package com.encriptadordesencriptador.infrastructure.config;

import com.encriptadordesencriptador.application.port.CipherUseCase;

public class AppConfig {
    public CipherUseCase cipherService() {
        return new CipherAdapter();
    }
}