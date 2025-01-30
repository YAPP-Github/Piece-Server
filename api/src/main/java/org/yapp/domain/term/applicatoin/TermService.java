package org.yapp.domain.term.applicatoin;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.term.Term;
import org.yapp.domain.term.dao.TermRepository;

@Service
@RequiredArgsConstructor
public class TermService {

    private final TermRepository termRepository;

    @Transactional(readOnly = true)
    public List<Term> getAllActiveTerms() {
        return termRepository.findAllByIsActiveTrue();
    }
}
