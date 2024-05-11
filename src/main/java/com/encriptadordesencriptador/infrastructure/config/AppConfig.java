package com.encriptadordesencriptador.infrastructure.config;

import com.encriptadordesencriptador.application.port.CipherService;
import com.encriptadordesencriptador.application.service.CipherServiceImpl;

public class AppConfig {
    public CipherService cipherService() {
        return new CipherServiceImpl();
    }
}
