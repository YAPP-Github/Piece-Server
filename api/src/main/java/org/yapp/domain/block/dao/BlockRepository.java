package org.yapp.domain.block.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.block.Block;

import java.util.List;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {
  boolean existsByUserIdAndPhoneNumber(Long userId, String phoneNumber);

  List<Block> findBlocksByUserId(Long userId);
}
