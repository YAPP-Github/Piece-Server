package org.yapp.core.domain.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.notification.enums.NotificationType;

@Entity
@Getter
@NoArgsConstructor
public class NotificationHistory {

  @Id
  @Column(name = "notification_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "notification_type")
  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  @Column(name = "title")
  private String title;

  @Column(name = "body")
  private String body;

  @Column(name = "is_read")
  private Boolean isRead;

  @Column(name = "date_time")
  private LocalDateTime dateTime;

  @Builder
  public NotificationHistory(Long userId, NotificationType notificationType, String title,
      String body, Boolean isRead, LocalDateTime dateTime) {
    this.userId = userId;
    this.notificationType = notificationType;
    this.title = title;
    this.body = body;
    this.isRead = isRead;
    this.dateTime = dateTime;
  }
}
