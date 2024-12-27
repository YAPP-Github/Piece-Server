package org.yapp.domain.value.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.value.ValueItem;

@Repository
public interface ValueItemRepository extends JpaRepository<ValueItem, Long> {
}
