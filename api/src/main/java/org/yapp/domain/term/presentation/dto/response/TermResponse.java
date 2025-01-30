package org.yapp.domain.term.presentation.dto.response;

import java.time.LocalDateTime;
import org.yapp.core.domain.term.Term;

public record TermResponse(
    Long termId,
    String title,
    String content,
    boolean required,
    LocalDateTime startDate
) {

    public static TermResponse from(Term term) {
        return new TermResponse(term.getId(), term.getTitle(), term.getContent(), term.isRequired(),
            term.getStartDate());
    }
}
