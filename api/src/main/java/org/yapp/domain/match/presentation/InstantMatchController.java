package org.yapp.domain.match.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.match.application.InstantMatchService;
import org.yapp.domain.match.presentation.dto.request.QdrantCreateRequest;
import org.yapp.domain.match.presentation.dto.response.FreeInstantMatchAvailabilityResponse;
import org.yapp.domain.match.presentation.dto.response.InstantMatchResponse;
import org.yapp.domain.match.presentation.dto.response.NewInstantMatchResponse;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches/instants")
public class InstantMatchController {

    private final InstantMatchService instantMatchService;

    @GetMapping("/free/today")
    @Operation(summary = "무료 즉시 매칭 가능 여부 반환", description = "오늘 무료로 즉시매칭 수행이 가능한지 여부를 반환합니다",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<FreeInstantMatchAvailabilityResponse>> getTodayFreeMatchingAvailability(
        @AuthenticationPrincipal Long userId
    ) {
        Boolean freeMatchAvailability = instantMatchService.canFreeInstantMatchToday(userId);
        FreeInstantMatchAvailabilityResponse freeInstantMatchAvailabilityResponse =
            new FreeInstantMatchAvailabilityResponse(freeMatchAvailability);
        return ResponseEntity.ok(
            CommonResponse.createSuccess(freeInstantMatchAvailabilityResponse));
    }

    @PostMapping("/new")
    @Operation(summary = "새로운 즉시 매칭 얻기", description = "새로운 즉시 매칭을 생성하고 새 매칭 ID를 반환합니다.",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<NewInstantMatchResponse>> getNewInstantMatch(
        @AuthenticationPrincipal Long userId
    ) {
        Long newInstantMatch = instantMatchService.getNewInstantMatch(userId);
        NewInstantMatchResponse newInstantMatchResponse = new NewInstantMatchResponse(
            newInstantMatch);
        return ResponseEntity.ok(CommonResponse.createSuccess(newInstantMatchResponse));
    }

    @GetMapping("/from-me")
    @Operation(summary = "나에 의해 생긴 즉시 매칭 조회", description = "나에 의해 생긴 즉시 매칭을 조회합니다",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<List<InstantMatchResponse>>> getInstantMatchesFromMe(
        @AuthenticationPrincipal Long userId
    ) {
        List<InstantMatchResponse> instantMatchFromMeList = instantMatchService.getInstantMatchFromMeList(
            userId);
        return ResponseEntity.ok(CommonResponse.createSuccess(instantMatchFromMeList));
    }

    @GetMapping("/to-me")
    @Operation(summary = "상대에 의해 생긴 즉시 매칭 조회", description = "상대에 의해 생긴 즉시 매칭을 조회합니다",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<List<InstantMatchResponse>>> getInstantMatchesToMe(
        @AuthenticationPrincipal Long userId
    ) {
        List<InstantMatchResponse> instantMatchToMeList = instantMatchService.getInstantMatchToMeList(
            userId);
        return ResponseEntity.ok(CommonResponse.createSuccess(instantMatchToMeList));
    }

    @PostMapping("/qdrant/collection")
    @Operation(summary = "Qdrant 컬렉션 생성", description = "장애 대응과 마이그레이션을 위한 Qdrant 컬렉션 생성 기능입니다",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<Void>> createQdrantCollection(
        @RequestBody QdrantCreateRequest request) {
        instantMatchService.createVectorCollection(request.vectorSize());
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }

    @DeleteMapping("/qdrant/collection")
    @Operation(summary = "Qdrant 컬렉션 삭제", description = "장애 대응과 마이그레이션을 위한 Qdrant 컬렉션 삭제 기능입니다",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<Void>> createQdrantCollection() {
        instantMatchService.deleteVectorCollection();
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }

    @PostMapping("/qdrant/collection/update")
    @Operation(summary = "Qdrant 벡터 업데이트", description = "장애 대응과 마이그레이션을 위한 Qdrant 벡터 업데이트 기능입니다",
        tags = {"매칭"})
    public ResponseEntity<CommonResponse<Void>> updateQdrantVectors() {
        instantMatchService.updateAllQdrantVectors();
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }
}
