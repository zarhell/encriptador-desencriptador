package com.encriptadordesencriptador.application.port;

import com.encriptadordesencriptador.domain.model.CipherText;
import com.encriptadordesencriptador.domain.model.GeneralKey;
import com.encriptadordesencriptador.domain.model.PlainText;

public interface CipherService {
    CipherText encrypt(PlainText plainText, GeneralKey key);
    PlainText decrypt(CipherText cipherText, GeneralKey key);
}
