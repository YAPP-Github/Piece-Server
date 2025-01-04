package org.yapp.domain.term.presentation.dto.request;

import java.util.List;

public record TermAgreementRequest(List<Long> agreedTermsId) {
}
