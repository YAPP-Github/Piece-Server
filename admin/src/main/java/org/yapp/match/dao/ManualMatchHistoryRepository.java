package org.yapp.match.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.match.ManualMatchHistory;

public interface ManualMatchHistoryRepository extends JpaRepository<ManualMatchHistory, Long> {

}