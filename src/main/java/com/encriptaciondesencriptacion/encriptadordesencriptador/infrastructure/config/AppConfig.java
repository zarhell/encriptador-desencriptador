package com.encriptaciondesencriptacion.encriptadordesencriptador.infrastructure.config;

import com.encriptaciondesencriptacion.encriptadordesencriptador.application.port.CipherService;
import com.encriptaciondesencriptacion.encriptadordesencriptador.application.port.CipherServiceImpl;

public class AppConfig {
    public CipherService cipherService() {
        return new CipherServiceImpl();
    }
}
