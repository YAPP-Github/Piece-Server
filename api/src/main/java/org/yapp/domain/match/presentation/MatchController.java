package org.yapp.domain.match.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.domain.profile.ContactType;
import org.yapp.domain.block.application.DirectBlockService;
import org.yapp.domain.match.application.MatchService;
import org.yapp.domain.match.presentation.dto.response.ImageUrlResponse;
import org.yapp.domain.match.presentation.dto.response.MatchInfoResponse;
import org.yapp.domain.match.presentation.dto.response.MatchProfileBasicResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValuePickResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValueTalkResponse;
import org.yapp.domain.profile.presentation.response.ContactResponse;
import org.yapp.domain.profile.presentation.response.ContactResponses;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

  private final MatchService matchService;
  private final DirectBlockService directBlockService;

  @GetMapping("/infos")
  @Operation(summary = "매칭 정보 조회", description = "이번 매칭의 정보를 조회합니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<MatchInfoResponse>> getMatchInfo(
      @AuthenticationPrincipal Long userId) {
    MatchInfoResponse matchInfoResponse = matchService.getMatchInfoResponse(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccess(matchInfoResponse));
  }

  @PatchMapping("/pieces/check")
  @Operation(summary = "매칭 조각 확인 체크", description = "이번 매칭의 조각을 확인했음을 서버에 알립니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<Void>> checkMatchPiece(
      @AuthenticationPrincipal Long userId) {
    matchService.checkPiece(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccessWithNoContent());
  }

  @GetMapping("/profiles/basic")
  @Operation(summary = "매칭 프로필 기본정보 확인", description = "매칭 상대의 프로필 기본정보를 확인합니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<MatchProfileBasicResponse>> getBasicMatchProfile(
      @AuthenticationPrincipal Long userId) {
    MatchProfileBasicResponse matchProfileBasic = matchService.getMatchProfileBasic(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccess(matchProfileBasic));
  }

  @GetMapping("/values/talks")
  @Operation(summary = "매칭 상대 가치관 톡 확인", description = "매칭 상대의 가치관 톡을 확인합니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<MatchValueTalkResponse>> getMatchTalkValues(
      @AuthenticationPrincipal Long userId) {
    MatchValueTalkResponse matchValueTalk = matchService.getMatchValueTalk(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccess(matchValueTalk));
  }


  @GetMapping("/values/picks")
  @Operation(summary = "매칭 상대 가치관 픽 확인", description = "매칭 상대의 가치관 픽을 확인합니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<MatchValuePickResponse>> getMatchValuePicks(
      @AuthenticationPrincipal Long userId) {
    MatchValuePickResponse matchValuePickResponse = matchService.getMatchedUserValuePicks(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccess(matchValuePickResponse));
  }

  @GetMapping("/images")
  @Operation(summary = "매칭 상대 프로필 이미지 확인", description = "매칭 상대의 프로필 이미지를 확인합니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<ImageUrlResponse>> getMatchedUserImages(
      @AuthenticationPrincipal Long userId) {
    String matchedUserImageUrl = matchService.getMatchedUserImageUrl(userId);
    ImageUrlResponse imageUrlResponse = new ImageUrlResponse(matchedUserImageUrl);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccess(imageUrlResponse));
  }

  @PostMapping("/accept")
  @Operation(summary = "매칭 수락하기", description = "매칭을 수락합니다.", tags = {"매칭"})
  public ResponseEntity<CommonResponse<Void>> acceptMatch(@AuthenticationPrincipal Long userId) {
    matchService.acceptMatch(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccessWithNoContent());
  }

  @GetMapping("/contacts")
  @Operation(summary = "매칭 상대 연락처 조회", description = "매칭 상대의 연락처를 조회합니다", tags = {"매칭"})
  public ResponseEntity<CommonResponse<ContactResponses>> getContacts(
      @AuthenticationPrincipal Long userId) {
    Map<ContactType, String> contacts = matchService.getContacts(userId);
    List<ContactResponse> contactsList = ContactResponses.convert(contacts);
    ContactResponses contactResponses = new ContactResponses(contactsList);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccess(contactResponses));
  }

  @PostMapping("/{matchId}/blocks")
  @Operation(summary = "매칭 상대 차단", description = "매칭 상대를 차단합니다", tags = {"매칭"})
  public ResponseEntity<CommonResponse<Void>> blockUsers(
      @AuthenticationPrincipal Long userId,
      @PathVariable Long matchId) {
    directBlockService.blockMatchedUser(userId, matchId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccessWithNoContent("매칭 상대방을 차단하였습니다."));
  }

  @PutMapping("/refuse")
  @Operation(summary = "매칭 거절", description = "매칭을 거절합니다", tags = {"매칭"})
  public ResponseEntity<CommonResponse<Void>> refuseMatch(@AuthenticationPrincipal Long userId) {
    matchService.refuseMatch(userId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(CommonResponse.createSuccessWithNoContent());
  }
}
