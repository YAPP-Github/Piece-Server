package org.yapp.match.application.query;

import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.match.ManualMatchHistory;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.UserErrorCode;
import org.yapp.match.dao.ManualMatchHistoryRepository;
import org.yapp.match.presentation.response.ManualMatchHistoryResponse;
import org.yapp.user.dao.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManualMatchHistoryQueryService {

    private static final int PAGE_SIZE = 10;

    private final ManualMatchHistoryRepository manualMatchHistoryRepository;
    private final UserRepository userRepository;

    public List<ManualMatchHistoryResponse> getManualMatchHistory(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.DESC, "id"));
        Page<ManualMatchHistory> manualMatchHistoryPage = manualMatchHistoryRepository.findAll(
            pageRequest);

        List<ManualMatchHistoryResponse> manualMatchHistoryResponses = manualMatchHistoryPage
            .stream()
            .flatMap(manualMatchHistory -> {
                try {
                    Long user1Id = manualMatchHistory.getUser1Id();
                    Long user2Id = manualMatchHistory.getUser2Id();

                    User user1 = userRepository.findById(user1Id).orElseThrow(
                        () -> new ApplicationException(UserErrorCode.NOTFOUND_USER)
                    );
                    User user2 = userRepository.findById(user2Id).orElseThrow(
                        () -> new ApplicationException(UserErrorCode.NOTFOUND_USER)
                    );

                    String nickname1 = user1.getProfile().getProfileBasic().getNickname();
                    String nickname2 = user2.getProfile().getProfileBasic().getNickname();

                    return Stream.of(new ManualMatchHistoryResponse(
                        manualMatchHistory.getId(),
                        user1Id,
                        nickname1,
                        user2Id,
                        nickname2,
                        manualMatchHistory.getDateTime(),
                        manualMatchHistory.getIsMatched()
                    ));
                } catch (ApplicationException e) {
                    log.error("존재하지 않는 유저가 히스토리에 포함되어 있습니다. : {} or {}",
                        manualMatchHistory.getUser1Id(),
                        manualMatchHistory.getUser2Id());
                    return Stream.empty(); // 예외가 발생하면 해당 요소는 제외
                }
            })
            .toList();

        return manualMatchHistoryResponses;
    }
}
