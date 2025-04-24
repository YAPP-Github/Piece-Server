package org.yapp.block.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.yapp.block.dao.DirectBlockRepository;
import org.yapp.block.presentation.response.UserBlockResponse;
import org.yapp.core.domain.block.DirectBlock;
import org.yapp.core.domain.user.User;
import org.yapp.format.PageResponse;
import org.yapp.user.application.UserService;

@Service
@RequiredArgsConstructor
public class UserBlockService {

    private final DirectBlockRepository directBlockRepository;
    private final UserService userService;
    private final String NON_MEMBER_NAME = "탈퇴한 회원";

    public PageResponse<UserBlockResponse> getUserBlockPageResponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<DirectBlock> userBlockPage = directBlockRepository.findAll(pageable);

        List<UserBlockResponse> content = userBlockPage.getContent().stream()
            .map(userBlock -> {
                Optional<User> blockingUserOpt = userService.getUserByIdIfExists(
                    userBlock.getBlockingUserId());
                Optional<User> blockedUserOpt = userService.getUserByIdIfExists(
                    userBlock.getBlockedUserId());

                String blockedNickname = blockedUserOpt
                    .map(u -> u.getProfile().getProfileBasic().getNickname())
                    .orElse(NON_MEMBER_NAME);
                String blockedName = blockedUserOpt.map(User::getName).orElse(null);
                LocalDate blockedBirthdate = blockedUserOpt
                    .map(u -> u.getProfile().getProfileBasic().getBirthdate())
                    .orElse(null);

                String blockingNickname = blockingUserOpt
                    .map(u -> u.getProfile().getProfileBasic().getNickname())
                    .orElse(NON_MEMBER_NAME);
                String blockingName = blockingUserOpt.map(User::getName).orElse(null);

                return new UserBlockResponse(
                    blockedUserOpt.map(User::getId).orElse(null),
                    blockedNickname,
                    blockedName,
                    blockedBirthdate,
                    blockingUserOpt.map(User::getId).orElse(null),
                    blockingNickname,
                    blockingName,
                    userBlock.getCreatedAt().toLocalDate()
                );
            }).toList();

        return new PageResponse<>(
            content,
            userBlockPage.getNumber(),
            userBlockPage.getSize(),
            userBlockPage.getTotalPages(),
            userBlockPage.getTotalElements(),
            userBlockPage.isFirst(),
            userBlockPage.isLast()
        );
    }
}
