package org.yapp.domain.match.presentation.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ContactResponse {

  private Map<Integer, String> contacts;
}