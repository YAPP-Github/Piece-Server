package org.yapp.domain.profile.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.presentation.request.ProfileUpdateRequest;
import org.yapp.domain.profile.presentation.response.ProfileResponse;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;
import org.yapp.util.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {
  private final ProfileService profileService;
  private final UserService userService;

  @GetMapping
  @Operation(summary = "프로필 조회", description = "현재 로그인된 사용자의 프로필을 조회합니다.", tags = {"Profile"})
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필이 성공적으로 조회되었습니다.")
  public ResponseEntity<CommonResponse<ProfileResponse>> updateProfile(@AuthenticationPrincipal Long userId) {
    User user = userService.getUserById(userId);
    return ResponseEntity.status(HttpStatus.OK)
                         .body(CommonResponse.createSuccess(ProfileResponse.from(user.getProfile())));
  }

  @PutMapping()
  @Operation(summary = "프로필 업데이트", description = "현재 로그인된 사용자의 프로필을 업데이트합니다.", tags = {"Profile"})
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "프로필이 성공적으로 업데이트되었습니다.")
  public ResponseEntity<CommonResponse<ProfileResponse>> updateProfile(@AuthenticationPrincipal Long userId,
      @RequestBody @Valid ProfileUpdateRequest request) {
    Profile profile = profileService.updateByUserId(userId, request);
    return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.createSuccess(ProfileResponse.from(profile)));
  }
}
