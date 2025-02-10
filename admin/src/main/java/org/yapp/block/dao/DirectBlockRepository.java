package org.yapp.block.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.block.DirectBlock;

@Repository
public interface DirectBlockRepository extends JpaRepository<DirectBlock, Long> {

}
