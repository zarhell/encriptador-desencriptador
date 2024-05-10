package com.encriptaciondesencriptacion.encriptadordesencriptador.application.port;

import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.CipherText;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.GeneralKey;
import com.encriptaciondesencriptacion.encriptadordesencriptador.domain.valueobject.PlainText;

public interface CipherService {
    CipherText encrypt(PlainText plainText, GeneralKey key);
    PlainText decrypt(CipherText cipherText, GeneralKey key);
}
