package org.yapp.block.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.block.UserBlock;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

}
