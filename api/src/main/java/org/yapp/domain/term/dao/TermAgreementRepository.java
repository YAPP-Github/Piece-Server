package org.yapp.domain.term.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.term.TermAgreement;

@Repository
public interface TermAgreementRepository extends JpaRepository<TermAgreement, Long> {

    List<TermAgreement> findByUserId(Long userId);
}
