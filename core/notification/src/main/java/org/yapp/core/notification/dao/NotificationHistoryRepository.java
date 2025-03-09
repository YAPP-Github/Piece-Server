package org.yapp.core.notification.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.notification.NotificationHistory;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

}
