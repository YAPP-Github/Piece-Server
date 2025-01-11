package org.yapp.domain.value.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yapp.domain.value.ValueTalk;
import java.util.List;

@Repository
public interface ValueTalkRepository extends JpaRepository<ValueTalk, Long> {
    @Query("SELECT v FROM ValueTalk v WHERE v.isActive = true ORDER BY v.id ASC")
    List<ValueTalk> findAllActiveOrdered();
}
