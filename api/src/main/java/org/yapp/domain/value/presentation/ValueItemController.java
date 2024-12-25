package org.yapp.domain.value.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.value.ValueItem;
import org.yapp.domain.value.application.ValueItemService;
import org.yapp.domain.value.presentation.dto.ValueItemResponses;
import org.yapp.util.ApiResponse;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/valueItems")
@RestController
@RequiredArgsConstructor
public class ValueItemController {
  private final ValueItemService valueItemService;

  @GetMapping()
  @Operation(summary = "가치관 질문 리스트 조회", description = "서비스에 등록된 모든 가치관 질문을 조회합니다.", tags = {"ValueItem"})
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가치관 리스트가 성공적으로 조회되었습니다.")
  public ResponseEntity<ApiResponse<ValueItemResponses>> getValueItems() {
    List<ValueItem> allValueItems = valueItemService.getAllValueItems();
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.createSuccess(ValueItemResponses.from(allValueItems)));
  }
}
