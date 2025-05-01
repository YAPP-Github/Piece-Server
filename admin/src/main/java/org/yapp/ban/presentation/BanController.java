package org.yapp.ban.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.ban.application.BanService;
import org.yapp.ban.presentation.dto.request.UserBanRequest;
import org.yapp.format.CommonResponse;

@RestController
@RequestMapping("/admin/v1/bans")
@RequiredArgsConstructor
public class BanController {

  private final BanService banService;

  @PostMapping("/users")
  public ResponseEntity<CommonResponse<Void>> banUser(@RequestBody UserBanRequest request) {
    banService.banUser(request.userId());
    return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
  }
}
