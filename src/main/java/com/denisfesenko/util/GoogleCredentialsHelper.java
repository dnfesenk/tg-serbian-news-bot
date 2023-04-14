package com.denisfesenko.util;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class GoogleCredentialsHelper {

    public static GoogleCredentials createFromBase64EvnVar() throws IOException {
        String base64Credentials = Constants.GOOGLE_APPLICATION_CREDENTIALS_BASE64;
        if (base64Credentials == null || base64Credentials.isEmpty()) {
            throw new IOException("Environment variable " + Constants.GOOGLE_APPLICATION_CREDENTIALS + " not set or empty.");
        }

        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);

        try (ByteArrayInputStream credentialsStream = new ByteArrayInputStream(decodedCredentials)) {
            return GoogleCredentials.fromStream(credentialsStream);
        }
    }

    private GoogleCredentialsHelper() {
    }
}
