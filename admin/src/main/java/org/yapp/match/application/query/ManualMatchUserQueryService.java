package org.yapp.match.application.query;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.core.domain.user.User;
import org.yapp.match.dao.ManualMatchHistoryRepository;
import org.yapp.match.presentation.response.ManualMatchCandidateListResponse;
import org.yapp.match.presentation.response.ManualMatchCandidateResponse;
import org.yapp.user.dao.UserRepository;

@RestController
@RequiredArgsConstructor
public class ManualMatchUserQueryService {

    private static final int PAGE_SIZE = 10;
    private final UserRepository userRepository;
    private final ManualMatchHistoryRepository manualMatchHistoryRepository;

    public ManualMatchCandidateListResponse getCandidateList(int page, LocalDateTime matchTime) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.ASC, "id"));
        Page<User> userPage = userRepository.findAllByRole("USER", pageRequest);

        List<ManualMatchCandidateResponse> candidateList = userPage.map(user -> {
            ProfileBasic profileBasic = user.getProfile().getProfileBasic();
            boolean canBeMatchedAtThisTime = checkIfUserCanBeMatchedAtThisTime(matchTime, user);
            return new ManualMatchCandidateResponse(
                user.getId(), profileBasic.getNickname(), canBeMatchedAtThisTime);
        }).stream().toList();

        return new ManualMatchCandidateListResponse(candidateList);
    }

    private boolean checkIfUserCanBeMatchedAtThisTime(LocalDateTime matchTime, User user) {
        boolean isMatched = false;
        if (matchTime != null) {
            isMatched = manualMatchHistoryRepository.existsByUserIdAndDateTimeBetween(
                user.getId(),
                matchTime.minusHours(24),
                matchTime.plusHours(24));
        }
        return !isMatched;
    }
}
