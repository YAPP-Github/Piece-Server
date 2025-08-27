package org.yapp.infra.billing.playstore.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PlayStoreConfig {

    @Bean
    public AndroidPublisher androidPublisher(
        @Value("${google.play-store.credentials-path}") String credentialsPath,
        @Value("${google.play-store.application-name}") String applicationName)
        throws IOException, GeneralSecurityException {

        GoogleCredentials credentials = GoogleCredentials.fromStream(
                new ClassPathResource(
                    credentialsPath).getInputStream())
            .createScoped(
                Collections.singleton("https://www.googleapis.com/auth/androidpublisher"));

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        return new AndroidPublisher.Builder(
            httpTransport,
            jsonFactory,
            new HttpCredentialsAdapter(credentials))
            .setApplicationName(applicationName)
            .build();
    }
}
