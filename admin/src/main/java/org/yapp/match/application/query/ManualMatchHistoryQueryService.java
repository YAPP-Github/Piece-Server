package org.yapp.match.application.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.match.ManualMatchHistory;
import org.yapp.core.domain.user.User;
import org.yapp.match.dao.ManualMatchHistoryRepository;
import org.yapp.match.dto.response.ManualMatchHistoryResponse;
import org.yapp.user.dao.UserRepository;

@Service
@RequiredArgsConstructor
public class ManualMatchHistoryQueryService {

    private static final int PAGE_SIZE = 10;

    private final ManualMatchHistoryRepository manualMatchHistoryRepository;
    private final UserRepository userRepository;

    public List<ManualMatchHistoryResponse> getManualMatchHistory(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.DESC, "id"));
        Page<ManualMatchHistory> manualMatchHistoryPage = manualMatchHistoryRepository.findAll(
            pageRequest);
        List<ManualMatchHistoryResponse> manualMatchHistoryResponses = manualMatchHistoryPage.map(
            manualMatchHistory -> {
                Long user1Id = manualMatchHistory.getUser1Id();
                Long user2Id = manualMatchHistory.getUser2Id();
                User user1 = userRepository.findById(user1Id).get();
                User user2 = userRepository.findById(user2Id).get();

                return new ManualMatchHistoryResponse(
                    manualMatchHistory.getUser1Id(),
                    user1.getName(),
                    user2Id,
                    user2.getName(),
                    manualMatchHistory.getDateTime(),
                    manualMatchHistory.getIsMatched()
                );
            }).getContent();
        return manualMatchHistoryResponses;
    }
}
