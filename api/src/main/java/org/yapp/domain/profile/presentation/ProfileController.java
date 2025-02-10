package org.yapp.domain.profile.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.user.User;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.profile.application.ProfileImageService;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.profile.application.ProfileValuePickService;
import org.yapp.domain.profile.application.ProfileValueTalkService;
import org.yapp.domain.profile.application.ProfileValueTalkSummaryService;
import org.yapp.domain.profile.presentation.request.ProfileBasicUpdateRequest;
import org.yapp.domain.profile.presentation.request.ProfileCreateRequest;
import org.yapp.domain.profile.presentation.request.ProfileValuePickUpdateRequest;
import org.yapp.domain.profile.presentation.request.ProfileValueTalkUpdateRequest;
import org.yapp.domain.profile.presentation.response.ProfileBasicPreviewResponse;
import org.yapp.domain.profile.presentation.response.ProfileBasicResponse;
import org.yapp.domain.profile.presentation.response.ProfileValuePickResponses;
import org.yapp.domain.profile.presentation.response.ProfileValueTalkResponses;
import org.yapp.domain.user.application.UserService;
import org.yapp.format.CommonResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;
    private final ProfileImageService profileImageService;
    private final ProfileValuePickService profileValuePickService;
    private final ProfileValueTalkService profileValueTalkService;
    private final ProfileValueTalkSummaryService profileValueTalkSummaryService;

    @PostMapping("")
    @Operation(summary = "프로필 생성", description = "현재 로그인된 사용자의 프로필을 생성합니다.", tags = {"프로필"})
    public ResponseEntity<CommonResponse<OauthLoginResponse>> createProfile(
        @RequestBody @Valid ProfileCreateRequest request) {

        Profile profile = profileService.create(request);
        OauthLoginResponse oauthLoginResponse = userService.completeProfileInitialize(profile);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CommonResponse.createSuccess(oauthLoginResponse));
    }

    @GetMapping("/basic")
    @Operation(summary = "프로필 기본 정보 조회", description = "현재 로그인된 사용자의 프로필 기본 정보를 조회합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필이 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<ProfileBasicResponse>> getProfileBasic(
        @AuthenticationPrincipal Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(ProfileBasicResponse.from(user.getProfile())));
    }

    @GetMapping("/basic/preview")
    @Operation(summary = "프로필 기본 정보 미리보기", description = "현재 로그인된 사용자의 프로필 기본 정보를 미리보기 합니다", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필 미리보기가 성공했습니다.")
    public ResponseEntity<CommonResponse<ProfileBasicPreviewResponse>> getProfileBasicPreview(
        @AuthenticationPrincipal Long userId) {
        User user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(
                ProfileBasicPreviewResponse.fromProfile(user.getProfile())));
    }

    @PutMapping("/basic")
    @Operation(summary = "프로필 기본 정보 업데이트", description = "현재 로그인된 사용자의 프로필 기본정보를 업데이트합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필 기본정보가 성공적으로 업데이트되었습니다.")
    public ResponseEntity<CommonResponse<ProfileBasicResponse>> updateProfile(
        @AuthenticationPrincipal Long userId,
        @RequestBody @Valid ProfileBasicUpdateRequest request) {
        Profile profile = profileService.updateProfileBasic(userId, request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(ProfileBasicResponse.from(profile)));
    }

    @GetMapping("/valuePicks")
    @Operation(summary = "프로필 가치관 Pick 정보 조회", description = "현재 로그인된 사용자가 입력한 프로필 가치관 Pick을 조회합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필이 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<ProfileValuePickResponses>> getProfilePick(
        @AuthenticationPrincipal Long userId) {
        ProfileValuePickResponses profileValuePickResponses = profileValuePickService.getProfileValuePickResponses(
            userId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(profileValuePickResponses));
    }

    @PutMapping("/valuePicks")
    @Operation(summary = "프로필 가치관 Pick 업데이트", description = "현재 로그인된 사용자가 입력한 프로필 가치관 Pick을 업데이트합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필 가치관 Pick을 성공적으로 업데이트하였습니다.")
    public ResponseEntity<CommonResponse<ProfileValuePickResponses>> updateProfilePick(
        @AuthenticationPrincipal Long userId,
        @RequestBody ProfileValuePickUpdateRequest request) {

        profileService.updateProfileValuePicks(userId, request);
        ProfileValuePickResponses profileValuePickResponses = profileValuePickService.getProfileValuePickResponses(
            userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(profileValuePickResponses));
    }

    @GetMapping("/valueTalks")
    @Operation(summary = "프로필 가치관 Talk 정보 조회", description = "현재 로그인된 사용자가 입력한 프로필 가치관 Talk을 조회합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필이 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<ProfileValueTalkResponses>> getProfileTalks(
        @AuthenticationPrincipal Long userId) {

        ProfileValueTalkResponses profileValueTalkResponses = profileValueTalkService.getProfileValueTalkResponses(
            userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(profileValueTalkResponses));
    }

    @PutMapping("/valueTalks")
    @Operation(summary = "프로필 가치관 Talk 업데이트", description = "현재 로그인된 사용자가 입력한 프로필 가치관 Talk 업데이트합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "프로필 가치관 Talk 성공적으로 업데이트하였습니다.")
    public ResponseEntity<CommonResponse<ProfileValueTalkResponses>> updateProfileTalks(
        @AuthenticationPrincipal Long userId,
        @RequestBody ProfileValueTalkUpdateRequest request) {

        profileService.updateProfileValueTalks(userId, request);
        profileValueTalkSummaryService.summaryProfileValueTalks(userId);

        ProfileValueTalkResponses profileValueTalkResponses = profileValueTalkService.getProfileValueTalkResponses(
            userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(profileValueTalkResponses));
    }


    @PostMapping("/check-nickname")
    @Operation(summary = "프로필 닉네임 중복 확인", description = "요청 파라미터로 전달된 닉네임이 중복되는지 확인합니다.", tags = {
        "프로필"})
    @ApiResponse(responseCode = "200", description = "중복되지 않은 닉네임입니다.")
    public ResponseEntity<CommonResponse<Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = profileService.isNicknameAvailable(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.createSuccess(isAvailable));
    }

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 등록", description = "업로드한 이미지를 버킷에 등록합니다.", tags = {"프로필 이미지"})
    @ApiResponse(responseCode = "200", description = "이미지가 버킷에 저장되었습니다.")
    public ResponseEntity<CommonResponse<String>> uploadProfileImage(
        @Parameter(description = "업로드할 프로필 이미지 파일 form-data 바이너리 파일 (JPEG, PNG, WEBP 지원)", required = true)
        @RequestParam("file") MultipartFile file) throws IOException {
        String profileImageUrl = profileImageService.uploadProfileImage(file);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(profileImageUrl));
    }
}
