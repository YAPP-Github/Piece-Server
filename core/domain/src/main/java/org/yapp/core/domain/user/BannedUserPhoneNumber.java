package org.yapp.core.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "black_list_phone")
public class BannedUserPhoneNumber extends BaseEntity {

  @Id
  @Column(name = "phone_number")
  private String phoneNumber;
}
