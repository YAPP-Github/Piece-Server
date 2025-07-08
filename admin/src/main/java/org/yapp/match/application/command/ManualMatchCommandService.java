package org.yapp.match.application.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.match.ManualMatchHistory;
import org.yapp.core.domain.match.MatchInfo;
import org.yapp.core.domain.user.User;
import org.yapp.match.dao.ManualMatchHistoryRepository;
import org.yapp.match.presentation.request.ManualMatchCancelRequest;
import org.yapp.match.presentation.request.ManualMatchReservationRequest;
import org.yapp.user.dao.ManualMatchRepository;
import org.yapp.user.dao.UserRepository;

@Service
@RequiredArgsConstructor
public class ManualMatchCommandService {

    private final ManualMatchHistoryRepository manualMatchHistoryRepository;
    private final ManualMatchRepository manualMatchRepository;
    private final UserRepository userRepository;


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

    @Transactional
    public void executeReservedMatching(LocalDateTime now) {
        List<ManualMatchHistory> unmatchedReservedMatchingBeforeNow = manualMatchHistoryRepository.
            findByIsMatchedAndDateTimeLessThanEqual(false, now);

        unmatchedReservedMatchingBeforeNow.forEach(manualMatchHistory -> {
            manualMatchHistory.checkMatched();
            User user1 = userRepository.getReferenceById(manualMatchHistory.getUser1Id());
            User user2 = userRepository.getReferenceById(manualMatchHistory.getUser2Id());
            manualMatchRepository.save(new MatchInfo(LocalDate.now(), user1, user2, false));
        });
    }
}
