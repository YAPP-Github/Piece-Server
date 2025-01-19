package org.yapp.user.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.yapp.domain.profile.ProfileRejectHistory;
import org.yapp.domain.user.User;
import org.yapp.error.dto.UserErrorCode;
import org.yapp.error.exception.ApplicationException;
import org.yapp.profile.dao.ProfileRejectHistoryRepository;
import org.yapp.user.dao.UserRepository;
import org.yapp.user.presentation.response.UserProfileValidationResponse;
import org.yapp.util.PageResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRejectHistoryRepository profileRejectHistoryRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ApplicationException(UserErrorCode.NOTFOUND_USER));
    }

    public PageResponse<UserProfileValidationResponse> getUserProfilesWithPagination(int page,
        int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserProfileValidationResponse> content = userPage.getContent().stream()
            .map(user -> {
                Optional<ProfileRejectHistory> optionalProfileRejectHistory =
                    profileRejectHistoryRepository.findTopByUserIdOrderByCreatedAtDesc(
                        user.getId());

                boolean reasonImage = optionalProfileRejectHistory.map(
                    ProfileRejectHistory::isReasonImage).orElse(false);
                boolean reasonDescription = optionalProfileRejectHistory.map(
                    ProfileRejectHistory::isReasonDescription).orElse(false);

                return UserProfileValidationResponse.from(user, reasonImage, reasonDescription);
            })
            .toList();

        // org.yapp.util.PageResponse 생성 및 반환
        return new PageResponse<>(
            content,
            userPage.getNumber(),
            userPage.getSize(),
            userPage.getTotalPages(),
            userPage.getTotalElements(),
            userPage.isFirst(),
            userPage.isLast()
        );
    }
}
