package org.yapp.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yapp.core.domain.match.MatchInfo;

public interface ManualMatchRepository extends JpaRepository<MatchInfo, Long> {
    
}
