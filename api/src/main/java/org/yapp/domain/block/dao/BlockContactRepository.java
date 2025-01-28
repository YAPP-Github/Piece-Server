package org.yapp.domain.block.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.domain.block.BlockContact;

@Repository
public interface BlockContactRepository extends JpaRepository<BlockContact, Long> {

  List<BlockContact> findBlocksByUserId(Long userId);

  boolean existsByUser_IdAndPhoneNumber(Long userId, String phoneNumber);
}