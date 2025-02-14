package org.yapp.core.domain.setting;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "block_contact_sync_time")
public class BlockContactSyncTime {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "sync_time")
  private LocalDateTime syncTime;

  public void updateSyncTime(LocalDateTime syncTime) {
    this.syncTime = syncTime;
  }
}
