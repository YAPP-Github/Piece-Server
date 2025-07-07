package org.yapp.match.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.match.ManualMatchHistory;
import org.yapp.match.dao.ManualMatchHistoryRepository;
import org.yapp.match.presentation.request.ManualMatchCancelRequest;
import org.yapp.match.presentation.request.ManualMatchReservationRequest;

@Service
@RequiredArgsConstructor
public class ManualMatchReservationCommandService {

    private final ManualMatchHistoryRepository manualMatchHistoryRepository;

    @Transactional
    public void reserveManualMatching(ManualMatchReservationRequest request) {
        ManualMatchHistory manualMatchHistory =
            new ManualMatchHistory(request.user1Id(), request.user2Id(), request.dateTime(), false);
        manualMatchHistoryRepository.save(manualMatchHistory);
    }

    @Transactional
    public void cancelManualMatching(ManualMatchCancelRequest request) {
        manualMatchHistoryRepository.deleteById(request.manualMatchId());
    }
}
