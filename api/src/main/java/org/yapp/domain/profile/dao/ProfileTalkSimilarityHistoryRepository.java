package org.yapp.domain.profile.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.profile.ProfileTalkSimilarityHistory;

@Repository
public interface ProfileTalkSimilarityHistoryRepository extends
    JpaRepository<ProfileTalkSimilarityHistory, Long> {

}