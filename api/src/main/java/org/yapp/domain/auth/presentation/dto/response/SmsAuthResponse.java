package org.yapp.domain.auth.presentation.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SmsAuthResponse {

  private Boolean isPhoneNumberDuplicated;
  @JsonInclude(NON_NULL)
  private String oauthProvider;
}
