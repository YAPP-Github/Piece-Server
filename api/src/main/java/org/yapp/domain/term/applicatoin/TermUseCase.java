package org.yapp.domain.term.applicatoin;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.term.TermAgreement;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.TermErrorCode;
import org.yapp.domain.term.applicatoin.dto.SignupTermsDto;
import org.yapp.domain.term.dao.TermAgreementRepository;
import org.yapp.domain.term.dao.TermRepository;
import org.yapp.domain.user.application.UserService;

@Service
@RequiredArgsConstructor
public class TermUseCase {

    private final TermRepository termRepository;
    private final TermAgreementRepository termAgreementRepository;
    private final UserService userService;

    @Transactional
    public void checkTermConstraints(SignupTermsDto dto) {
        List<Long> requiredTermIds = termRepository.findRequiredActiveTermIds();
        List<Long> agreedTermIds = dto.agreedTermsId();

        boolean hasAgreedToAllRequired = agreedTermIds.containsAll(requiredTermIds);

        if (!hasAgreedToAllRequired) {
            throw new ApplicationException(TermErrorCode.NOT_REQUIRED_TERM);
        }

        User user = userService.getUserById(dto.userId());

        List<Long> alreadyAgreedTermIds = termAgreementRepository.findByUserId(user.getId())
            .stream()
            .map(agreement -> agreement.getTerm().getId())
            .toList();

        List<Long> newAgreementTermIds = agreedTermIds.stream()
            .filter(termId -> !alreadyAgreedTermIds.contains(termId))
            .toList();

        List<TermAgreement> agreements = newAgreementTermIds.stream()
            .map(termId -> TermAgreement.builder()
                .user(user)
                .term(termRepository.findById(termId)
                    .orElseThrow(() -> new ApplicationException(TermErrorCode.NOTFOUND_TERM)))
                .agreedAt(LocalDateTime.now())
                .build())
            .toList();

        termAgreementRepository.saveAll(agreements);
    }
}
