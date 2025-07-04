package org.yapp.match.application.query;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.core.domain.user.User;
import org.yapp.match.dto.response.ManualMatchCandidateListResponse;
import org.yapp.match.dto.response.ManualMatchCandidateResponse;
import org.yapp.user.dao.UserRepository;

@RestController
@RequiredArgsConstructor
public class ManualMatchUserQueryService {

    private static final int PAGE_SIZE = 10;
    private final UserRepository userRepository;

    public ManualMatchCandidateListResponse getCandidateList(int page) {
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, Sort.by(Direction.DESC, "id"));
        Page<User> userPage = userRepository.findAll(pageRequest);

        List<ManualMatchCandidateResponse> candidateList = userPage.map(user -> {
            ProfileBasic profileBasic = user.getProfile().getProfileBasic();
            return new ManualMatchCandidateResponse(user.getId(), profileBasic.getNickname());
        }).stream().toList();

        return new ManualMatchCandidateListResponse(candidateList);
    }
}
