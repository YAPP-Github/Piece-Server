package org.yapp.core.notification.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yapp.core.domain.notification.NotificationHistory;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

  @Query("select n from NotificationHistory n "
      + "where n.userId = :userId and (:cursor is null or n.id < :cursor) "
      + "order by n.id desc")
  public List<NotificationHistory> getNotificationHistories(Long cursor, Long userId,
      Pageable pageable);
}
