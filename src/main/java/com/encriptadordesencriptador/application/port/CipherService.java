package com.encriptadordesencriptador.application.port;

import com.encriptadordesencriptador.domain.valueobject.CipherText;
import com.encriptadordesencriptador.domain.valueobject.GeneralKey;
import com.encriptadordesencriptador.domain.valueobject.PlainText;

public interface CipherService {
    CipherText encrypt(PlainText plainText, GeneralKey key);
    PlainText decrypt(CipherText cipherText, GeneralKey key);
}
