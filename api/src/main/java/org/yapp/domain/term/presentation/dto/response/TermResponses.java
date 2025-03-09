package org.yapp.domain.term.presentation.dto.response;

import java.util.List;
import org.yapp.core.domain.term.Term;

public record TermResponses(List<TermResponse> responses) {

    public static TermResponses from(List<Term> terms) {
        List<TermResponse> list = terms.stream().map(TermResponse::from).toList();
        return new TermResponses(list);
    }
}
