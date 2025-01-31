package org.yapp.domain.value.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.value.ValuePick;

@Repository
public interface ValuePickRepository extends JpaRepository<ValuePick, Long> {

    @Query("SELECT v FROM ValuePick v WHERE v.isActive = true ORDER BY v.id ASC")
    List<ValuePick> findAllActiveOrdered();
}
