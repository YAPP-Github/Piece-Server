package org.yapp.core.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_delete_reason")
public class UserDeleteReason {

  @Id
  @Column(name = "user_id")
  private Long id;

  @Column(name = "reason")
  private String reason;
}
