package com.encriptadordesencriptador.infrastructure.config;

import com.encriptadordesencriptador.application.port.CipherUseCase;
import com.encriptadordesencriptador.infrastructure.adapter.CipherAdapter;

public class AppConfig {
    public CipherUseCase cipherService() {
        return new CipherAdapter();
    }
}