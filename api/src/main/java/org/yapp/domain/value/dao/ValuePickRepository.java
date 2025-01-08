package org.yapp.domain.value.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.value.ValuePick;

@Repository
public interface ValuePickRepository extends JpaRepository<ValuePick, Long> {

    List<ValuePick> findAllByIsActiveTrue();
}
