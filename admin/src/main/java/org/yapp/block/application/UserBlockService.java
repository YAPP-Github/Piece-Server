package org.yapp.block.application;

import java.util.List;
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

    public PageResponse<UserBlockResponse> getUserBlockPageResponse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<DirectBlock> userBlockPage = directBlockRepository.findAll(pageable);

        List<UserBlockResponse> content = userBlockPage.getContent().stream()
            .map(userBlock -> {

                User blockingUser = userService.getUserById(userBlock.getBlockingUserId());
                User blockedUser = userService.getUserById(userBlock.getBlockedUserId());

                return new UserBlockResponse(
                    blockedUser.getId(),
                    blockedUser.getProfile().getProfileBasic().getNickname(),
                    blockedUser.getName(),
                    blockedUser.getProfile().getProfileBasic().getBirthdate(),
                    blockingUser.getId(),
                    blockingUser.getProfile().getProfileBasic().getNickname(),
                    blockingUser.getName(),
                    userBlock.getCreatedAt().toLocalDate());
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
