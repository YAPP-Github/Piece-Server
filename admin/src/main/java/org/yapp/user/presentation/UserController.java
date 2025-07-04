package org.yapp.user.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.format.CommonResponse;
import org.yapp.format.PageResponse;
import org.yapp.profile.application.AdminProfileService;
import org.yapp.user.application.UserService;
import org.yapp.user.presentation.request.UpdateProfileStatusRequest;
import org.yapp.user.presentation.response.UserProfileDetailResponses;
import org.yapp.user.presentation.response.UserProfileImageDetailResponse;
import org.yapp.user.presentation.response.UserProfileValidationResponse;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
public class UserController {

    private final UserService userService;
    private final AdminProfileService adminProfileService;

    @GetMapping("")
    public ResponseEntity<CommonResponse<PageResponse<UserProfileValidationResponse>>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        PageResponse<UserProfileValidationResponse> userProfilesWithPagination = userService
            .getUserProfilesWithPagination(
                page, size);

        return ResponseEntity.ok(CommonResponse.createSuccess(userProfilesWithPagination));
    }


    @GetMapping("/search")
    public ResponseEntity<CommonResponse<UserProfileValidationResponse>> searchUser(
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long profileId,
        @RequestParam(required = false) String nickname
    ) {
        return ResponseEntity.ok(CommonResponse.createSuccess(
            userService.getUserProfileByUniqueKey(userId, profileId, nickname)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserProfileDetailResponses>> getUserProfile(
        @PathVariable Long userId) {

        UserProfileDetailResponses userProfileDetails = userService.getUserProfileDetails(userId);
        return ResponseEntity.ok(CommonResponse.createSuccess(userProfileDetails));
    }

    @GetMapping("/{userId}/profileImage")
    public ResponseEntity<CommonResponse<UserProfileImageDetailResponse>> getUserProfileImage(
        @PathVariable Long userId) {

        UserProfileImageDetailResponse userProfileImageDetails = userService.getUserProfileImageDetails(
            userId);

        return ResponseEntity.ok(CommonResponse.createSuccess(userProfileImageDetails));
    }

    @PostMapping("/{userId}/profile")
    public ResponseEntity<CommonResponse<Void>> updateUserProfileStatus(
        @PathVariable Long userId,
        @RequestBody @Valid UpdateProfileStatusRequest request) {

        adminProfileService.updateProfileStatus(userId, request.rejectImage(),
            request.rejectDescription());

        return ResponseEntity.ok(CommonResponse.createSuccess(null));
    }
}
