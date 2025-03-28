package org.yapp.domain.term.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.term.Term;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    @Query("SELECT t.id FROM Term t WHERE t.required = true AND t.isActive = true")
    List<Long> findRequiredActiveTermIds();

    @Query("SELECT t FROM Term t WHERE t.isActive = true")
    List<Term> findAllByIsActiveTrue();
}
