package org.yapp.block.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.block.application.UserBlockService;
import org.yapp.block.presentation.response.UserBlockResponse;
import org.yapp.format.CommonResponse;
import org.yapp.format.PageResponse;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/admin/v1/blocks")
public class UserBlockController {

    private final UserBlockService userBlockService;

    @GetMapping("")
    public ResponseEntity<CommonResponse<PageResponse<UserBlockResponse>>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        PageResponse<UserBlockResponse> userBlockPageResponse = userBlockService.getUserBlockPageResponse(
            page, size);

        return ResponseEntity.ok(CommonResponse.createSuccess(userBlockPageResponse));
    }
}
