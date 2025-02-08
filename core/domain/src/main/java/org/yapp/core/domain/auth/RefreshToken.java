package org.yapp.core.domain.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {
  @Id
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "token")
  private String token;

  public RefreshToken(Long userId, String token) {
    this.userId = userId;
    this.token = token;
  }

  public void updateToken(String token) {
    this.token = token;
  }
}
