package config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.FcmErrorCode;

@Slf4j
@Configuration
public class FcmConfig {

  @PostConstruct
  public void init() {
    try {
      InputStream serviceAccount = new ClassPathResource(
          "firebasekey.json").getInputStream();
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
      }
    } catch (Exception e) {
      throw new ApplicationException(FcmErrorCode.FCM_INIT_ERROR);
    }
  }
}
